package com.example.julia.weatherguide.utils;


import org.junit.Test;

public class PreconditionsTest {

    private static final String MESSAGE = "message";

    @Test(expected = NullPointerException.class)
    public void nonNull_throwsExceptionMessage() {
        Preconditions.nonNull(MESSAGE, Boolean.TRUE, null);
    }

    @Test(expected = NullPointerException.class)
    public void nonNull_throwsException() {
        Preconditions.nonNull(Boolean.TRUE, null);
    }

    @Test(expected = IllegalStateException.class)
    public void assertTrue_throwsExceptionMessage() {
        Preconditions.assertTrue(MESSAGE, false);
    }

    @Test(expected = IllegalStateException.class)
    public void assertTrue_throwsException() {
        Preconditions.assertTrue(false);
    }

    @Test(expected = IllegalStateException.class)
    public void assertFalse_throwsExceptionMessage() {
        Preconditions.assertFalse(MESSAGE, true);
    }

    @Test(expected = IllegalStateException.class)
    public void assertFalse_throwsException() {
        Preconditions.assertFalse(true);
    }

}
