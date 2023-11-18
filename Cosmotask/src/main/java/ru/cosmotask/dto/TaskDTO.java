package ru.cosmotask.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private String id;
    private String patientId;
    private String procedureId;
    private LocalDateTime startDate;
    private boolean invited;

}
