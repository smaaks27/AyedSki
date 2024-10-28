package tn.esprit.spring.dto;

import java.time.LocalDate;
import java.util.Set;

public record SkierDTO(
        Long numSkier,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String city,
        Long subscriptionId, // Subscription ID reference
        Set<Long> pisteIds, // Piste IDs only for simplicity
        Set<Long> registrationIds // Registration IDs for this skier
) {}


