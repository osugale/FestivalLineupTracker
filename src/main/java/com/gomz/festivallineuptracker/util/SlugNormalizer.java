package com.gomz.festivallineuptracker.util;

import java.util.Locale;

public final class SlugNormalizer {

    private SlugNormalizer() {
    }

    public static String fromName(String name) {
        if (name == null) {
            return "";
        }

        String slug = name.trim().toLowerCase(Locale.ROOT);
        slug = slug.replace("&", " and ");
        slug = slug.replaceAll("[^a-z0-9]+", "-");
        slug = slug.replaceAll("^-+", "").replaceAll("-+$", "");
        slug = slug.replaceAll("-{2,}", "-");
        return slug;
    }
}
