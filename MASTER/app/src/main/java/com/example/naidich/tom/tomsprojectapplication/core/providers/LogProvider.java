package com.example.naidich.tom.tomsprojectapplication.core.providers;

import com.example.naidich.tom.tomsprojectapplication.core.interfaces.ILoggingService;
import com.example.naidich.tom.tomsprojectapplication.core.models.LogServiceConfiguration;

public abstract class LogProvider implements ILoggingService{
    protected LogServiceConfiguration logConfiguration;

    protected LogProvider(){
        logConfiguration = new LogServiceConfiguration();
    }

    @Override
    public void logDebug(String msg) {
        writeLog("DEBUG: " + msg);
    }

    @Override
    public void logWarning(String msg) {
        writeLog("WARNING: " + msg);
    }

    @Override
    public void logError(String msg) {
        writeLog("ERROR: " + msg);
    }

    protected abstract void writeLog(String msg);
}
