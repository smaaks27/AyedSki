package tn.esprit.spring.dto;

import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;

public record CourseDTO(
        Long numCourse,
        int level,
        TypeCourse typeCourse,
        Support support,
        Float price,
        int timeSlot
) {}

