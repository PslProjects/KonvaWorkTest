package com.scada.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "line_rule")
public class LineRule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Long circuitId;   // purana (optional, rehne do)

    @Column(name = "circuit_name")
    private String circuitName;   // <-- NAYA: ab isi se rules jodenge

    @Column(name = "when_condition", length = 500)
    private String whenCondition;

    @Column(name = "lines_csv", length = 500)
    private String linesCsv;

    private String stroke = "red";
    private Integer priority = 1;

    // --- getters / setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

//    public Long getCircuitId() { return circuitId; }
//    public void setCircuitId(Long circuitId) { this.circuitId = circuitId; }

    public String getCircuitName() { return circuitName; }
    public void setCircuitName(String circuitName) { this.circuitName = circuitName; }

    public String getWhenCondition() { return whenCondition; }
    public void setWhenCondition(String w) { this.whenCondition = w; }

    public String getLinesCsv() { return linesCsv; }
    public void setLinesCsv(String l) { this.linesCsv = l; }

    public String getStroke() { return stroke; }
    public void setStroke(String s) { this.stroke = s; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer p) { this.priority = p; }
}