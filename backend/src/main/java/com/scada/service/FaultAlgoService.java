package com.scada.service;

import com.scada.entities.FaultAlgo;
import com.scada.repository.FaultAlgoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class FaultAlgoService {

    @Autowired
    private FaultAlgoRepository faultAlgoRepository;

    public Optional<FaultAlgo> getSequenceByLine(String line) {
        return faultAlgoRepository.findByLine(line);
    }
}
