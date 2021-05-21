package com.pedrocosta.exchangelog.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Log {

    private static final int TRACE = 0;
    private static final int DEBUG = 1;
    private static final int INFO = 2;
    private static final int WARN = 3;
    private static final int ERROR = 4;

    public static void trace(Object obj, String msg) {
        log(TRACE, obj, msg, null);
    }

    public static void debug(Object obj, String msg) {
        log(DEBUG, obj, msg, null);
    }

    public static void info(Object obj, String msg) {
        log(INFO, obj, msg, null);
    }

    public static void warn(Object obj, String msg) {
        log(WARN, obj, msg, null);
    }

    public static void error(Object obj, String msg) {
        log(ERROR, obj, msg, null);
    }

    public static void error(Object obj, Throwable throwable) {
        error(obj, null, throwable);
    }

    public static void error(Object obj, String msg, Throwable throwable) {
        if (msg != null)
            log(ERROR, obj, msg, null);

        if (throwable != null)
            log(ERROR, obj, null, throwable);
    }

    private static void log(int type, Object obj, String msg, Throwable throwable) {
        Logger logger = LogManager.getLogger(obj);
        switch(type) {
            case TRACE:
                logger.trace(msg);
                break;
            case DEBUG:
                logger.debug(msg);
                break;
            case INFO:
                logger.info(msg);
                break;
            case WARN:
                logger.warn(msg);
                break;
            case ERROR:
                if (throwable == null)
                    logger.error(msg);
                else
                    logger.error(obj, throwable);
                break;
            default:
                throw new IllegalArgumentException("Wrong log type");
        }
    }
}
