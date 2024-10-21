package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class SkierServicesImpl implements ISkierServices {

    private final ISkierRepository skierRepository;
    private final IPisteRepository pisteRepository;
    private final ICourseRepository courseRepository;
    private final IRegistrationRepository registrationRepository;
    private final ISubscriptionRepository subscriptionRepository;

    @Override
    public List<Skier> retrieveAllSkiers() {
        log.info("Retrieving all skiers");
        return skierRepository.findAll();
    }

    @Override
    public Skier addSkier(Skier skier) {
        log.info("Adding new skier: {}", skier);

        // Ensure subscription and start date are not null
        if (skier.getSubscription() != null && skier.getSubscription().getStartDate() != null) {
            switch (skier.getSubscription().getTypeSub()) {
                case ANNUAL:
                    skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusYears(1));
                    break;
                case SEMESTRIEL:
                    skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(6));
                    break;
                case MONTHLY:
                    skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(1));
                    break;
                default:
                    log.warn("Unknown subscription type: {}", skier.getSubscription().getTypeSub());
            }
        }
        return skierRepository.save(skier);
    }

    @Override
    public Skier assignSkierToSubscription(Long numSkier, Long numSubscription) {
        log.info("Assigning skier with ID {} to subscription {}", numSkier, numSubscription);

        Optional<Skier> skierOpt = skierRepository.findById(numSkier);
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(numSubscription);

        if (skierOpt.isPresent() && subscriptionOpt.isPresent()) {
            Skier skier = skierOpt.get();
            skier.setSubscription(subscriptionOpt.get());
            return skierRepository.save(skier);
        } else {
            log.warn("Skier or Subscription not found for IDs {}, {}", numSkier, numSubscription);
            return null; // Handle this based on your needs (throw exception or return null)
        }
    }

    @Override
    public Skier addSkierAndAssignToCourse(Skier skier, Long numCourse) {
        log.info("Adding skier and assigning to course {}", numCourse);

        Skier savedSkier = skierRepository.save(skier);
        Course course = courseRepository.findById(numCourse).orElse(null);

        if (course != null) {
            Set<Registration> registrations = savedSkier.getRegistrations();
            if (registrations != null) {
                registrations.forEach(r -> {
                    r.setSkier(savedSkier);
                    r.setCourse(course);
                    registrationRepository.save(r);
                });
            }
        } else {
            log.warn("Course not found for ID {}", numCourse);
        }

        return savedSkier;
    }

    @Override
    public void removeSkier(Long numSkier) {
        log.info("Removing skier with ID {}", numSkier);
        skierRepository.deleteById(numSkier);
    }

    @Override
    public Skier retrieveSkier(Long numSkier) {
        log.info("Retrieving skier with ID {}", numSkier);
        return skierRepository.findById(numSkier).orElse(null);
    }

    @Override
    public Skier assignSkierToPiste(Long numSkieur, Long numPiste) {
        log.info("Assigning skier {} to piste {}", numSkieur, numPiste);

        Optional<Skier> skierOpt = skierRepository.findById(numSkieur);
        Optional<Piste> pisteOpt = pisteRepository.findById(numPiste);

        if (skierOpt.isPresent() && pisteOpt.isPresent()) {
            Skier skier = skierOpt.get();
            Piste piste = pisteOpt.get();

            if (skier.getPistes() == null) {
                skier.setPistes(new HashSet<>());
            }

            skier.getPistes().add(piste);
            return skierRepository.save(skier);
        } else {
            log.warn("Skier or Piste not found for IDs {}, {}", numSkieur, numPiste);
            return null; // Handle error cases appropriately
        }
    }

    @Override
    public List<Skier> retrieveSkiersBySubscriptionType(TypeSubscription typeSubscription) {
        log.info("Retrieving skiers by subscription type: {}", typeSubscription);
        return skierRepository.findBySubscription_TypeSub(typeSubscription);
    }
}
