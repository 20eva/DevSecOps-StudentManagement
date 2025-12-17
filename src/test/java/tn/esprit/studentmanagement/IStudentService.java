package tn.esprit.studentmanagement;

import tn.esprit.studentmanagement.entities.Student;
import java.util.List;

public interface IStudentService {
    List<Student> retrieveAllStudents();
    Student retrieveStudent(Long id);
    Student addStudent(Student student);
    Student updateStudent(Student student);
    void removeStudent(Long id);
}