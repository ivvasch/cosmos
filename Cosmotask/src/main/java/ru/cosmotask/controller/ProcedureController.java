package ru.cosmotask.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cosmotask.dto.CompletedProcedureDTO;
import ru.cosmotask.dto.ProcedureDTO;
import ru.cosmotask.model.CompletedProcedure;
import ru.cosmotask.model.Procedure;
import ru.cosmotask.service.ProcedureService;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class ProcedureController {

    private final ProcedureService procedureService;

    public ProcedureController(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }

    @PostMapping("procedure")
    public ResponseEntity<String> addProcedure(@RequestBody ProcedureDTO procedureDTO) {
        String savedProcedureId = procedureService.saveProcedure(procedureDTO);
        return ResponseEntity.ok(savedProcedureId);
    }

    @GetMapping("procedure/{id}")
    public ResponseEntity<Procedure> getProcedure(@PathVariable String id) {
        Procedure procedureById = procedureService.getProcedureById(id);
        if (procedureById == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(procedureById);
    }

    @PutMapping("procedure")
    public ResponseEntity<String> updateProcedure(@RequestBody ProcedureDTO procedureDTO) {
        String updatedProcedureId = procedureService.saveProcedure(procedureDTO);
        return ResponseEntity.ok(updatedProcedureId);
    }

    @DeleteMapping("procedure/{id}")
    public ResponseEntity<String> deleteProcedure(@PathVariable String id) {
        String deleted = procedureService.deleteProcedure(id);
        if (deleted == null) {
            return ResponseEntity.badRequest().body("The procedure already in use with some patient and cannot be delete");
        }
        return ResponseEntity.ok(id);
    }

    @PostMapping("completed-procedure")
    public ResponseEntity<String> addCompletedProcedure(@RequestBody CompletedProcedureDTO completedProcedureDTO) {
        String savedId = procedureService.saveCompletedProcedure(completedProcedureDTO);
        if (savedId == null) {
            return ResponseEntity.badRequest().body("Procedure or patient not found, completed procedure cannot be save.");
        }
        return ResponseEntity.ok(savedId);
    }

    @GetMapping("completed-procedure/{id}")
    public ResponseEntity<CompletedProcedure> getCompletedProcedureById(@PathVariable("id") String id) {
        CompletedProcedure completedProcedureById = procedureService.getCompletedProcedureById(id);
        if (completedProcedureById == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(completedProcedureById);
    }
    @PatchMapping("completed-procedure")
    public ResponseEntity<String> updateCompletedProcedureById(@RequestBody CompletedProcedure completedProcedure) {
        String updatedId = procedureService.updateCompletedProcedure(completedProcedure);
        return ResponseEntity.ok(updatedId);
    }

    @GetMapping("completed-procedure/patient/{id}")
    public ResponseEntity<List<CompletedProcedure>> getAllCompletedProcedureByPatientId(@PathVariable("id") String patientId) {
        List<CompletedProcedure> allCompletedProcedureByPatientId = procedureService.getAllCompletedProcedureByPatientId(patientId);
        return ResponseEntity.ok(allCompletedProcedureByPatientId);
    }

    @DeleteMapping("completed-procedure/{id}")
    public ResponseEntity<String> deleteCompletedProcedure(@PathVariable String id) {
        String result = procedureService.checkCompletedProcedureForDelete(id);
        return ResponseEntity.ok(result);
    }
    @DeleteMapping("completed-procedure/{id}/{confirm}")
    public ResponseEntity<String> deleteCompletedProcedure(@PathVariable("id") String id, @PathVariable("confirm") String confirm) {
        if (StringUtils.containsAny(confirm, "yes", "y", "yea")) {
            String result = procedureService.deleteCompletedProcedure(id);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body("Something wrong when we want to delete completed procedure.");
    }

}
