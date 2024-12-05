package com.drew.metadata.flir;

import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import com.drew.metadata.TagDescriptor;

import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public class FlirHeaderDirectory extends Directory {

    public static final int TAG_CREATOR_SOFTWARE = 0;

    @NotNull
    private static final HashMap<Integer, String> nameByTag = new HashMap<>();

    static {
        nameByTag.put(TAG_CREATOR_SOFTWARE, "Creator Software");
    }

    public FlirHeaderDirectory() {
        this.setDescriptor(new TagDescriptor<>(this));
    }

    @Override
    public String getName() {
        return "FLIR Header";
    }

    @Override
    protected HashMap<Integer, String> getTagNameMap() {
        return nameByTag;
    }
}
