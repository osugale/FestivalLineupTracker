package com.gomz.festivallineuptracker.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlugNormalizerTest {

    @Test
    void fromName_normalizesAmpersandCaseAndPunctuation() {
        assertEquals("chase-and-status", SlugNormalizer.fromName("Chase & Status"));
        assertEquals("chase-and-status", SlugNormalizer.fromName("Chase and Status"));
        assertEquals("drum-and-bass", SlugNormalizer.fromName(" Drum & Bass "));
        assertEquals("", SlugNormalizer.fromName("???"));
    }
}
