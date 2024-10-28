package tn.esprit.spring.dto;

import java.time.LocalDate;
import java.util.Set;

public record InstructorDTO(
        Long numInstructor,
        String firstName,
        String lastName,
        LocalDate dateOfHire,
        Set<Long> courseIds // Storing course IDs only for simplicity
) {}


