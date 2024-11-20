package com.learnreactiveprogramming.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionalExample {

    public static void main(String[] args) {
        var namesList =  List.of("alex", "ben", "chloe","adam", "adam");
        var newNamesList = namesGreaterThanSize(namesList, 3);
        System.out.println("new names list : " + newNamesList);
    }

    private static List<String> namesGreaterThanSize(List<String> namesList, int size) {
        return namesList
                .parallelStream()
                .filter(name -> name.length() > size)
                .map(String::toUpperCase)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

}
