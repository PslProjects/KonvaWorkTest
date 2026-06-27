package com.scada.controller;

import com.scada.entities.LineRule;
import com.scada.repository.LineRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rules")
//@CrossOrigin(origins = "*")
public class LineRuleController {

    @Autowired private LineRuleRepository repo;

//    @GetMapping("/circuit/{circuitId}")
//    public List<LineRule> byCircuit(@PathVariable Long circuitId) {
//        return repo.findByCircuitId(circuitId);
//    }

    // <-- NAYA: circuit ke NAAM se rules
    @GetMapping("/by-name/{name}")
    public List<LineRule> byName(@PathVariable String name) {
        return repo.findByCircuitName(name);
    }

    @PostMapping
    public LineRule create(@RequestBody LineRule rule) {
        return repo.save(rule);
    }

    @PutMapping("/{id}")
    public LineRule update(@PathVariable Long id, @RequestBody LineRule rule) {
        rule.setId(id);
        return repo.save(rule);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }

    @DeleteMapping("/by-name/{name}")
    public void deleteByName(@PathVariable String name) {
        repo.deleteAll(repo.findByCircuitName(name));
    }
}