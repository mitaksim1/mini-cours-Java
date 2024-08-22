package com.projet_java.todolist.user;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

// Interface : modèle/contrat, nous avons les méthodes, mais pas son impémentation qui sera faite après par une autre classe 
public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByUsername(String username);
}
