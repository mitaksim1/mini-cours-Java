package com.projet_java.todolist.task.filter;

import java.io.IOException;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.projet_java.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// En metant @Componenet on dit à Java qui l'on veut qu'il s'occupe de cette classe
@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Vérification de la route sur laquelle appliquer la vérification
        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks/")) {
            // 1. Récupérer l'authentification de l'utilisateur
            var authorization = request.getHeader("Authorization");
            
            var authEncoded = authorization.substring("Basic".length()).trim();
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);
            var authString = new String(authDecode);
            // Rappel : split crée un array en séparant les élements avant et après les ":" comme des valeurs de cet array
            // ["mon_username", "1234"]
            String [] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];
            
            // 2. Validation de l'utilisateur
            var user = this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401);
            } else {
                // Valider le mot-de-passe
                // verify() prend comme arguments le mot-de-passe reçu par l'application et le mot-de-passe avec lequel on veut le comparer (celui de la bdd)
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if(passwordVerify.verified) {
                    // setAttribute() prend deux arguments : le nom de l'attribut, et la valeur que l'on souhaite lui passer
                    request.setAttribute("idUser", user.getId());
                    // Go ahead
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
        
    }   
}
