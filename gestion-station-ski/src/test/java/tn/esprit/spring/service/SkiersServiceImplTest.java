package tn.esprit.spring.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.SkierServicesImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class SkierServicesImplTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SkierServicesImpl skierServices;

    @Test
    void testRetrieveAllSkiers() {
        // Arrange
        List<Skier> skiers = List.of(new Skier(), new Skier());
        when(skierRepository.findAll()).thenReturn(skiers);

        // Act
        List<Skier> result = skierServices.retrieveAllSkiers();

        // Assert
        assertEquals(2, result.size());
        verify(skierRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveAllSkiersEmpty() {
        // Arrange
        when(skierRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Skier> result = skierServices.retrieveAllSkiers();

        // Assert
        assertTrue(result.isEmpty());
        verify(skierRepository, times(1)).findAll();
    }

    @Test
    void testAddSkierWithAnnualSubscription() {
        // Arrange
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.now());
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        LocalDate expectedEndDate = LocalDate.now().plusYears(1);

        Skier skier = new Skier();
        skier.setSubscription(subscription);

        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // Act
        Skier result = skierServices.addSkier(skier);

        // Assert
        assertNotNull(result);
        assertEquals(expectedEndDate, result.getSubscription().getEndDate());
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testAssignSkierToSubscriptionNotFound() {
        // Arrange
        when(skierRepository.findById(1L)).thenReturn(Optional.empty());
        when(subscriptionRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        Skier result = skierServices.assignSkierToSubscription(1L, 2L);

        // Assert
        assertNull(result);
        verify(skierRepository, times(1)).findById(1L);
        verify(subscriptionRepository, times(1)).findById(2L);
    }

    @Test
    void testAddSkierAndAssignToCourseNotFound() {
        // Arrange
        Skier skier = new Skier();
        skier.setRegistrations(new HashSet<>());

        when(courseRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Skier result = skierServices.addSkierAndAssignToCourse(skier, 3L);

        // Assert
        assertNull(result);
        verify(courseRepository, times(1)).findById(3L);
    }

    @Test
    void testRetrieveSkierNotFound() {
        // Arrange
        when(skierRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Skier result = skierServices.retrieveSkier(1L);

        // Assert
        assertNull(result);
        verify(skierRepository, times(1)).findById(1L);
    }

    @Test
    void testAssignSkierToPisteNotFound() {
        // Arrange
        when(skierRepository.findById(1L)).thenReturn(Optional.empty());
        when(pisteRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        Skier result = skierServices.assignSkierToPiste(1L, 2L);

        // Assert
        assertNull(result);
        verify(skierRepository, times(1)).findById(1L);
        verify(pisteRepository, times(1)).findById(2L);
    }

    @Test
    void testRetrieveSkiersBySubscriptionTypeEmpty() {
        // Arrange
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL)).thenReturn(Collections.emptyList());

        // Act
        List<Skier> result = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);

        // Assert
        assertTrue(result.isEmpty());
        verify(skierRepository, times(1)).findBySubscription_TypeSub(TypeSubscription.ANNUAL);
    }
}
