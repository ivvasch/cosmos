package ru.cosmotask.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import ru.cosmotask.model.Patient;

import java.util.Optional;

@Component
public interface PatientRepository extends MongoRepository<Patient, String> {

    Optional<Patient> findById(String patientId);

}
