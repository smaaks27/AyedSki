package tn.esprit.spring.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.SkierServicesImpl;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SkierServicesImplTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SkierServicesImpl skierServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
    void testAddSkierWithAnnualSubscription() {
        // Arrange
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.now());
        subscription.setTypeSub(TypeSubscription.ANNUAL);

        Skier skier = new Skier();
        skier.setSubscription(subscription);

        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // Act
        Skier result = skierServices.addSkier(skier);

        // Assert
        assertNotNull(result);
        assertEquals(LocalDate.now().plusYears(1), result.getSubscription().getEndDate());
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testAssignSkierToSubscription() {
        // Arrange
        Skier skier = new Skier();
        Subscription subscription = new Subscription();

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(2L)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // Act
        Skier result = skierServices.assignSkierToSubscription(1L, 2L);

        // Assert
        assertNotNull(result);
        assertEquals(subscription, result.getSubscription());
        verify(skierRepository, times(1)).findById(1L);
        verify(subscriptionRepository, times(1)).findById(2L);
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testAddSkierAndAssignToCourse() {
        // Arrange
        Skier skier = new Skier();
        Course course = new Course();
        Registration registration = new Registration();
        Set<Registration> registrations = new HashSet<>();
        registrations.add(registration);
        skier.setRegistrations(registrations);

        when(skierRepository.save(any(Skier.class))).thenReturn(skier);
        when(courseRepository.findById(3L)).thenReturn(Optional.of(course));

        // Act
        Skier result = skierServices.addSkierAndAssignToCourse(skier, 3L);

        // Assert
        assertNotNull(result);
        assertEquals(course, registration.getCourse());
        verify(skierRepository, times(1)).save(skier);
        verify(courseRepository, times(1)).findById(3L);
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testRemoveSkier() {
        // Act
        skierServices.removeSkier(1L);

        // Assert
        verify(skierRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrieveSkier() {
        // Arrange
        Skier skier = new Skier();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));

        // Act
        Skier result = skierServices.retrieveSkier(1L);

        // Assert
        assertNotNull(result);
        verify(skierRepository, times(1)).findById(1L);
    }

    @Test
    void testAssignSkierToPiste() {
        // Arrange
        Skier skier = new Skier();
        Piste piste = new Piste();

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(2L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // Act
        Skier result = skierServices.assignSkierToPiste(1L, 2L);

        // Assert
        assertNotNull(result);
        assertTrue(skier.getPistes().contains(piste));
        verify(skierRepository, times(1)).findById(1L);
        verify(pisteRepository, times(1)).findById(2L);
        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testRetrieveSkiersBySubscriptionType() {
        // Arrange
        List<Skier> skiers = List.of(new Skier(), new Skier());
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL)).thenReturn(skiers);

        // Act
        List<Skier> result = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(skierRepository, times(1)).findBySubscription_TypeSub(TypeSubscription.ANNUAL);
    }
}

