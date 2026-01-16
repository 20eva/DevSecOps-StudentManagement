package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment1;
    private Enrollment enrollment2;

    @BeforeEach
    void setUp() {
        enrollment1 = new Enrollment();
        enrollment1.setId(1L);
        enrollment1.setGrade("85.5");
        
        enrollment2 = new Enrollment();
        enrollment2.setId(2L);
        enrollment2.setGrade("90.0");
    }

    @Test
    void testGetAllEnrollments() {
        // Given
        List<Enrollment> expectedEnrollments = Arrays.asList(enrollment1, enrollment2);
        when(enrollmentRepository.findAll()).thenReturn(expectedEnrollments);

        // When
        List<Enrollment> actualEnrollments = enrollmentService.getAllEnrollments();

        // Then
        assertNotNull(actualEnrollments);
        assertEquals(2, actualEnrollments.size());
        assertEquals(expectedEnrollments, actualEnrollments);
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllEnrollments_EmptyList() {
        // Given
        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Enrollment> actualEnrollments = enrollmentService.getAllEnrollments();

        // Then
        assertNotNull(actualEnrollments);
        assertTrue(actualEnrollments.isEmpty());
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetEnrollmentById_Success() {
        // Given
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment1));

        // When
        Enrollment actualEnrollment = enrollmentService.getEnrollmentById(1L);

        // Then
        assertNotNull(actualEnrollment);
        assertEquals(1L, actualEnrollment.getId());
        assertEquals("85.5", actualEnrollment.getGrade());
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEnrollmentById_NotFound() {
        // Given
        when(enrollmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(Exception.class, () -> {
            enrollmentService.getEnrollmentById(999L);
        });
        verify(enrollmentRepository, times(1)).findById(999L);
    }

    @Test
    void testSaveEnrollment_Create() {
        // Given
        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setGrade("88.0");
        
        Enrollment savedEnrollment = new Enrollment();
        savedEnrollment.setId(3L);
        savedEnrollment.setGrade("88.0");
        
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(savedEnrollment);

        // When
        Enrollment result = enrollmentService.saveEnrollment(newEnrollment);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(3L, result.getId());
        assertEquals("88.0", result.getGrade());
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void testSaveEnrollment_Update() {
        // Given
        enrollment1.setGrade("95.0");
        when(enrollmentRepository.save(enrollment1)).thenReturn(enrollment1);

        // When
        Enrollment result = enrollmentService.saveEnrollment(enrollment1);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("95.0", result.getGrade());
        verify(enrollmentRepository, times(1)).save(enrollment1);
    }

    @Test
    void testDeleteEnrollment_Success() {
        // Given
        doNothing().when(enrollmentRepository).deleteById(1L);

        // When
        enrollmentService.deleteEnrollment(1L);

        // Then
        verify(enrollmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEnrollment_NonExistentId() {
        // Given
        doNothing().when(enrollmentRepository).deleteById(anyLong());

        // When
        enrollmentService.deleteEnrollment(999L);

        // Then
        verify(enrollmentRepository, times(1)).deleteById(999L);
        // Note: deleteById doesn't throw exception if ID doesn't exist
    }

    @Test
    void testSaveEnrollment_NullEnrollment() {
        // Given
        when(enrollmentRepository.save(null)).thenThrow(new IllegalArgumentException("Enrollment cannot be null"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            enrollmentService.saveEnrollment(null);
        });
    }

    @Test
    void testGetEnrollmentById_NullId() {
        // Given
        when(enrollmentRepository.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            enrollmentService.getEnrollmentById(null);
        });
    }
}