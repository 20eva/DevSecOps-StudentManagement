package tn.esprit.studentmanagement.repositories;

import tn.esprit.studentmanagement.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class StudentRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private StudentRepository studentRepository;
    
    private Student testStudent;
    
    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
    }
    
    @Test
    void saveStudent_ShouldPersistStudent() {
        // Act
        Student saved = studentRepository.save(testStudent);
        
        // Assert
        assertNotNull(saved.getIdStudent());
        assertEquals("John", saved.getFirstName());
    }
    
    @Test
    void findById_WhenStudentExists_ShouldReturnStudent() {
        // Arrange
        Student saved = entityManager.persistAndFlush(testStudent);
        
        // Act
        Optional<Student> found = studentRepository.findById(saved.getIdStudent());
        
        // Assert
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
    }
    
    @Test
    void findAll_ShouldReturnAllStudents() {
        // Arrange
        entityManager.persistAndFlush(testStudent);
        
        Student anotherStudent = new Student();
        anotherStudent.setFirstName("Jane");
        anotherStudent.setLastName("Smith");
        entityManager.persistAndFlush(anotherStudent);
        
        // Act
        List<Student> students = studentRepository.findAll();
        
        // Assert
        assertEquals(2, students.size());
    }
    
    @Test
    void deleteById_ShouldRemoveStudent() {
        // Arrange
        Student saved = entityManager.persistAndFlush(testStudent);
        Long studentId = saved.getIdStudent();
        
        // Act
        studentRepository.deleteById(studentId);
        
        // Assert
        Optional<Student> found = studentRepository.findById(studentId);
        assertFalse(found.isPresent());
    }
}