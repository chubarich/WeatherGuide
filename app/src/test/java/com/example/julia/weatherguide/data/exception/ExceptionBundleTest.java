package com.example.julia.weatherguide.data.exception;

import com.example.julia.weatherguide.data.exceptions.ExceptionBundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ExceptionBundleTest {

    @Test
    public void getReason_returnValue() throws Exception{
        ExceptionBundle exceptionBundle = new ExceptionBundle(ExceptionBundle.Reason.API_ERROR);
        assertEquals(exceptionBundle.getReason(), ExceptionBundle.Reason.API_ERROR);

        exceptionBundle = new ExceptionBundle(ExceptionBundle.Reason.EMPTY_DATABASE);
        assertEquals(exceptionBundle.getReason(), ExceptionBundle.Reason.EMPTY_DATABASE);
    }

    @Test
    public void addExtra_getExtra_saveAndReturnExpected() throws Exception {
        String firstKey = "message";
        String firstValue = "Some message";
        String secondKey = "number";
        int secondValue = 128;
        String thirdKey = "other_message";
        String thirdValue = "Some other message";
        ExceptionBundle exceptionBundle = new ExceptionBundle(ExceptionBundle.Reason.API_ERROR);

        exceptionBundle.addStringExtra(firstKey, firstValue);
        exceptionBundle.addIntExtra(secondKey, secondValue);
        exceptionBundle.addStringExtra(thirdKey, thirdValue);

        assertEquals(exceptionBundle.getStringExtra(firstKey), firstValue);
        assertEquals(exceptionBundle.getIntExtra(secondKey), secondValue);
        assertEquals(exceptionBundle.getStringExtra(thirdKey), thirdValue);
    }

}
