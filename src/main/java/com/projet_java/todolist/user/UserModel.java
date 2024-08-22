package com.projet_java.todolist.user;

import java.util.UUID;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

// @Data : crée les getters et setters
@Data
// Permet de définir que ce model correspond à otre table dans la bdd
@Entity(name="tb_users")
public class UserModel {
    // Définition d'une clé primaire
    @Id
    // Génération auto des ids
    @GeneratedValue(generator = "UUID")
    private UUID id;
    
    // Contrainte pour que le username soit une valeur unique
    @Column(unique = true)
    private String username;
    private String name;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
