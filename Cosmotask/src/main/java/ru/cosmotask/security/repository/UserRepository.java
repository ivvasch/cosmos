package ru.cosmotask.security.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import ru.cosmotask.security.model.User;

import java.util.Optional;

@Component
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
}
