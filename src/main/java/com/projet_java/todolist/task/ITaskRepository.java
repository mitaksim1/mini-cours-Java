package com.projet_java.todolist.task;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    // Récupère la liste des tâches pour un user donné 
    List<TaskModel>findByIdUser(UUID idUser);
}
