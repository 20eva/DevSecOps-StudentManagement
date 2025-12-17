package tn.esprit.studentmanagement.services;

import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    
    @Mock
    private StudentRepository studentRepository;
    
    @InjectMocks
    private StudentService studentService;
    
    private Student testStudent;
    
    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setIdStudent(1L);
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
    }
    
    @Test
    void retrieveAllStudents_ShouldReturnAllStudents() {
        // Arrange
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findAll()).thenReturn(students);
        
        // Act
        List<Student> result = studentService.retrieveAllStudents();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }
    
    @Test
    void retrieveAllStudents_WhenNoStudents_ShouldReturnEmptyList() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());
        
        // Act
        List<Student> result = studentService.retrieveAllStudents();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findAll();
    }
    
    @Test
    void retrieveStudent_WhenStudentExists_ShouldReturnStudent() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        
        // Act
        Student result = studentService.retrieveStudent(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(studentRepository, times(1)).findById(1L);
    }
    
    @Test
    void retrieveStudent_WhenStudentNotExists_ShouldThrowException() {
        // Arrange
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentService.retrieveStudent(999L);
        });
        
        assertEquals("Student not found with id: 999", exception.getMessage());
        verify(studentRepository, times(1)).findById(999L);
    }
    
    @Test
    void addStudent_ShouldSaveAndReturnStudent() {
        // Arrange
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
        
        // Act
        Student result = studentService.addStudent(testStudent);
        
        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(studentRepository, times(1)).save(testStudent);
    }
    
    @Test
    void updateStudent_ShouldUpdateAndReturnStudent() {
        // Arrange
        Student updatedStudent = new Student();
        updatedStudent.setIdStudent(1L);
        updatedStudent.setFirstName("Jane");
        updatedStudent.setLastName("Smith");
        
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);
        
        // Act
        Student result = studentService.updateStudent(updatedStudent);
        
        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        verify(studentRepository, times(1)).save(updatedStudent);
    }
    
    @Test
    void removeStudent_WhenStudentExists_ShouldDeleteStudent() {
        // Arrange
        Long studentId = 1L;
        when(studentRepository.existsById(studentId)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(studentId);
        
        // Act
        studentService.removeStudent(studentId);
        
        // Assert
        verify(studentRepository, times(1)).existsById(studentId);
        verify(studentRepository, times(1)).deleteById(studentId);
    }
    
    @Test
    void removeStudent_WhenStudentNotExists_ShouldThrowException() {
        // Arrange
        Long studentId = 999L;
        when(studentRepository.existsById(studentId)).thenReturn(false);
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentService.removeStudent(studentId);
        });
        
        assertEquals("Student not found with id: 999", exception.getMessage());
        verify(studentRepository, times(1)).existsById(studentId);
        verify(studentRepository, never()).deleteById(studentId);
    }
}