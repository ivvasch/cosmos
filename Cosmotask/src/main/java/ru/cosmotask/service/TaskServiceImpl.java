package ru.cosmotask.service;

import com.google.api.services.calendar.model.Event;
import org.springframework.stereotype.Service;
import ru.cosmotask.calendar.model.CalendarEvent;
import ru.cosmotask.calendar.service.CalendarEventService;
import ru.cosmotask.dto.TaskDTO;
import ru.cosmotask.model.Patient;
import ru.cosmotask.model.Procedure;
import ru.cosmotask.model.Task;
import ru.cosmotask.repository.PatientRepository;
import ru.cosmotask.repository.ProcedureRepository;
import ru.cosmotask.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final PatientRepository patientRepository;
    private final ProcedureRepository procedureRepository;
    private final CalendarEventService calendarEventService;

    public TaskServiceImpl(TaskRepository taskRepository, PatientRepository patientRepository, ProcedureRepository procedureRepository, CalendarEventService calendarEventService) {
        this.taskRepository = taskRepository;
        this.patientRepository = patientRepository;
        this.procedureRepository = procedureRepository;
        this.calendarEventService = calendarEventService;
    }


    @Override
    public Task saveTask(TaskDTO taskDTO) {
        Task task = convertDtoToTask(taskDTO);
        if (task == null) {
            return null;
        }
        Task saved = taskRepository.save(task);

        LocalDateTime endDate = saved.getStartDate().plusHours(1);
        String summary = getSummaryForEvent(saved);
        Event event = new Event()
                .setSummary(summary)
                .setColorId("2");
        CalendarEvent calendarEvent = CalendarEvent.builder()
                .event(event)
                .endDate(endDate)
                .task(saved)
                .build();
        calendarEventService.saveEventToCalendar(calendarEvent);

        return saved;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(String taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    @Override
    public String deleteTaskById(String taskId) {
        String result = String.format("Task with id %s was successfully delete.", taskId);
        Optional<Task> taskById = taskRepository.findById(taskId);
        taskById.ifPresent(taskRepository::delete);
        Event eventByTaskId = calendarEventService.getEventByTaskId(taskById.orElse(null));
        calendarEventService.deleteEvent(eventByTaskId);
        return result;
    }

    @Override
    public Task updateTask(TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskDTO.getId()).orElse(null);
        Task saved = null;
        if (task != null) {
            task.setInvited(taskDTO.isInvited());
             saved = taskRepository.save(task);
        }
        return saved;
    }

    private String getSummaryForEvent(Task saved) {
        StringBuilder summaryBuilder = new StringBuilder();
        Patient patient = patientRepository.findById(saved.getPatient().getId()).orElse(null);
        Procedure procedure = procedureRepository.findById(saved.getProcedure().getId()).orElse(null);
        if (patient != null && procedure != null) {
            summaryBuilder
                    .append("Пациент: ")
                    .append(patient.getFirstName())
                    .append(" ").append(patient.getMiddleName())
                    .append(" ").append(patient.getPhone()).append(" ")
                    .append("Записан на процедуру: ")
                    .append(procedure.getType());
        } else {
            summaryBuilder.append("Пациент или процедура не существует");
        }
        return summaryBuilder.toString();
    }

    private Task convertDtoToTask(TaskDTO taskDTO) {
        Patient patient = patientRepository.findById(taskDTO.getPatientId()).orElse(null);
        Procedure procedure = procedureRepository.findById(taskDTO.getProcedureId()).orElse(null);
        if (patient == null || procedure == null) {
            return null;
        }
        return Task.builder()
                .id(taskDTO.getId())
                .patient(patient)
                .procedure(procedure)
                .startDate(taskDTO.getStartDate())
                .invited(false)
                .build();
    }
}
