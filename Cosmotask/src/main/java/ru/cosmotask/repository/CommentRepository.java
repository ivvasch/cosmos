package ru.cosmotask.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import ru.cosmotask.model.Comment;

@Component
public interface CommentRepository extends MongoRepository<Comment, String> {

}
