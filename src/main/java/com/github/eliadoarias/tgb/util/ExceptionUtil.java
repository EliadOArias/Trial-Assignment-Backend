package com.github.eliadoarias.tgb.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionUtil {
    public static void printError(Exception e){
        log.error("Catch an error", e);
    }
}
