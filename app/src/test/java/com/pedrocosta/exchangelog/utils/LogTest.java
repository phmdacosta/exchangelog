package com.pedrocosta.exchangelog.utils;

import com.pedrocosta.utils.output.Log;
import org.junit.jupiter.api.Test;

public class LogTest {

    @Test
    public void testInfoLog() {
        Log.info(this, "Test of info log.");
    }

    @Test
    public void testWarnLog() {
        Log.warn(this, "Test of warning log.");
    }

    @Test
    public void testErrorLog() {
        Log.error(this, "Test of error log.");
    }

    @Test
    public void testErrorLogWithException() {
        Log.error(this, new NullPointerException("Test of error log."));
    }

    @Test
    public void testDebugLog() {
        Log.debug(this, "Test of error log.");
    }
}
