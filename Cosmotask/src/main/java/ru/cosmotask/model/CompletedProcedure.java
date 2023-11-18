package ru.cosmotask.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@Document(collection = "t_completed_procedure")
public class CompletedProcedure {
    @Id
    private String id;
    @DBRef
    private Patient patient;
    private Procedure procedure;
    @NotBlank
    private Float cost;
    @NotBlank
    private LocalDate date;
    private LocalDate nextDate;
    private boolean necessaryNextProcedure;
    @DBRef
    private List<Comment> comments;
    private boolean done; // по умолчанию false
}
