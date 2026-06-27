package com.scada.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class FaultSession {
    @Id
    @GeneratedValue
    private Long id;

    private String faultLine;

    private boolean resolved;

    @OneToMany(mappedBy = "faultSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FaultAction> actions = new ArrayList<>();

    // getters/setters
}