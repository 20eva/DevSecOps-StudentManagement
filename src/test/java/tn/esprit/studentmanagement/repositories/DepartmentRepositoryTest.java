package tn.esprit.studentmanagement.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.studentmanagement.entities.Department;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();
        
        testDepartment = new Department();
        testDepartment.setName("Computer Science");
        testDepartment.setLocation("Building A, Floor 3");
        testDepartment.setPhone("+216 12 345 678");
        testDepartment.setHead("Dr. Ahmed Ben Ali");
    }

    @Test
    void testSaveDepartment() {
        Department savedDepartment = departmentRepository.save(testDepartment);

        assertThat(savedDepartment).isNotNull();
        assertThat(savedDepartment.getIdDepartment()).isNotNull();
        assertThat(savedDepartment.getName()).isEqualTo("Computer Science");
        assertThat(savedDepartment.getLocation()).isEqualTo("Building A, Floor 3");
        assertThat(savedDepartment.getPhone()).isEqualTo("+216 12 345 678");
        assertThat(savedDepartment.getHead()).isEqualTo("Dr. Ahmed Ben Ali");
    }

    @Test
    void testFindById() {
        Department savedDepartment = departmentRepository.save(testDepartment);

        Optional<Department> foundDepartment = departmentRepository.findById(savedDepartment.getIdDepartment());

        assertThat(foundDepartment).isPresent();
        assertThat(foundDepartment.get().getName()).isEqualTo("Computer Science");
        assertThat(foundDepartment.get().getLocation()).isEqualTo("Building A, Floor 3");
    }

    @Test
    void testFindById_NotFound() {
        Optional<Department> foundDepartment = departmentRepository.findById(99999L);

        assertThat(foundDepartment).isEmpty();
    }

    @Test
    void testFindAll() {
        departmentRepository.save(testDepartment);

        Department dept2 = new Department();
        dept2.setName("Mathematics");
        dept2.setLocation("Building C, Floor 1");
        dept2.setPhone("+216 98 765 432");
        dept2.setHead("Dr. Mohamed Trabelsi");
        departmentRepository.save(dept2);

        List<Department> departments = departmentRepository.findAll();

        assertThat(departments).hasSize(2);
        assertThat(departments).extracting(Department::getName)
                .contains("Computer Science", "Mathematics");
    }

    @Test
    void testUpdateDepartment() {
        Department savedDepartment = departmentRepository.save(testDepartment);

        savedDepartment.setName("Computer Science and Engineering");
        savedDepartment.setLocation("Building B, Floor 2");
        savedDepartment.setHead("Dr. Fatma Gharbi");
        Department updatedDepartment = departmentRepository.save(savedDepartment);

        assertThat(updatedDepartment.getName()).isEqualTo("Computer Science and Engineering");
        assertThat(updatedDepartment.getLocation()).isEqualTo("Building B, Floor 2");
        assertThat(updatedDepartment.getHead()).isEqualTo("Dr. Fatma Gharbi");
        assertThat(updatedDepartment.getPhone()).isEqualTo("+216 12 345 678");
    }

    @Test
    void testDeleteDepartment() {
        Department savedDepartment = departmentRepository.save(testDepartment);
        Long departmentId = savedDepartment.getIdDepartment();

        departmentRepository.delete(savedDepartment);

        Optional<Department> deletedDepartment = departmentRepository.findById(departmentId);
        assertThat(deletedDepartment).isEmpty();
    }

    @Test
    void testDeleteById() {
        Department savedDepartment = departmentRepository.save(testDepartment);
        Long departmentId = savedDepartment.getIdDepartment();

        departmentRepository.deleteById(departmentId);

        Optional<Department> deletedDepartment = departmentRepository.findById(departmentId);
        assertThat(deletedDepartment).isEmpty();
    }

    @Test
    void testCount() {
        departmentRepository.save(testDepartment);

        Department dept2 = new Department();
        dept2.setName("Engineering");
        dept2.setLocation("Building E");
        departmentRepository.save(dept2);

        long count = departmentRepository.count();

        assertThat(count).isEqualTo(2);
    }

    @Test
    void testExistsById() {
        Department savedDepartment = departmentRepository.save(testDepartment);

        boolean exists = departmentRepository.existsById(savedDepartment.getIdDepartment());
        boolean notExists = departmentRepository.existsById(99999L);

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void testDeleteAll() {
        departmentRepository.save(testDepartment);

        Department dept2 = new Department();
        dept2.setName("Physics");
        dept2.setLocation("Building D");
        departmentRepository.save(dept2);

        departmentRepository.deleteAll();

        List<Department> departments = departmentRepository.findAll();
        assertThat(departments).isEmpty();
    }

    @Test
    void testSaveMultipleDepartments() {
        Department dept2 = new Department();
        dept2.setName("Business Administration");
        dept2.setLocation("Building F, Floor 4");
        dept2.setPhone("+216 71 222 333");
        dept2.setHead("Dr. Leila Ben Mahmoud");

        List<Department> departments = List.of(testDepartment, dept2);
        List<Department> savedDepartments = departmentRepository.saveAll(departments);

        assertThat(savedDepartments).hasSize(2);
        assertThat(savedDepartments).extracting(Department::getName)
                .contains("Computer Science", "Business Administration");
    }

    @Test
    void testUpdateDepartmentPhone() {
        Department savedDepartment = departmentRepository.save(testDepartment);

        savedDepartment.setPhone("+216 55 123 456");
        Department updatedDepartment = departmentRepository.save(savedDepartment);

        assertThat(updatedDepartment.getPhone()).isEqualTo("+216 55 123 456");
        assertThat(updatedDepartment.getName()).isEqualTo("Computer Science");
    }

    @Test
    void testSaveDepartmentWithNullFields() {
        Department minimalDept = new Department();
        minimalDept.setName("Chemistry");
        minimalDept.setLocation("Building G");
        // phone and head are null

        Department savedDepartment = departmentRepository.save(minimalDept);

        assertThat(savedDepartment).isNotNull();
        assertThat(savedDepartment.getIdDepartment()).isNotNull();
        assertThat(savedDepartment.getName()).isEqualTo("Chemistry");
        assertThat(savedDepartment.getPhone()).isNull();
        assertThat(savedDepartment.getHead()).isNull();
    }

    @Test
    void testUpdateDepartmentLocation() {
        Department savedDepartment = departmentRepository.save(testDepartment);

        savedDepartment.setLocation("Building Z, Floor 5");
        Department updatedDepartment = departmentRepository.save(savedDepartment);

        assertThat(updatedDepartment.getLocation()).isEqualTo("Building Z, Floor 5");
        assertThat(updatedDepartment.getName()).isEqualTo("Computer Science");
        assertThat(updatedDepartment.getHead()).isEqualTo("Dr. Ahmed Ben Ali");
    }

    @Test
    void testFindAllOrderByName() {
        Department dept1 = new Department();
        dept1.setName("Zebra Department");
        dept1.setLocation("Building Z");
        departmentRepository.save(dept1);

        Department dept2 = new Department();
        dept2.setName("Alpha Department");
        dept2.setLocation("Building A");
        departmentRepository.save(dept2);

        departmentRepository.save(testDepartment);

        List<Department> departments = departmentRepository.findAll();

        assertThat(departments).hasSize(3);
        assertThat(departments).extracting(Department::getName)
                .contains("Computer Science", "Alpha Department", "Zebra Department");
    }
}