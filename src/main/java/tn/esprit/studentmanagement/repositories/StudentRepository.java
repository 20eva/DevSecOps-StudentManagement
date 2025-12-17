package tn.esprit.studentmanagement.repositories;

import tn.esprit.studentmanagement.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // That's it! No custom methods needed unless you want specific queries
}