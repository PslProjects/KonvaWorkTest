package com.scada.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fault_flex_config")
@Getter
@Setter
public class FaultFlexConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String faultLine;   // e.g. LG7
    private String variant;     // e.g. SERIES1
    private String blockName;   // e.g. START, FIX, END

    @Column(columnDefinition = "TEXT")
    private String sequenceJson; // e.g. ["RS8","RS6","RS4","RS12"]
}
