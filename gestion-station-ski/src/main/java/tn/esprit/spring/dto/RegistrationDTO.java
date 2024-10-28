package tn.esprit.spring.dto;

public record RegistrationDTO(
        Long numRegistration,
        int numWeek,
        Long skierId, // Skier ID reference
        Long courseId // Course ID reference
) {}

