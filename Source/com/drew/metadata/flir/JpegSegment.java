package com.drew.metadata.flir;

import com.drew.imaging.jpeg.JpegSegmentType;

import java.util.Arrays;

public final class JpegSegment {

    private final JpegSegmentType type;
    private final byte[] bytes;
    private final long offset;

    public JpegSegment(JpegSegmentType type, byte[] bytes, long offset) {
        this.type = type;
        this.bytes = bytes.clone();  // To ensure immutability (cloning to avoid external modification)
        this.offset = offset;
    }

    public JpegSegmentType getType() {
        return type;
    }

    public byte[] getBytes() {
        return bytes.clone();  // Return a clone to preserve immutability
    }

    public long getOffset() {
        return offset;
    }

    // Simulating Span<byte> with a method that returns a slice of the byte array
    public byte[] getSpan() {
        return Arrays.copyOfRange(bytes, 0, bytes.length);
    }

    @Override
    public String toString() {
        return String.format("[%s] %d bytes at offset %d", type, bytes.length, offset);
    }
}

