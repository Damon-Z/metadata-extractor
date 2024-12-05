package com.drew.metadata.flir;

import com.drew.metadata.MetadataException;
import com.drew.metadata.TagDescriptor;

public class FlirCameraInfoDescriptor extends TagDescriptor<FlirCameraInfoDirectory> {

    public FlirCameraInfoDescriptor(FlirCameraInfoDirectory directory) {
        super(directory);
    }

    @Override
    public String getDescription(int tagType) {
        switch (tagType) {
            case FlirCameraInfoDirectory.TAG_REFLECTED_APPARENT_TEMPERATURE:
            case FlirCameraInfoDirectory.TAG_ATMOSPHERIC_TEMPERATURE:
            case FlirCameraInfoDirectory.TAG_IR_WINDOW_TEMPERATURE:
            case FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_RANGE_MAX:
            case FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_RANGE_MIN:
            case FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MAX_CLIP:
            case FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MIN_CLIP:
            case FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MAX_WARN:
            case FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MIN_WARN:
            case FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MAX_SATURATED:
            case FlirCameraInfoDirectory.TAG_CAMERA_TEMPERATURE_MIN_SATURATED:
                try {
                    return kelvinToCelsius(tagType);
                } catch (MetadataException e) {
                    throw new RuntimeException(e);
                }

            case FlirCameraInfoDirectory.TAG_RELATIVE_HUMIDITY:
                try {
                    return relativeHumidity(tagType);
                } catch (MetadataException e) {
                    throw new RuntimeException(e);
                }

            default:
                return super.getDescription(tagType);
        }
    }

    private String kelvinToCelsius(int tagType) throws MetadataException {
        float kelvin = this._directory.getFloat(tagType);
        float celsius = kelvin - 273.15f;
        return String.format("%.1f C", celsius);
    }

    private String relativeHumidity(int tagType) throws MetadataException {
        float value = this._directory.getFloat(tagType);
        float percentage = (value > 2 ? value / 100 : value) * 100;
        return String.format("%.1f %%", percentage);
    }
}

