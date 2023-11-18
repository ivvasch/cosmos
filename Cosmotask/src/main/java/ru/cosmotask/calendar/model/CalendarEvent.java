package ru.cosmotask.calendar.model;

import com.google.api.services.calendar.model.Event;
import lombok.Builder;
import lombok.Data;
import ru.cosmotask.model.Task;

import java.time.LocalDateTime;

@Data
@Builder
public class CalendarEvent {
    private Task task;
    private Event event;
    private LocalDateTime endDate;
}
