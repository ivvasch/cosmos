package ru.cosmotask.service;

import org.springframework.stereotype.Service;
import ru.cosmotask.model.CompletedProcedure;
import ru.cosmotask.model.Patient;
import ru.cosmotask.repository.CompletedProcedureRepository;
import ru.cosmotask.repository.PatientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final CompletedProcedureRepository completedProcedureRepository;

    public PatientServiceImpl(PatientRepository patientRepository,
                              CompletedProcedureRepository completedProcedureRepository) {
        this.patientRepository = patientRepository;
        this.completedProcedureRepository = completedProcedureRepository;
    }

    @Override
    public Patient findById(String id) {
        Optional<Patient> patientById = patientRepository.findById(id);
        return patientById.orElse(null);
    }

    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public String deletePatientById(String id) {
        Patient patientById = patientRepository.findById(id).orElse(null);
        List<CompletedProcedure> allByPatientId;
        if (patientById != null) {
            String patientId = patientById.getId();
            allByPatientId = completedProcedureRepository.findAllByPatientId(patientId);
            if (allByPatientId.isEmpty()) {
                patientRepository.delete(patientById);
                return patientId;
            }
        }
        return  null;
    }
}
