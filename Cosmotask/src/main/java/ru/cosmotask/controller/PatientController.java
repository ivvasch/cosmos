package ru.cosmotask.controller;

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

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") String id) {
        Patient patientById = patientService.findById(id);
        return ResponseEntity.ok(patientById);
    }

    @PostMapping()
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.save(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @PutMapping()
    public ResponseEntity<Patient> putPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.save(patient);
        return ResponseEntity.ok(savedPatient);
    }
    @PatchMapping()
    public ResponseEntity<Patient> patchPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.save(patient);
        return ResponseEntity.ok(savedPatient);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatientById(@PathVariable("id") String id) {
        String patientId = patientService.deletePatientById(id);
        if (patientId == null) {
            return ResponseEntity.badRequest().body("The patient has some completed procedures or patient does not exist and cannot be remove");
        }
        return ResponseEntity.ok(patientId);
    }
}
