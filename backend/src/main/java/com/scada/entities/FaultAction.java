package com.scada.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class FaultAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String signalId;  // e.g., RS4, RS5, etc.
    private String action;    // "open" or "close"
    private LocalDateTime timestamp; // Time when the user performed this action

    @ManyToOne
    private FaultSession faultSession; // Links actions to a single fault session
}
