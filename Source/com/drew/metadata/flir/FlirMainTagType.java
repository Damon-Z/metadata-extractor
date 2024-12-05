package com.drew.metadata.flir;

public enum FlirMainTagType {

    // General
    UNUSED(0),
    PIXELS(1),
    GAIN_MAP(2),
    OFFS_MAP(3),
    DEAD_MAP(4),
    GAIN_DEAD_MAP(5),
    COARSE_MAP(6),
    IMAGE_MAP(7),

    // FLIR matrix tags
    BASIC_DATA(0x20),
    MEASURE(0x21),
    COLOR_PAL(0x22);

    private final int value;

    FlirMainTagType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Optional: Method to get enum from integer value
    public static FlirMainTagType fromValue(int value) {
        for (FlirMainTagType type : FlirMainTagType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

