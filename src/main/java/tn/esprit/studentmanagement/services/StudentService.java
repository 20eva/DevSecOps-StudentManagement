package tn.esprit.studentmanagement.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StudentService implements IStudentService {
    
    StudentRepository studentRepository;
    
    @Override
    public List<Student> retrieveAllStudents() {
        log.info("Retrieving all students");
        return studentRepository.findAll();
    }
    
    @Override
    public Student addStudent(Student student) {
        log.info("Adding new student: {}", student.getFirstName());
        return studentRepository.save(student);
    }
    
    @Override
    public Student updateStudent(Student student) {
        log.info("Updating student with ID: {}", student.getIdStudent());
        return studentRepository.save(student);
    }
    
    @Override
    public Student retrieveStudent(Long idStudent) {
        log.info("Retrieving student with ID: {}", idStudent);
        return studentRepository.findById(idStudent)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + idStudent));
    }
    
    @Override
    public void removeStudent(Long idStudent) {
        log.info("Removing student with ID: {}", idStudent);
        if (!studentRepository.existsById(idStudent)) {
            throw new IllegalArgumentException("Student not found with id: " + idStudent);
        }
        studentRepository.deleteById(idStudent);
    }

    @Override
    public void deleteStudent(long anyLong) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteStudent'");
    }
}