package tn.esprit.studentmanagement.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class StudentController {
    
    IStudentService studentService;

    @GetMapping("/getAllStudents")
    public List<Student> getAllStudents() {
        log.info("Getting all students");
        return studentService.retrieveAllStudents();
    }

    @GetMapping("/getStudent/{id}")
    public Student getStudent(@PathVariable Long id) {
        log.info("Getting student with ID: {}", id);
        return studentService.retrieveStudent(id);
    }

    @PostMapping("/createStudent")
    public Student createStudent(@RequestBody Student student) {
        log.info("Creating student: {}", student.getFirstName());
        return studentService.addStudent(student);
    }

    @PutMapping("/updateStudent")
    public Student updateStudent(@RequestBody Student student) {
        log.info("Updating student with ID: {}", student.getIdStudent());
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/deleteStudent/{id}")
    public void deleteStudent(@PathVariable Long id) {
        log.info("Deleting student with ID: {}", id);
        studentService.removeStudent(id);
    }
}