package ru.cosmotask.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import ru.cosmotask.model.Procedure;

@Component
public interface ProcedureRepository extends MongoRepository<Procedure, String> {
}
