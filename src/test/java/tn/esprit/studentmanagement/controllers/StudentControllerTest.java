package tn.esprit.studentmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private IStudentService studentService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Student testStudent;
    
    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setIdStudent(1L);
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
    }
    
    @Test
    void getAllStudents_ShouldReturnStudentList() throws Exception {
        // Arrange
        List<Student> students = Arrays.asList(testStudent);
        when(studentService.retrieveAllStudents()).thenReturn(students);
        
        // Act & Assert
        mockMvc.perform(get("/students/getAllStudents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"));
        
        verify(studentService, times(1)).retrieveAllStudents();
    }
    
    @Test
    void getAllStudents_WhenNoStudents_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(studentService.retrieveAllStudents()).thenReturn(Collections.emptyList());
        
        // Act & Assert
        mockMvc.perform(get("/students/getAllStudents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        
        verify(studentService, times(1)).retrieveAllStudents();
    }
    
    @Test
    void getStudentById_ShouldReturnStudent() throws Exception {
        // Arrange
        when(studentService.retrieveStudent(1L)).thenReturn(testStudent);
        
        // Act & Assert
        mockMvc.perform(get("/students/getStudent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
        
        verify(studentService, times(1)).retrieveStudent(1L);
    }
    
    @Test
    void createStudent_ShouldReturnCreatedStudent() throws Exception {
        // Arrange
        when(studentService.addStudent(any(Student.class))).thenReturn(testStudent);
        
        // Act & Assert
        mockMvc.perform(post("/students/createStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
        
        verify(studentService, times(1)).addStudent(any(Student.class));
    }
    
    @Test
    void updateStudent_ShouldReturnUpdatedStudent() throws Exception {
        // Arrange
        Student updatedStudent = new Student();
        updatedStudent.setIdStudent(1L);
        updatedStudent.setFirstName("Jane");
        updatedStudent.setLastName("Smith");
        
        when(studentService.updateStudent(any(Student.class))).thenReturn(updatedStudent);
        
        // Act & Assert
        mockMvc.perform(put("/students/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));
        
        verify(studentService, times(1)).updateStudent(any(Student.class));
    }
    
    @Test
    void deleteStudent_ShouldReturnOk() throws Exception {
        // Arrange
        doNothing().when(studentService).removeStudent(anyLong());
        
        // Act & Assert
        mockMvc.perform(delete("/students/deleteStudent/1"))
                .andExpect(status().isOk());
        
        verify(studentService, times(1)).removeStudent(1L);
    }
}