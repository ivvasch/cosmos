package ru.cosmotask.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import ru.cosmotask.model.Task;

@Component
public interface TaskRepository extends MongoRepository<Task, String> {
}
