package com.example.julia.weatherguide.utils;

import android.graphics.Path;

import org.junit.Test;
import static org.junit.Assert.*;


public class OptionalTest {

    @Test
    public void isPresent_returnValue() {
        String string = "2345677";
        Optional<String> notEmpty = Optional.of(string);
        assertTrue(notEmpty.isPresent());

        Optional empty = Optional.of(null);
        assertFalse(empty.isPresent());
    }

    @Test
    public void get_returnValue() {
        String string = "234567";
        Optional<String> optional = Optional.of(string);
        assertEquals(optional.get(), string);
    }


}
