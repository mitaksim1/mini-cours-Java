package com.projet_java.todolist.task;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projet_java.todolist.utils.HandleNullProperties;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    // Autowired : permet d'instancier la classe auto
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        // CurrentDate doit être < StartDate or EndDate
        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date or end date must be greater than the current date");
        }

        // EndDate doit être > startDate
        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End date must be greater than the start date");
        }

        var task = this.taskRepository.save(taskModel);

        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);

        return tasks;
    }

    // Task update : http://localhost:8080/tasks/8925682-cje82
    @PutMapping("/{id}")
    // @PathVariable UUID id : Pour savoir sur quelle tâche faire la modification, nous aurons besoin de l'id de cette tâche
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        // Récupère une tâche existante
        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found");
        }

        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't have permission to modify this task");
        }

        // Rappel : on peut appeler une méthode d'une classe static sans l'instancier avant
        // taskModel = source, task = target
        HandleNullProperties.copyNotNullProperties(taskModel, task);

        var taskUpdated = this.taskRepository.save(task);
    
        return ResponseEntity.ok().body(taskUpdated);
    }
}
