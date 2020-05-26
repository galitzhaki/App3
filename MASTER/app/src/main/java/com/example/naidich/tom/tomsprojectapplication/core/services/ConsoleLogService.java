package com.example.naidich.tom.tomsprojectapplication.core.services;

import com.example.naidich.tom.tomsprojectapplication.core.providers.LogProvider;

import static java.lang.System.*;

public class ConsoleLogService extends LogProvider {
    public ConsoleLogService() {
        super();
        logConfiguration.isDebugLogEnabled = false;
        logConfiguration.isWarningLogEnabled = true;
        logConfiguration.isErrorLogEnabled = true;
    }

    @Override
    protected void writeLog(String msg) {
        out.println(msg);
    }
}
