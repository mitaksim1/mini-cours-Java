package com.projet_java.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class HandleNullProperties {
    // Récupére les données des propriétés qui ne sont pas null
    public static void copyNotNullProperties(Object source, Object target) {
        // Copie les données de toutes les propriétés contenues dans le premier paramètre (source) dans le deuxième paramètre (target) 
        // en ignorant les propriétés qui ont une valeur null troisième paramètre (récupérés grâce à la méthode ci-dessous)
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));    
    }

    // Récupère les propriétés de l'objet qui sont null
    public static String[] getNullPropertyNames(Object source) {
        // BeanWrapper : interface qui nous permet d'accéder aux propriétés d'un objet
        final BeanWrapper src = new BeanWrapperImpl(source);

        // Récupération des propriétés 
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        // emptyNames retourne une collection avec les propriétés dont les valeurs sont nulles
        Set<String> emptyNames = new HashSet<>();

        // Façon de faire une boucle en Java, on donne un nom pour chaque item => pd et après la virgule on met le tableau à boucleer => pds
        for(PropertyDescriptor pd: pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if(srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        // Pour pouvoir travailler avec les valeurs nulles, on doit les convertir de collection à array
        String[] result = new String[emptyNames.size()];

        return emptyNames.toArray(result);
    }
}
