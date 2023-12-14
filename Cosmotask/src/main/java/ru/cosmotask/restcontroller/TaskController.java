package ru.cosmotask.restcontroller;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Add Task.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Task added successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "task-controller-addTask")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Task> addTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskService.saveTask(taskDTO);
        return ResponseEntity.ofNullable(task);
    }

    @Operation(summary = "Get all Tasks.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "All Tasks got successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "task-controller-getAllTask")
    @GetMapping()
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            return ResponseEntity.badRequest().body(tasks);
        }
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get Task.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Task got successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "task-controller-getTaskByID")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") String taskId) {
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.ofNullable(task);
    }

    @Operation(summary = "Delete Task.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Task deleted successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "task-controller-deleteTaskById")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable("id") String taskId) {
        String result = taskService.deleteTaskById(taskId);
        return ResponseEntity.ofNullable(result);
    }

    @Operation(summary = "Update Task.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Task updated successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @Timed(value = "task-controller-updateTask")
    @PatchMapping()
    public ResponseEntity<Task> updateTaskById(@RequestBody TaskDTO taskDTO) {
        Task updatedTask = taskService.updateTask(taskDTO);
        return ResponseEntity.ofNullable(updatedTask);
    }
}
