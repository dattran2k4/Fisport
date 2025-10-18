package com.Fisport.util;

import com.github.slugify.Slugify;

import java.util.Locale;


public class SlugUtils {
    private static final Slugify slugify = Slugify.builder().locale(new Locale("vi")).build();

    public SlugUtils() {
    }

    public static String slugify(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return slugify.slugify(text);
    }
}
