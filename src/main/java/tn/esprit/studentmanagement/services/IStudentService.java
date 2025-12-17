package tn.esprit.studentmanagement.services;

import tn.esprit.studentmanagement.entities.Student;
import java.util.List;

public interface IStudentService {
    List<Student> retrieveAllStudents();
    Student addStudent(Student student);
    Student updateStudent(Student student);
    Student retrieveStudent(Long idStudent);
    void removeStudent(Long idStudent);
    void deleteStudent(long anyLong);
}