package tn.esprit.studentmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.studentmanagement.entities.Course;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.CourseRepository;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Enrollment testEnrollment;
    private Student testStudent;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();

        testStudent = new Student();
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setEmail("john.doe@test.com");
        testStudent = studentRepository.save(testStudent);

        testCourse = new Course();
        testCourse.setName("DevSecOps");
        testCourse.setCode("DEVSEC101");
        testCourse.setCredit(3);
        testCourse = courseRepository.save(testCourse);

        testEnrollment = new Enrollment();
        testEnrollment.setStudent(testStudent);
        testEnrollment.setCourse(testCourse);
        testEnrollment.setEnrollmentDate(LocalDate.now());
        testEnrollment.setGrade("A");
    }

    @Test
    void testGetAllEnrollment() throws Exception {
        enrollmentRepository.save(testEnrollment);

        mockMvc.perform(get("/Enrollment/getAllEnrollment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].grade").value("A"));
    }

    @Test
    void testGetEnrollmentById() throws Exception {
        Enrollment savedEnrollment = enrollmentRepository.save(testEnrollment);

        mockMvc.perform(get("/Enrollment/getEnrollment/" + savedEnrollment.getIdEnrollment()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value("A"))
                .andExpect(jsonPath("$.student.firstName").value("John"))
                .andExpect(jsonPath("$.course.name").value("DevSecOps"));
    }

    @Test
    void testCreateEnrollment() throws Exception {
        mockMvc.perform(post("/Enrollment/createEnrollment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEnrollment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value("A"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testUpdateEnrollment() throws Exception {
        Enrollment savedEnrollment = enrollmentRepository.save(testEnrollment);
        
        savedEnrollment.setGrade("B+");

        mockMvc.perform(put("/Enrollment/updateEnrollment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedEnrollment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value("B+"));
    }

    @Test
    void testDeleteEnrollment() throws Exception {
        Enrollment savedEnrollment = enrollmentRepository.save(testEnrollment);

        mockMvc.perform(delete("/Enrollment/deleteEnrollment/" + savedEnrollment.getIdEnrollment()))
                .andExpect(status().isOk());

    }

    @Test
    void testGetAllEnrollment_EmptyList() throws Exception {
        mockMvc.perform(get("/Enrollment/getAllEnrollment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testCreateEnrollmentWithMultipleStudents() throws Exception {
        enrollmentRepository.save(testEnrollment);

        Student student2 = new Student();
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setEmail("jane.smith@test.com");
        student2 = studentRepository.save(student2);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(student2);
        enrollment2.setCourse(testCourse);
        enrollment2.setEnrollmentDate(LocalDate.now());
        enrollment2.setGrade("B");

        mockMvc.perform(post("/Enrollment/createEnrollment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollment2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value("B"))
                .andExpect(jsonPath("$.student.firstName").value("Jane"));
    }

    @Test
    void testUpdateEnrollmentGrade() throws Exception {
        Enrollment savedEnrollment = enrollmentRepository.save(testEnrollment);
        
        savedEnrollment.setGrade("A+");
        savedEnrollment.setEnrollmentDate(LocalDate.now().plusDays(1));

        mockMvc.perform(put("/Enrollment/updateEnrollment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedEnrollment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value("A+"));
    }
}