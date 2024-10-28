package tn.esprit.spring.dto;

import tn.esprit.spring.entities.TypeSubscription;

import java.time.LocalDate;

public record SubscriptionDTO(
        Long numSub,
        LocalDate startDate,
        LocalDate endDate,
        Float price,
        TypeSubscription typeSub
) {}

