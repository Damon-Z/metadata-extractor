package com.drew.metadata.flir;

import com.drew.metadata.Directory;

import java.util.HashMap;

public class FlirCameraInfoDirectory extends Directory {

    public static final int TAG_EMISSIVITY = 32;
    public static final int TAG_OBJECT_DISTANCE = 36;
    public static final int TAG_REFLECTED_APPARENT_TEMPERATURE = 40;
    public static final int TAG_ATMOSPHERIC_TEMPERATURE = 44;
    public static final int TAG_IR_WINDOW_TEMPERATURE = 48;
    public static final int TAG_IR_WINDOW_TRANSMISSION = 52;
    public static final int TAG_RELATIVE_HUMIDITY = 60;
    public static final int TAG_PLANCK_R1 = 88;
    public static final int TAG_PLANCK_B = 92;
    public static final int TAG_PLANCK_F = 96;
    public static final int TAG_ATMOSPHERIC_TRANS_ALPHA1 = 112;
    public static final int TAG_ATMOSPHERIC_TRANS_ALPHA2 = 116;
    public static final int TAG_ATMOSPHERIC_TRANS_BETA1 = 120;
    public static final int TAG_ATMOSPHERIC_TRANS_BETA2 = 124;
    public static final int TAG_ATMOSPHERIC_TRANS_X = 128;
    public static final int TAG_CAMERA_TEMPERATURE_RANGE_MAX = 144;
    public static final int TAG_CAMERA_TEMPERATURE_RANGE_MIN = 148;
    public static final int TAG_CAMERA_TEMPERATURE_MAX_CLIP = 152;
    public static final int TAG_CAMERA_TEMPERATURE_MIN_CLIP = 156;
    public static final int TAG_CAMERA_TEMPERATURE_MAX_WARN = 160;
    public static final int TAG_CAMERA_TEMPERATURE_MIN_WARN = 164;
    public static final int TAG_CAMERA_TEMPERATURE_MAX_SATURATED = 168;
    public static final int TAG_CAMERA_TEMPERATURE_MIN_SATURATED = 172;
    public static final int TAG_CAMERA_MODEL = 212;
    public static final int TAG_CAMERA_PART_NUMBER = 244;
    public static final int TAG_CAMERA_SERIAL_NUMBER = 260;
    public static final int TAG_CAMERA_SOFTWARE = 276;
    public static final int TAG_LENS_MODEL = 368;
    public static final int TAG_LENS_PART_NUMBER = 400;
    public static final int TAG_LENS_SERIAL_NUMBER = 416;
    public static final int TAG_FIELD_OF_VIEW = 436;
    public static final int TAG_FILTER_MODEL = 492;
    public static final int TAG_FILTER_PART_NUMBER = 508;
    public static final int TAG_FILTER_SERIAL_NUMBER = 540;
    public static final int TAG_PLANCK_O = 776;
    public static final int TAG_PLANCK_R2 = 780;
    public static final int TAG_RAW_VALUE_RANGE_MIN = 784;
    public static final int TAG_RAW_VALUE_RANGE_MAX = 786;
    public static final int TAG_RAW_VALUE_MEDIAN = 824;
    public static final int TAG_RAW_VALUE_RANGE = 828;
    public static final int TAG_DATE_TIME_ORIGINAL = 900;
    public static final int TAG_FOCUS_STEP_COUNT = 912;
    public static final int TAG_FOCUS_DISTANCE = 1116;
    public static final int TAG_FRAME_RATE = 1124;

    @Override
    public String getName() {
        return "FLIR Camera Info";
    }

    @Override
    protected HashMap<Integer, String> getTagNameMap() {
        return nameByTag;
    }

    private static final HashMap<Integer, String> nameByTag = new HashMap<>();

    static {
        nameByTag.put(TAG_EMISSIVITY, "Emissivity");
        nameByTag.put(TAG_OBJECT_DISTANCE, "Object Distance");
        nameByTag.put(TAG_REFLECTED_APPARENT_TEMPERATURE, "Reflected Apparent Temperature");
        nameByTag.put(TAG_ATMOSPHERIC_TEMPERATURE, "Atmospheric Temperature");
        nameByTag.put(TAG_IR_WINDOW_TEMPERATURE, "IR Window Temperature");
        nameByTag.put(TAG_IR_WINDOW_TRANSMISSION, "IR Window Transmission");
        nameByTag.put(TAG_RELATIVE_HUMIDITY, "Relative Humidity");
        nameByTag.put(TAG_PLANCK_R1, "Planck R1");
        nameByTag.put(TAG_PLANCK_B, "Planck B");
        nameByTag.put(TAG_PLANCK_F, "Planck F");
        nameByTag.put(TAG_ATMOSPHERIC_TRANS_ALPHA1, "Atmospheric Trans Alpha1");
        nameByTag.put(TAG_ATMOSPHERIC_TRANS_ALPHA2, "Atmospheric Trans Alpha2");
        nameByTag.put(TAG_ATMOSPHERIC_TRANS_BETA1, "Atmospheric Trans Beta1");
        nameByTag.put(TAG_ATMOSPHERIC_TRANS_BETA2, "Atmospheric Trans Beta2");
        nameByTag.put(TAG_ATMOSPHERIC_TRANS_X, "Atmospheric Trans X");
        nameByTag.put(TAG_CAMERA_TEMPERATURE_RANGE_MAX, "Camera Temperature Range Max");
        nameByTag.put(TAG_CAMERA_TEMPERATURE_RANGE_MIN, "Camera Temperature Range Min");
        nameByTag.put(TAG_CAMERA_TEMPERATURE_MAX_CLIP, "Camera Temperature Max Clip");
        nameByTag.put(TAG_CAMERA_TEMPERATURE_MIN_CLIP, "Camera Temperature Min Clip");
        nameByTag.put(TAG_CAMERA_TEMPERATURE_MAX_WARN, "Camera Temperature Max Warn");
        nameByTag.put(TAG_CAMERA_TEMPERATURE_MIN_WARN, "Camera Temperature Min Warn");
        nameByTag.put(TAG_CAMERA_TEMPERATURE_MAX_SATURATED, "Camera Temperature Max Saturated");
        nameByTag.put(TAG_CAMERA_TEMPERATURE_MIN_SATURATED, "Camera Temperature Min Saturated");
        nameByTag.put(TAG_CAMERA_MODEL, "Camera Model");
        nameByTag.put(TAG_CAMERA_PART_NUMBER, "Camera Part Number");
        nameByTag.put(TAG_CAMERA_SERIAL_NUMBER, "Camera Serial Number");
        nameByTag.put(TAG_CAMERA_SOFTWARE, "Camera Software");
        nameByTag.put(TAG_LENS_MODEL, "Lens Model");
        nameByTag.put(TAG_LENS_PART_NUMBER, "Lens Part Number");
        nameByTag.put(TAG_LENS_SERIAL_NUMBER, "Lens Serial Number");
        nameByTag.put(TAG_FIELD_OF_VIEW, "Field Of View");
        nameByTag.put(TAG_FILTER_MODEL, "Filter Model");
        nameByTag.put(TAG_FILTER_PART_NUMBER, "Filter Part Number");
        nameByTag.put(TAG_FILTER_SERIAL_NUMBER, "Filter Serial Number");
        nameByTag.put(TAG_PLANCK_O, "Planck O");
        nameByTag.put(TAG_PLANCK_R2, "Planck R2");
        nameByTag.put(TAG_RAW_VALUE_RANGE_MIN, "Raw Value Range Min");
        nameByTag.put(TAG_RAW_VALUE_RANGE_MAX, "Raw Value Range Max");
        nameByTag.put(TAG_RAW_VALUE_MEDIAN, "Raw Value Median");
        nameByTag.put(TAG_RAW_VALUE_RANGE, "Raw Value Range");
        nameByTag.put(TAG_DATE_TIME_ORIGINAL, "Date Time Original");
        nameByTag.put(TAG_FOCUS_STEP_COUNT, "Focus Step Count");
        nameByTag.put(TAG_FOCUS_DISTANCE, "Focus Distance");
        nameByTag.put(TAG_FRAME_RATE, "Frame Rate");
    }

    public FlirCameraInfoDirectory() {
        this.setDescriptor(new FlirCameraInfoDescriptor(this));
    }
}
