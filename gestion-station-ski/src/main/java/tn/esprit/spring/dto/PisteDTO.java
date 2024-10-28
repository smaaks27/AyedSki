package tn.esprit.spring.dto;

import tn.esprit.spring.entities.Color;

import java.util.Set;

public record PisteDTO(
        Long numPiste,
        String namePiste,
        Color color,
        int length,
        int slope,
        Set<Long> skierIds // Storing skier IDs only for simplicity
) {}


