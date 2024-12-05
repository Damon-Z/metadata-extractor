package com.drew.metadata.flir;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public abstract class IndexedReader {
    private final boolean isMotorolaByteOrder;

    public IndexedReader(boolean isMotorolaByteOrder) {
        this.isMotorolaByteOrder = isMotorolaByteOrder;
    }

    public boolean isMotorolaByteOrder() {
        return isMotorolaByteOrder;
    }

    public abstract IndexedReader withByteOrder(boolean isMotorolaByteOrder);

    public abstract IndexedReader withShiftedBaseOffset(int shift);

    public abstract int toUnshiftedOffset(int localOffset);

    public byte[] getBytes(int index, int count) throws IOException {
        validateIndex(index, count);

        byte[] bytes = new byte[count];

        getBytes(index, bytes);

        return bytes;
    }


    public abstract void getBytes(int index, byte[] bytes) throws IOException;

    protected abstract void validateIndex(int index, int bytesRequested) throws IOException;

    public abstract long length() throws IOException;

    public boolean getBit(int index) throws IOException {
        int byteIndex = index / 8;
        int bitIndex = index % 8;

        byte[] bytes = new byte[1];
        getBytes(byteIndex, bytes);

        return ((bytes[0] >> bitIndex) & 1) == 1;
    }

    public byte getByte(int index) throws IOException {
        byte[] bytes = new byte[1];
        getBytes(index, bytes);
        return bytes[0];
    }

    public byte getSByte(int index) throws IOException {
        return (byte) getByte(index);
    }

    public short getUInt16(int index) throws IOException {
        byte[] bytes = new byte[2];
        getBytes(index, bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(isMotorolaByteOrder ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        return buffer.getShort();
    }

    public short getInt16(int index) throws IOException {
        byte[] bytes = new byte[2];
        getBytes(index, bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(isMotorolaByteOrder ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        return buffer.getShort();
    }

    public int getInt24(int index) throws IOException {
        byte[] bytes = new byte[3];
        getBytes(index, bytes);

        int result;
        if (isMotorolaByteOrder) {
            result = (bytes[0] << 16) | (bytes[1] << 8) | (bytes[2]);
        } else {
            result = (bytes[2] << 16) | (bytes[1] << 8) | (bytes[0]);
        }
        return result;
    }

    public int getUInt32(int index) throws IOException {
        byte[] bytes = new byte[4];
        getBytes(index, bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(isMotorolaByteOrder ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }

    public int getInt32(int index) throws IOException {
        byte[] bytes = new byte[4];
        getBytes(index, bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(isMotorolaByteOrder ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }

    public long getInt64(int index) throws IOException {
        byte[] bytes = new byte[8];
        getBytes(index, bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(isMotorolaByteOrder ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        return buffer.getLong();
    }

    public long getUInt64(int index) throws IOException {
        byte[] bytes = new byte[8];
        getBytes(index, bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(isMotorolaByteOrder ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        return buffer.getLong();
    }

    public float getS15Fixed16(int index) throws IOException {
        byte[] bytes = new byte[4];
        getBytes(index, bytes);

        if (isMotorolaByteOrder) {
            int res = (bytes[0] << 8) | bytes[1];
            int d = (bytes[2] << 8) | bytes[3];
            return res + d / 65536.0f;
        } else {
            int d = (bytes[1] << 8) | bytes[0];
            int res = (bytes[3] << 8) | bytes[2];
            return res + d / 65536.0f;
        }
    }

    public float getFloat32(int index) throws IOException {
        byte[] bytes = new byte[4];
        getBytes(index, bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(isMotorolaByteOrder ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        return buffer.getFloat();
    }

    public double getDouble64(int index) throws IOException {
        byte[] bytes = new byte[8];
        getBytes(index, bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(isMotorolaByteOrder ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        return buffer.getDouble();
    }

    public String getString(int index, int bytesRequested, Charset encoding) throws IOException {
        if (bytesRequested < 0) {
            throw new IllegalArgumentException("Must be zero or greater.");
        }

        if (bytesRequested == 0) {
            return "";
        }

        byte[] buffer = getBytes(index, bytesRequested);
        return new String(buffer, encoding);
    }

    public String getNullTerminatedString(int index, int maxLengthBytes, Charset encoding) throws IOException {
        byte[] bytes = getNullTerminatedBytes(index, maxLengthBytes);
        return new String(bytes, encoding);
    }

    public byte[] getNullTerminatedBytes(int index, int maxLengthBytes) throws IOException {
        byte[] buffer = getBytes(index, maxLengthBytes);

        int length = 0;
        while (length < buffer.length && buffer[length] != 0) {
            length++;
        }

        byte[] result = new byte[length];
        System.arraycopy(buffer, 0, result, 0, length);
        return result;
    }
}
