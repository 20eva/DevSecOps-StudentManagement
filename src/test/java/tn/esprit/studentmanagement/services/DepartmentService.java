package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department1;
    private Department department2;

    @BeforeEach
    void setUp() {
        department1 = new Department();
        department1.setIdDepartment(1L);
        department1.setName("Computer Science");
        
        department2 = new Department();
        department2.setIdDepartment(2L);
        department2.setName("Mathematics");
    }

    @Test
    void testGetAllDepartments() {
        // Given
        List<Department> expectedDepartments = Arrays.asList(department1, department2);
        when(departmentRepository.findAll()).thenReturn(expectedDepartments);

        // When
        List<Department> actualDepartments = departmentService.getAllDepartments();

        // Then
        assertNotNull(actualDepartments);
        assertEquals(2, actualDepartments.size());
        assertEquals(expectedDepartments, actualDepartments);
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllDepartments_EmptyList() {
        // Given
        when(departmentRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Department> actualDepartments = departmentService.getAllDepartments();

        // Then
        assertNotNull(actualDepartments);
        assertTrue(actualDepartments.isEmpty());
        assertEquals(0, actualDepartments.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testGetDepartmentById_Success() {
        // Given
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department1));

        // When
        Department actualDepartment = departmentService.getDepartmentById(1L);

        // Then
        assertNotNull(actualDepartment);
        assertEquals(1L, actualDepartment.getIdDepartment());
        assertEquals("Computer Science", actualDepartment.getName());
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDepartmentById_NotFound() {
        // Given
        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> {
            departmentService.getDepartmentById(999L);
        });
        verify(departmentRepository, times(1)).findById(999L);
    }

    @Test
    void testGetDepartmentById_NullId() {
        // Given
        when(departmentRepository.findById(null))
            .thenThrow(new IllegalArgumentException("ID cannot be null"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            departmentService.getDepartmentById(null);
        });
        verify(departmentRepository, times(1)).findById(null);
    }

    @Test
    void testSaveDepartment_Create() {
        // Given
        Department newDepartment = new Department();
        newDepartment.setName("Physics");
        
        Department savedDepartment = new Department();
        savedDepartment.setIdDepartment(3L);
        savedDepartment.setName("Physics");
        
        when(departmentRepository.save(any(Department.class))).thenReturn(savedDepartment);

        // When
        Department result = departmentService.saveDepartment(newDepartment);

        // Then
        assertNotNull(result);
        assertNotNull(result.getIdDepartment());
        assertEquals(3L, result.getIdDepartment());
        assertEquals("Physics", result.getName());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testSaveDepartment_Update() {
        // Given
        department1.setName("Computer Science & Engineering");
        when(departmentRepository.save(department1)).thenReturn(department1);

        // When
        Department result = departmentService.saveDepartment(department1);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getIdDepartment());
        assertEquals("Computer Science & Engineering", result.getName());
        verify(departmentRepository, times(1)).save(department1);
    }

    @Test
    void testSaveDepartment_NullDepartment() {
        // Given
        when(departmentRepository.save(null))
            .thenThrow(new IllegalArgumentException("Department cannot be null"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            departmentService.saveDepartment(null);
        });
        verify(departmentRepository, times(1)).save(null);
    }

    @Test
    void testDeleteDepartment_Success() {
        // Given
        doNothing().when(departmentRepository).deleteById(1L);

        // When
        departmentService.deleteDepartment(1L);

        // Then
        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteDepartment_NonExistentId() {
        // Given
        doNothing().when(departmentRepository).deleteById(anyLong());

        // When
        departmentService.deleteDepartment(999L);

        // Then
        verify(departmentRepository, times(1)).deleteById(999L);
        // Note: deleteById doesn't throw exception if ID doesn't exist in Spring Data JPA
    }

    @Test
    void testDeleteDepartment_NullId() {
        // Given
        doThrow(new IllegalArgumentException("ID cannot be null"))
            .when(departmentRepository).deleteById(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            departmentService.deleteDepartment(null);
        });
        verify(departmentRepository, times(1)).deleteById(null);
    }

    @Test
    void testGetAllDepartments_VerifyDepartmentProperties() {
        // Given
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department1, department2));

        // When
        List<Department> departments = departmentService.getAllDepartments();

        // Then
        assertEquals("Computer Science", departments.get(0).getName());
        assertEquals("Mathematics", departments.get(1).getName());
        assertEquals(1L, departments.get(0).getIdDepartment());
        assertEquals(2L, departments.get(1).getIdDepartment());
    }

    @Test
    void testSaveDepartment_WithSpecialCharacters() {
        // Given
        Department specialDepartment = new Department();
        specialDepartment.setName("Arts & Humanities - Literature");
        
        Department savedDepartment = new Department();
        savedDepartment.setIdDepartment(4L);
        savedDepartment.setName("Arts & Humanities - Literature");
        
        when(departmentRepository.save(specialDepartment)).thenReturn(savedDepartment);

        // When
        Department result = departmentService.saveDepartment(specialDepartment);

        // Then
        assertNotNull(result);
        assertEquals("Arts & Humanities - Literature", result.getName());
        verify(departmentRepository, times(1)).save(specialDepartment);
    }

    @Test
    void testSaveDepartment_MultipleCallsWithDifferentDepartments() {
        // Given
        when(departmentRepository.save(department1)).thenReturn(department1);
        when(departmentRepository.save(department2)).thenReturn(department2);

        // When
        Department result1 = departmentService.saveDepartment(department1);
        Department result2 = departmentService.saveDepartment(department2);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1.getIdDepartment(), result2.getIdDepartment());
        verify(departmentRepository, times(2)).save(any(Department.class));
    }
}