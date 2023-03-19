package com.kainos.globalktabledemo;

import org.slf4j.Logger;

import java.util.function.Supplier;

public class LogTime {
    public LogTime() {
    }

    <T> T produceLogged(Supplier<T> function, String operationName, Logger logger) {
        var startTime = System.nanoTime();
        T result = function.get();
        var endTime = System.nanoTime();
        var duration = endTime - startTime;
        logger.info("operationName: {}" + " {} ns", operationName, duration);
        return result;
    }
}