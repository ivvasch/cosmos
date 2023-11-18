package ru.cosmotask.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cosmotask.dto.TaskDTO;
import ru.cosmotask.model.Task;
import ru.cosmotask.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Task> addTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskService.saveTask(taskDTO);
        return ResponseEntity.ofNullable(task);
    }
    @GetMapping()
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            return ResponseEntity.badRequest().body(tasks);
        }
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") String taskId) {
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.ofNullable(task);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable("id") String taskId) {
        String result = taskService.deleteTaskById(taskId);
        return ResponseEntity.ofNullable(result);
    }
    @PatchMapping()
    public ResponseEntity<Task> updateTaskById(@RequestBody TaskDTO taskDTO) {
        Task updatedTask = taskService.updateTask(taskDTO);
        return ResponseEntity.ofNullable(updatedTask);
    }
}
