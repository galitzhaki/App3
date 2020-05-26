package com.example.naidich.tom.tomsprojectapplication.core.helpers;

import com.example.naidich.tom.tomsprojectapplication.core.interfaces.ILoggingService;
import com.example.naidich.tom.tomsprojectapplication.core.services.ConsoleLogService;

public class LogHelper {
    private static LogHelper _singletonInstance = new LogHelper();
    private ILoggingService logService = new ConsoleLogService();

    private LogHelper(){ }

    public static void logDebug(String msg) {
        _singletonInstance.logService.logDebug(msg);
    }

    public static void logWarning(String msg) {
        _singletonInstance.logService.logWarning(msg);
    }

    public static void logError(String msg) {
        _singletonInstance.logService.logError(msg);
    }
}
