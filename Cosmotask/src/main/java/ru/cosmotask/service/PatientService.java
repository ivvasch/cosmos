package ru.cosmotask.service;

import ru.cosmotask.model.Patient;

public interface PatientService {
    Patient findById(String id);

    Patient save(Patient patient);

    String deletePatientById(String id);

}
