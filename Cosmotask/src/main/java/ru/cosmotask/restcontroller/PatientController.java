package ru.cosmotask.restcontroller;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cosmotask.model.Patient;
import ru.cosmotask.service.PatientService;

@RestController
@RequestMapping("api/v1/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "Get Patient.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Patient got successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "patient-controller-getPatient")
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") String id) {
        Patient patientById = patientService.findById(id);
        return ResponseEntity.ok(patientById);
    }

    @Operation(summary = "Add Patient.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Patient added successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "patient-controller-addPatient")
    @PostMapping()
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.save(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @Operation(summary = "Change data of Patient.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Patient updated successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "patient-controller-putPatient")
    @PutMapping()
    public ResponseEntity<Patient> putPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.save(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @Operation(summary = "Change some data of Patient.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Patient updated successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "patient-controller-patchPatient")
    @PatchMapping()
    public ResponseEntity<Patient> patchPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.save(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @Operation(summary = "Delete Patient.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Patient deleted successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "patient-controller-deletePatient")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatientById(@PathVariable("id") String id) {
        String patientId = patientService.deletePatientById(id);
        if (patientId == null) {
            return ResponseEntity.badRequest().body("The patient has some completed procedures or patient does not exist and cannot be remove");
        }
        return ResponseEntity.ok(patientId);
    }
}
