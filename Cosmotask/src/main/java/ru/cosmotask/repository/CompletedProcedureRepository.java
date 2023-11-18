package ru.cosmotask.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import ru.cosmotask.model.CompletedProcedure;

import java.util.List;

@Component
public interface CompletedProcedureRepository extends MongoRepository<CompletedProcedure, String> {

    @Query("{'patient.$id': ObjectId(?0)}")
    List<CompletedProcedure> findAllByPatientId(String id);

    @Query("{'procedure.$id': ObjectId(?0)}")
    List<CompletedProcedure> findAllByProcedureId(String id);
}
