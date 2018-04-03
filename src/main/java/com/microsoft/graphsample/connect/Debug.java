package com.microsoft.graphsample.connect;

import com.microsoft.graph.logger.LoggerLevel;

/**
 * Exposes a module wide debug vaiable that results in the conditional
 * execution of debug code.
 */
public  final class Debug {
    //set to false to allow compiler to identify and eliminate
    //unreachable code
    public static final LoggerLevel DebugLevel = LoggerLevel.ERROR;
}