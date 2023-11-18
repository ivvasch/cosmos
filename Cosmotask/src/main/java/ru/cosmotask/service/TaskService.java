package ru.cosmotask.service;

import ru.cosmotask.dto.TaskDTO;
import ru.cosmotask.model.Task;

import java.util.List;

public interface TaskService {
    Task saveTask(TaskDTO taskDTO);

    List<Task> getAllTasks();

    Task getTaskById(String taskId);

    String deleteTaskById(String taskId);

    Task updateTask(TaskDTO taskDTO);
}
