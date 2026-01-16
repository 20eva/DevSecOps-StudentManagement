package tn.esprit.studentmanagement.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.studentmanagement.entities.Course;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.entities.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

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
        testEnrollment.setGrade("85.5");  // Double value
        testEnrollment.setStatus(Status.ACTIVE);  // Using Status enum
    }

    @Test
    void testSaveEnrollment() {
        Enrollment savedEnrollment = enrollmentRepository.save(testEnrollment);

        assertThat(savedEnrollment).isNotNull();
        assertThat(savedEnrollment.getIdEnrollment()).isNotNull();
        assertThat(savedEnrollment.getGrade()).isEqualTo("85.5" );
        assertThat(savedEnrollment.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(savedEnrollment.getStudent().getFirstName()).isEqualTo("John");
        assertThat(savedEnrollment.getCourse().getName()).isEqualTo("DevSecOps");
    }

    @Test
    void testFindById() {
        Enrollment savedEnrollment = enrollmentRepository.save(testEnrollment);

        Optional<Enrollment> foundEnrollment = enrollmentRepository.findById(savedEnrollment.getIdEnrollment());

        assertThat(foundEnrollment).isPresent();
        assertThat(foundEnrollment.get().getGrade()).isEqualTo("85.5");
        assertThat(foundEnrollment.get().getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(foundEnrollment.get().getStudent().getFirstName()).isEqualTo("John");
    }

    @Test
    void testFindById_NotFound() {
        Optional<Enrollment> foundEnrollment = enrollmentRepository.findById(99999L);

        assertThat(foundEnrollment).isEmpty();
    }

    @Test
    void testFindAll() {
        enrollmentRepository.save(testEnrollment);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(testStudent);
        enrollment2.setCourse(testCourse);
        enrollment2.setEnrollmentDate(LocalDate.now());
        enrollment2.setGrade("75.0");
        enrollment2.setStatus(Status.COMPLETED);
        enrollmentRepository.save(enrollment2);

        List<Enrollment> enrollments = enrollmentRepository.findAll();

        assertThat(enrollments).hasSize(2);
        assertThat(enrollments).extracting(Enrollment::getGrade)
                .contains("85.5", "75.0");
    }

    @Test
    void testUpdateEnrollment() {
        Enrollment savedEnrollment = enrollmentRepository.save(testEnrollment);

        savedEnrollment.setGrade("90.0");
        savedEnrollment.setStatus(Status.COMPLETED);
        Enrollment updatedEnrollment = enrollmentRepository.save(savedEnrollment);

        assertThat(updatedEnrollment.getGrade()).isEqualTo("90.0");
        assertThat(updatedEnrollment.getStatus()).isEqualTo(Status.COMPLETED);
        assertThat(updatedEnrollment.getStudent().getFirstName()).isEqualTo("John");
    }

    @Test
    void testDeleteEnrollment() {
        Enrollment savedEnrollment = enrollmentRepository.save(testEnrollment);
        Long enrollmentId = savedEnrollment.getIdEnrollment();

        enrollmentRepository.delete(savedEnrollment);

        Optional<Enrollment> deletedEnrollment = enrollmentRepository.findById(enrollmentId);
        assertThat(deletedEnrollment).isEmpty();
    }

    @Test
    void testDeleteById() {
        Enrollment savedEnrollment = enrollmentRepository.save(testEnrollment);
        Long enrollmentId = savedEnrollment.getIdEnrollment();

        enrollmentRepository.deleteById(enrollmentId);

        Optional<Enrollment> deletedEnrollment = enrollmentRepository.findById(enrollmentId);
        assertThat(deletedEnrollment).isEmpty();
    }

    @Test
    void testCount() {
        enrollmentRepository.save(testEnrollment);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(testStudent);
        enrollment2.setCourse(testCourse);
        enrollment2.setEnrollmentDate(LocalDate.now());
        enrollment2.setGrade("88.0");
        enrollment2.setStatus(Status.ACTIVE);
        enrollmentRepository.save(enrollment2);

        long count = enrollmentRepository.count();

        assertThat(count).isEqualTo(2);
    }

    @Test
    void testExistsById() {
        Enrollment savedEnrollment = enrollmentRepository.save(testEnrollment);

        boolean exists = enrollmentRepository.existsById(savedEnrollment.getIdEnrollment());
        boolean notExists = enrollmentRepository.existsById(99999L);

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void testDeleteAll() {
        enrollmentRepository.save(testEnrollment);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(testStudent);
        enrollment2.setCourse(testCourse);
        enrollment2.setEnrollmentDate(LocalDate.now());
        enrollment2.setGrade("92.0");
        enrollment2.setStatus(Status.ACTIVE);
        enrollmentRepository.save(enrollment2);

        enrollmentRepository.deleteAll();

        List<Enrollment> enrollments = enrollmentRepository.findAll();
        assertThat(enrollments).isEmpty();
    }

    @Test
    void testSaveMultipleEnrollments() {
        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(testStudent);
        enrollment2.setCourse(testCourse);
        enrollment2.setEnrollmentDate(LocalDate.now().plusDays(1));
        enrollment2.setGrade("78.5");
        enrollment2.setStatus(Status.ACTIVE);

        List<Enrollment> enrollments = List.of(testEnrollment, enrollment2);
        List<Enrollment> savedEnrollments = enrollmentRepository.saveAll(enrollments);

        assertThat(savedEnrollments).hasSize(2);
        assertThat(savedEnrollments).extracting(Enrollment::getGrade)
                .contains("85.5", "78.5");
    }

    @Test
    void testEnrollmentWithDifferentStatuses() {
        Enrollment activeEnrollment = enrollmentRepository.save(testEnrollment);

        Enrollment completedEnrollment = new Enrollment();
        completedEnrollment.setStudent(testStudent);
        completedEnrollment.setCourse(testCourse);
        completedEnrollment.setEnrollmentDate(LocalDate.now().minusDays(30));
        completedEnrollment.setGrade("95.0");
        completedEnrollment.setStatus(Status.COMPLETED);
        completedEnrollment = enrollmentRepository.save(completedEnrollment);

        List<Enrollment> allEnrollments = enrollmentRepository.findAll();

        assertThat(allEnrollments).hasSize(2);
        assertThat(allEnrollments).extracting(Enrollment::getStatus)
                .contains(Status.ACTIVE, Status.COMPLETED);
    }

    @Test
    void testUpdateEnrollmentDate() {
        Enrollment savedEnrollment = enrollmentRepository.save(testEnrollment);
        LocalDate newDate = LocalDate.now().plusDays(7);

        savedEnrollment.setEnrollmentDate(newDate);
        savedEnrollment.setGrade("87.0");
        Enrollment updatedEnrollment = enrollmentRepository.save(savedEnrollment);

        assertThat(updatedEnrollment.getEnrollmentDate()).isEqualTo(newDate);
        assertThat(updatedEnrollment.getGrade()).isEqualTo("87.0");
    }
}