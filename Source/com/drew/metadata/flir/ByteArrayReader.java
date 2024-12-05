package com.drew.metadata.flir;

import java.nio.charset.Charset;

public class ByteArrayReader extends IndexedReader {
    private final byte[] buffer;
    private final int baseOffset;

    public ByteArrayReader(byte[] buffer, int baseOffset, boolean isMotorolaByteOrder) {
        super(isMotorolaByteOrder);

        if (baseOffset < 0) {
            throw new IllegalArgumentException("Base offset must be zero or greater.");
        }

        if (buffer == null) {
            throw new IllegalArgumentException("Buffer cannot be null.");
        }

        this.buffer = buffer;
        this.baseOffset = baseOffset;
    }

    @Override
    public IndexedReader withByteOrder(boolean isMotorolaByteOrder) {
        return isMotorolaByteOrder == isMotorolaByteOrder() ? this :
            new ByteArrayReader(buffer, baseOffset, isMotorolaByteOrder);
    }

    @Override
    public IndexedReader withShiftedBaseOffset(int shift) {
        return shift == 0 ? this : new ByteArrayReader(buffer, baseOffset + shift, isMotorolaByteOrder());
    }

    @Override
    public int toUnshiftedOffset(int localOffset) {
        return localOffset + baseOffset;
    }

    @Override
    public long length() {
        return buffer.length - baseOffset;
    }

    @Override
    public void getBytes(int index, byte[] bytes) {
        validateIndex(index, bytes.length);

        System.arraycopy(buffer, index + baseOffset, bytes, 0, bytes.length);
    }

    @Override
    protected void validateIndex(int index, int bytesRequested) {
        if (!isValidIndex(index, bytesRequested)) {
            throw new BufferBoundsException(toUnshiftedOffset(index), bytesRequested, buffer.length);
        }
    }

    private boolean isValidIndex(int index, int bytesRequested) {
        return bytesRequested >= 0 &&
            index >= 0 &&
            index + bytesRequested - 1L < length();
    }

    // Additional utility method to read a string from the byte array
    public String getString(int index, int bytesRequested, Charset encoding) {
        if (bytesRequested < 0) {
            throw new IllegalArgumentException("Bytes requested must be zero or greater.");
        }

        if (bytesRequested == 0) {
            return "";
        }

        byte[] buffer = new byte[bytesRequested];
        getBytes(index, buffer);
        return new String(buffer, encoding);
    }

    // A utility method for reading a null-terminated string from the byte array
    public String getNullTerminatedString(int index, int maxLengthBytes, Charset encoding) {
        byte[] bytes = getNullTerminatedBytes(index, maxLengthBytes);
        return new String(bytes, encoding);
    }

    public byte[] getNullTerminatedBytes(int index, int maxLengthBytes) {
        byte[] buffer = new byte[maxLengthBytes];
        getBytes(index, buffer);

        // Count the number of non-null bytes
        int length = 0;
        while (length < buffer.length && buffer[length] != 0) {
            length++;
        }

        if (length == 0) {
            return new byte[0];
        }

        if (length == maxLengthBytes) {
            return buffer;
        }

        byte[] result = new byte[length];
        System.arraycopy(buffer, 0, result, 0, length);
        return result;
    }

    // A helper class to throw an exception for buffer out of bounds
    public static class BufferBoundsException extends RuntimeException {
        public BufferBoundsException(int offset, int bytesRequested, int bufferLength) {
            super("Requested bytes extend beyond the buffer bounds at offset " + offset + ". Requested: " +
                bytesRequested + ", Available: " + bufferLength);
        }
    }
}
