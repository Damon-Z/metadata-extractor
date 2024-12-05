package com.drew.metadata.flir;

// Copyright (c) Drew Noakes and contributors. All Rights Reserved. Licensed under the Apache License, Version 2.0.

import com.drew.metadata.Directory;
import com.drew.metadata.TagDescriptor;

import java.util.HashMap;

public class FlirRawDataDirectory extends Directory {

    public static final int TAG_RAW_THERMAL_IMAGE_WIDTH = 2;
    public static final int TAG_RAW_THERMAL_IMAGE_HEIGHT = 4;
    public static final int TAG_RAW_THERMAL_IMAGE_TYPE = 33;
    public static final int TAG_RAW_THERMAL_IMAGE = 100;

    private static final HashMap<Integer, String> _nameByTag = new HashMap<>();

    static {
        _nameByTag.put(TAG_RAW_THERMAL_IMAGE_WIDTH, "Raw Thermal Image Width");
        _nameByTag.put(TAG_RAW_THERMAL_IMAGE_HEIGHT, "Raw Thermal Image Height");
        _nameByTag.put(TAG_RAW_THERMAL_IMAGE_TYPE, "Raw Thermal Image Type");
        _nameByTag.put(TAG_RAW_THERMAL_IMAGE, "Raw Thermal Image");
    }

    public FlirRawDataDirectory() {
        setDescriptor(new TagDescriptor<>(this));  // Assuming TagDescriptor is similar in Java
    }

    @Override
    public String getName() {
        return "FLIR Raw Data";
    }

    @Override
    protected HashMap<Integer, String> getTagNameMap() {
        return _nameByTag;
    }
}
