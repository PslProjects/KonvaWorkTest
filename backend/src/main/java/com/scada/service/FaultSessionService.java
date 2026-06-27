package com.scada.service;

import org.springframework.stereotype.Service;

@Service
public class FaultSessionService {
    private volatile String lastTriggeredLine;

    public synchronized void setLastTriggeredLine(String line) {
        this.lastTriggeredLine = line;
    }

    public synchronized String getLastTriggeredLine() {
        return this.lastTriggeredLine;
    }
}