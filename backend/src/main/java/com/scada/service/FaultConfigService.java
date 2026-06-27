package com.scada.service;

import com.scada.entities.FaultConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FaultConfigService {

    /**
     * 🚫 DB Disabled — always load from static FaultConfig
     */
    public Map<String, List<String>> getFlexConfig(String faultLine, String variant) {

        // Always fetch from static config map
        Map<String, List<String>> blocks = FaultConfig.getFlexible(faultLine, variant);

        if (blocks == null) {
            System.err.println("⚠️ No static config found for " + faultLine + "_" + variant);
        }

        return blocks;
    }
}
