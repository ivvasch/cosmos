package ru.cosmotask.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompletedProcedureDTO {
    private String id;
    private String patientId;
    private String procedureId;
    private Float cost;
    private LocalDate nextDate;
    private boolean necessaryNextProcedure;
    private String comment;
    private boolean done;

}
