package com.drew.metadata.flir;

import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class FlirReader implements JpegSegmentMetadataReader {
    public static final String JPEG_SEGMENT_PREAMBLE = "FLIR\u0000";

    @Override
    public List<JpegSegmentType> getSegmentTypes() {
        // Use Arrays.asList to create a list of segment types compatible with Java 8
        return java.util.Collections.singletonList(JpegSegmentType.APP1);
    }

    @Override
    public void readJpegSegments(Iterable<byte[]> segments, Metadata metadata, JpegSegmentType segmentType) {
        int preambleLength = JPEG_SEGMENT_PREAMBLE.length() + 3;
        int totalLength = 0;

        for (byte[] segmentBytes : segments) {
            if (startsWithPreamble(segmentBytes)) {
                totalLength += segmentBytes.length - preambleLength;
            }
        }

        if (totalLength == 0) {
            return;
        }

        byte[] buffer = new byte[totalLength];
        int bufferOffset = 0;

        // Merge segments
        for (byte[] segmentBytes : segments) {
            if (startsWithPreamble(segmentBytes)) {
                System.arraycopy(segmentBytes, preambleLength, buffer, bufferOffset, segmentBytes.length - preambleLength);
                bufferOffset += segmentBytes.length - preambleLength;
            }
        }
        ByteArrayReader reader = new ByteArrayReader(buffer,0, true);
        try {
            List<Directory> directories = extract(reader);
            for (Directory directory : directories) {
                metadata.addDirectory(directory);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean startsWithPreamble(byte[] bytes) {
        if (bytes.length < JPEG_SEGMENT_PREAMBLE.length()) return false;
        for (int i = 0; i < JPEG_SEGMENT_PREAMBLE.length(); i++) {
            if (bytes[i] != JPEG_SEGMENT_PREAMBLE.charAt(i)) return false;
        }
        return true;
    }

    private List<Directory> extract(IndexedReader reader) throws IOException {
        List<Directory> directories = new ArrayList<>();
        byte[] header = reader.getBytes(0, 4);

        if (!new String(header).equals("FFF\u0000")) {
            FlirHeaderDirectory flirHeaderDirectory = new FlirHeaderDirectory();
            flirHeaderDirectory.addError("Unexpected FFF header bytes.");
            directories.add(flirHeaderDirectory);
            return directories;
        }

        FlirHeaderDirectory headerDirectory = new FlirHeaderDirectory();
        headerDirectory.setObject(FlirHeaderDirectory.TAG_CREATOR_SOFTWARE, reader.getNullTerminatedString(4, 16, Charset.defaultCharset()));
        directories.add(headerDirectory);

        int baseIndexOffset = reader.getUInt32(24);
        int indexCount = reader.getUInt32(28);

        int indexOffset = baseIndexOffset;
        for (int i = 0; i < indexCount; i++) {
            int mainType = reader.getUInt16(indexOffset);
            int subType = reader.getUInt16(indexOffset + 2);
            int version = reader.getUInt32(indexOffset + 4);
            int id = reader.getUInt32(indexOffset + 8);
            int dataOffset = reader.getInt32(indexOffset + 12);
            int dataLength = reader.getInt32(indexOffset + 16);

            if (mainType == FlirMainTagType.PIXELS.getValue()) {
                extractPixels(reader, dataOffset, dataLength, directories);
            } else if (mainType == FlirMainTagType.BASIC_DATA.getValue()) {
                extractBasicData(reader, dataOffset, directories);
            }

            indexOffset += 32;
        }
        return directories;
    }

    private void extractPixels(IndexedReader reader, int dataOffset, int dataLength, List<Directory> directories) throws IOException {
        IndexedReader shiftedReader = reader.withShiftedBaseOffset(dataOffset);
        int marker = shiftedReader.getUInt16(0);
        if (marker > 0x0100) {
            shiftedReader = shiftedReader.withByteOrder(!shiftedReader.isMotorolaByteOrder());
        }

        FlirRawDataDirectory directory = new FlirRawDataDirectory();
        directory.setObject(FlirRawDataDirectory.TAG_RAW_THERMAL_IMAGE_WIDTH, shiftedReader.getUInt16(FlirRawDataDirectory.TAG_RAW_THERMAL_IMAGE_WIDTH));
        directory.setObject(FlirRawDataDirectory.TAG_RAW_THERMAL_IMAGE_HEIGHT, shiftedReader.getUInt16(FlirRawDataDirectory.TAG_RAW_THERMAL_IMAGE_HEIGHT));
        directory.setObject(FlirRawDataDirectory.TAG_RAW_THERMAL_IMAGE_TYPE, shiftedReader.getNullTerminatedString(FlirRawDataDirectory.TAG_RAW_THERMAL_IMAGE_TYPE, 5, Charset.defaultCharset()));
        directory.setObject(FlirRawDataDirectory.TAG_RAW_THERMAL_IMAGE, shiftedReader.getBytes(32, dataLength - 32));

        directories.add(directory);
    }

    private void extractBasicData(IndexedReader reader, int dataOffset, List<Directory> directories) throws IOException {
        IndexedReader shiftedReader = reader.withShiftedBaseOffset(dataOffset);
        int marker = shiftedReader.getUInt16(0);
        if (marker > 0x0100) {
            shiftedReader = shiftedReader.withByteOrder(!shiftedReader.isMotorolaByteOrder());
        }

        FlirCameraInfoDirectory directory = new FlirCameraInfoDirectory();
        directory.setObject(FlirCameraInfoDirectory.TAG_EMISSIVITY, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_EMISSIVITY));
        directory.setObject(FlirCameraInfoDirectory.TAG_OBJECT_DISTANCE, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_OBJECT_DISTANCE));
        directory.setObject(FlirCameraInfoDirectory.TAG_REFLECTED_APPARENT_TEMPERATURE, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_REFLECTED_APPARENT_TEMPERATURE));
        directory.setObject(FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TEMPERATURE, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TEMPERATURE));
        directory.setObject(FlirCameraInfoDirectory.TAG_IR_WINDOW_TEMPERATURE, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_IR_WINDOW_TEMPERATURE));
        directory.setObject(FlirCameraInfoDirectory.TAG_IR_WINDOW_TRANSMISSION, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_IR_WINDOW_TRANSMISSION));
        directory.setObject(FlirCameraInfoDirectory.TAG_RELATIVE_HUMIDITY, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_RELATIVE_HUMIDITY));
        directory.setObject(FlirCameraInfoDirectory.TAG_PLANCK_R1, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_PLANCK_R1));
        directory.setObject(FlirCameraInfoDirectory.TAG_PLANCK_B, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_PLANCK_B));
        directory.setObject(FlirCameraInfoDirectory.TAG_PLANCK_F, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_PLANCK_F));
        directory.setObject(FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TRANS_ALPHA1, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TRANS_ALPHA1));
        directory.setObject(FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TRANS_ALPHA2, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TRANS_ALPHA2));
        directory.setObject(FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TRANS_BETA1, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TRANS_BETA1));
        directory.setObject(FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TRANS_BETA2, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TRANS_BETA2));
        directory.setObject(FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TRANS_X, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TRANS_X));
        directory.setObject(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_RANGE_MAX, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_RANGE_MAX));
        directory.setObject(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_RANGE_MIN, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_RANGE_MIN));
        directory.setObject(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MAX_CLIP, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MAX_CLIP));
        directory.setObject(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MIN_CLIP, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MIN_CLIP));
        directory.setObject(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MAX_WARN, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MAX_WARN));
        directory.setObject(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MIN_WARN, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MIN_WARN));
        directory.setObject(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MAX_SATURATED, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MAX_SATURATED));
        directory.setObject(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MIN_SATURATED, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MIN_SATURATED));
        directory.setObject(FlirCameraInfoDirectory.TAG_CAMERA_MODEL, shiftedReader.getNullTerminatedString(FlirCameraInfoDirectory.TAG_CAMERA_MODEL,32, Charset.defaultCharset()));
        directory.setObject(FlirCameraInfoDirectory.TAG_CAMERA_PART_NUMBER, shiftedReader.getNullTerminatedString(FlirCameraInfoDirectory.TAG_CAMERA_PART_NUMBER,16, Charset.defaultCharset()));
        directory.setObject(FlirCameraInfoDirectory.TAG_CAMERA_SERIAL_NUMBER, shiftedReader.getNullTerminatedString(FlirCameraInfoDirectory.TAG_CAMERA_SERIAL_NUMBER,16, Charset.defaultCharset()));
        directory.setObject(FlirCameraInfoDirectory.TAG_CAMERA_SOFTWARE, shiftedReader.getNullTerminatedString(FlirCameraInfoDirectory.TAG_CAMERA_SOFTWARE,16, Charset.defaultCharset()));
        directory.setObject(FlirCameraInfoDirectory.TAG_LENS_MODEL, shiftedReader.getNullTerminatedString(FlirCameraInfoDirectory.TAG_LENS_MODEL,32, Charset.defaultCharset()));
        directory.setObject(FlirCameraInfoDirectory.TAG_LENS_PART_NUMBER, shiftedReader.getNullTerminatedString(FlirCameraInfoDirectory.TAG_LENS_PART_NUMBER,16, Charset.defaultCharset()));
        directory.setObject(FlirCameraInfoDirectory.TAG_LENS_SERIAL_NUMBER, shiftedReader.getNullTerminatedString(FlirCameraInfoDirectory.TAG_LENS_SERIAL_NUMBER,16, Charset.defaultCharset()));
        directory.setObject(FlirCameraInfoDirectory.TAG_FIELD_OF_VIEW, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_FIELD_OF_VIEW));
        directory.setObject(FlirCameraInfoDirectory.TAG_FILTER_MODEL, shiftedReader.getNullTerminatedString(FlirCameraInfoDirectory.TAG_FILTER_MODEL,16, Charset.defaultCharset()));
        directory.setObject(FlirCameraInfoDirectory.TAG_FILTER_PART_NUMBER, shiftedReader.getNullTerminatedString(FlirCameraInfoDirectory.TAG_FILTER_PART_NUMBER,32, Charset.defaultCharset()));
        directory.setObject(FlirCameraInfoDirectory.TAG_FILTER_SERIAL_NUMBER, shiftedReader.getNullTerminatedString(FlirCameraInfoDirectory.TAG_FILTER_SERIAL_NUMBER,32, Charset.defaultCharset()));
        directory.setObject(FlirCameraInfoDirectory.TAG_PLANCK_O, shiftedReader.getInt32(FlirCameraInfoDirectory.TAG_PLANCK_O));
        directory.setObject(FlirCameraInfoDirectory.TAG_PLANCK_R2, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_PLANCK_R2));
        directory.setObject(FlirCameraInfoDirectory.TAG_RAW_VALUE_RANGE_MIN, shiftedReader.getUInt16(FlirCameraInfoDirectory.TAG_RAW_VALUE_RANGE_MIN));
        directory.setObject(FlirCameraInfoDirectory.TAG_RAW_VALUE_RANGE_MAX, shiftedReader.getUInt32(FlirCameraInfoDirectory.TAG_RAW_VALUE_RANGE_MAX));
        directory.setObject(FlirCameraInfoDirectory.TAG_RAW_VALUE_MEDIAN, shiftedReader.getUInt16(FlirCameraInfoDirectory.TAG_RAW_VALUE_MEDIAN));
        directory.setObject(FlirCameraInfoDirectory.TAG_RAW_VALUE_RANGE, shiftedReader.getUInt16(FlirCameraInfoDirectory.TAG_RAW_VALUE_RANGE));

        byte[] dateTimeBytes = shiftedReader.getBytes(FlirCameraInfoDirectory.TAG_DATE_TIME_ORIGINAL, 10);
        directory.setObject(FlirCameraInfoDirectory.TAG_DATE_TIME_ORIGINAL, processDateTime(dateTimeBytes));

        directory.setObject(FlirCameraInfoDirectory.TAG_FOCUS_STEP_COUNT, shiftedReader.getUInt16(FlirCameraInfoDirectory.TAG_FOCUS_STEP_COUNT));
        directory.setObject(FlirCameraInfoDirectory.TAG_FOCUS_DISTANCE, shiftedReader.getFloat32(FlirCameraInfoDirectory.TAG_FOCUS_DISTANCE));
        directory.setObject(FlirCameraInfoDirectory.TAG_FRAME_RATE, shiftedReader.getUInt16(FlirCameraInfoDirectory.TAG_FRAME_RATE));

        directories.add(directory);
    }

    public static ZonedDateTime processDateTime(byte[] dateTimeBytes) {
        if (dateTimeBytes.length != 10) {
            throw new IllegalArgumentException("Expected byte array of length 10");
        }

        // 使用 ByteBuffer 来读取字节
        ByteBuffer buffer = ByteBuffer.wrap(dateTimeBytes).order(ByteOrder.LITTLE_ENDIAN);

        // 读取时间戳和其他值
        long tm = Integer.toUnsignedLong(buffer.getInt()); // 4 字节
        int ss = buffer.getInt() & 0xFFFF;                 // 4 字节中的低 16 位
        short tz = buffer.getShort();                     // 2 字节

        // 将 Unix 时间戳转换为 Java 的 Instant
        Instant instant = Instant.ofEpochSecond(tm - tz * 60).plus((long) (ss / 1000d), ChronoUnit.MILLIS);

        // 根据偏移量创建 DateTimeOffset（在 Java 中为 ZonedDateTime）
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(tz * 60);
        ZonedDateTime dateTime = instant.atZone(offset);
        return dateTime;
    }

}




