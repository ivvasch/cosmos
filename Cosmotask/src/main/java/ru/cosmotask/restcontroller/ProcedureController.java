package ru.cosmotask.restcontroller;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Add Procedure.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Procedure added successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "procedure-controller-addProcedure")
    @PostMapping("procedure")
    public ResponseEntity<String> addProcedure(@RequestBody ProcedureDTO procedureDTO) {
        String savedProcedureId = procedureService.saveProcedure(procedureDTO);
        return ResponseEntity.ok(savedProcedureId);
    }

    @Operation(summary = "Get Procedure.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Procedure got successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "procedure-controller-getProcedure")
    @GetMapping("procedure/{id}")
    public ResponseEntity<Procedure> getProcedure(@PathVariable String id) {
        Procedure procedureById = procedureService.getProcedureById(id);
        if (procedureById == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(procedureById);
    }

    @Operation(summary = "Update Procedure.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Procedure updates successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "procedure-controller-putProcedure")
    @PutMapping("procedure")
    public ResponseEntity<String> updateProcedure(@RequestBody ProcedureDTO procedureDTO) {
        String updatedProcedureId = procedureService.saveProcedure(procedureDTO);
        return ResponseEntity.ok(updatedProcedureId);
    }

    @Operation(summary = "Delete Procedure.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Procedure deleted successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "procedure-controller-deleteProcedure")
    @DeleteMapping("procedure/{id}")
    public ResponseEntity<String> deleteProcedure(@PathVariable String id) {
        String deleted = procedureService.deleteProcedure(id);
        if (deleted == null) {
            return ResponseEntity.badRequest().body("The procedure already in use with some patient and cannot be delete");
        }
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "Add CompletedProcedure.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "CompletedProcedure added successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "procedure-controller-addCompletedProcedure")
    @PostMapping("completed-procedure")
    public ResponseEntity<String> addCompletedProcedure(@RequestBody CompletedProcedureDTO completedProcedureDTO) {
        String savedId = procedureService.saveCompletedProcedure(completedProcedureDTO);
        if (savedId == null) {
            return ResponseEntity.badRequest().body("Procedure or patient not found, completed procedure cannot be save.");
        }
        return ResponseEntity.ok(savedId);
    }

    @Operation(summary = "Get CompletedProcedure.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "CompletedProcedure got successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "procedure-controller-getCompletedProcedure")
    @GetMapping("completed-procedure/{id}")
    public ResponseEntity<CompletedProcedure> getCompletedProcedureById(@PathVariable("id") String id) {
        CompletedProcedure completedProcedureById = procedureService.getCompletedProcedureById(id);
        if (completedProcedureById == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(completedProcedureById);
    }

    @Operation(summary = "Update CompletedProcedure.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "CompletedProcedure updated successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "procedure-controller-updateCompletedProcedure")
    @PatchMapping("completed-procedure")
    public ResponseEntity<String> updateCompletedProcedureById(@RequestBody CompletedProcedure completedProcedure) {
        String updatedId = procedureService.updateCompletedProcedure(completedProcedure);
        return ResponseEntity.ok(updatedId);
    }

    @Operation(summary = "Get all CompletedProcedure by Patient.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "All CompletedProcedures got successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "procedure-controller-getAllCompletedProcedureByPatient")
    @GetMapping("completed-procedure/patient/{id}")
    public ResponseEntity<List<CompletedProcedure>> getAllCompletedProcedureByPatientId(@PathVariable("id") String patientId) {
        List<CompletedProcedure> allCompletedProcedureByPatientId = procedureService.getAllCompletedProcedureByPatientId(patientId);
        return ResponseEntity.ok(allCompletedProcedureByPatientId);
    }

    @Operation(summary = "Delete CompletedProcedure.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "CompletedProcedure sent request for confirm of delete.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "procedure-controller-deleteCompletedProcedure")
    @DeleteMapping("completed-procedure/{id}")
    public ResponseEntity<String> deleteCompletedProcedure(@PathVariable String id) {
        String result = procedureService.checkCompletedProcedureForDelete(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Send confirm for delete CompletedProcedure.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "CompletedProcedure deleted successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "procedure-controller-deleteCompletedProcedureWithConfirm")
    @DeleteMapping("completed-procedure/{id}/{confirm}")
    public ResponseEntity<String> deleteCompletedProcedure(@PathVariable("id") String id, @PathVariable("confirm") String confirm) {
        if (StringUtils.containsAny(confirm, "yes", "y", "yea")) {
            String result = procedureService.deleteCompletedProcedure(id);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body("Something wrong when we want to delete completed procedure.");
    }

}
