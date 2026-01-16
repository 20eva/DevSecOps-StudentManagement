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
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testGetAllDepartment() throws Exception {
        departmentRepository.save(testDepartment);

        mockMvc.perform(get("/Depatment/getAllDepartment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").value("Computer Science"))
                .andExpect(jsonPath("$[0].location").value("Building A, Floor 3"));
    }

    @Test
    void testGetDepartmentById() throws Exception {
        Department savedDepartment = departmentRepository.save(testDepartment);

        mockMvc.perform(get("/Depatment/getDepartment/" + savedDepartment.getIdDepartment()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Computer Science"))
                .andExpect(jsonPath("$.location").value("Building A, Floor 3"))
                .andExpect(jsonPath("$.phone").value("+216 12 345 678"))
                .andExpect(jsonPath("$.head").value("Dr. Ahmed Ben Ali"));
    }

    @Test
    void testCreateDepartment() throws Exception {
        mockMvc.perform(post("/Depatment/createDepartment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDepartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Computer Science"))
                .andExpect(jsonPath("$.location").value("Building A, Floor 3"))
                .andExpect(jsonPath("$.idDepartment").exists());
    }

    @Test
    void testUpdateDepartment() throws Exception {
        Department savedDepartment = departmentRepository.save(testDepartment);
        
        savedDepartment.setName("Computer Science and Engineering");
        savedDepartment.setLocation("Building B, Floor 2");
        savedDepartment.setHead("Dr. Fatma Gharbi");

        mockMvc.perform(put("/Depatment/updateDepartment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedDepartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Computer Science and Engineering"))
                .andExpect(jsonPath("$.location").value("Building B, Floor 2"))
                .andExpect(jsonPath("$.head").value("Dr. Fatma Gharbi"));
    }

    @Test
    void testDeleteDepartment() throws Exception {
        Department savedDepartment = departmentRepository.save(testDepartment);

        mockMvc.perform(delete("/Depatment/deleteDepartment/" + savedDepartment.getIdDepartment()))
                .andExpect(status().isOk());

        // Verify it's deleted by checking count
        long count = departmentRepository.count();
        assertThat(count).isEqualTo(0);
    }

    @Test
    void testGetAllDepartment_EmptyList() throws Exception {
        mockMvc.perform(get("/Depatment/getAllDepartment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testCreateMultipleDepartments() throws Exception {
        departmentRepository.save(testDepartment);

        Department dept2 = new Department();
        dept2.setName("Mathematics");
        dept2.setLocation("Building C, Floor 1");
        dept2.setPhone("+216 98 765 432");
        dept2.setHead("Dr. Mohamed Trabelsi");

        mockMvc.perform(post("/Depatment/createDepartment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dept2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mathematics"))
                .andExpect(jsonPath("$.head").value("Dr. Mohamed Trabelsi"));

        // Verify two departments exist
        long count = departmentRepository.count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testUpdateDepartmentPhone() throws Exception {
        Department savedDepartment = departmentRepository.save(testDepartment);
        
        savedDepartment.setPhone("+216 55 123 456");

        mockMvc.perform(put("/Depatment/updateDepartment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedDepartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("+216 55 123 456"))
                .andExpect(jsonPath("$.name").value("Computer Science"));
    }

    @Test
    void testCreateDepartmentWithMinimalInfo() throws Exception {
        Department minimalDept = new Department();
        minimalDept.setName("Physics");
        minimalDept.setLocation("Building D");

        mockMvc.perform(post("/Depatment/createDepartment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minimalDept)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Physics"))
                .andExpect(jsonPath("$.location").value("Building D"))
                .andExpect(jsonPath("$.idDepartment").exists());
    }

    @Test
    void testGetAllDepartments_MultipleDepartments() throws Exception {
        departmentRepository.save(testDepartment);

        Department dept2 = new Department();
        dept2.setName("Engineering");
        dept2.setLocation("Building E");
        dept2.setPhone("+216 70 111 222");
        dept2.setHead("Dr. Salah Eddine");
        departmentRepository.save(dept2);

        Department dept3 = new Department();
        dept3.setName("Business");
        dept3.setLocation("Building F");
        dept3.setPhone("+216 71 333 444");
        dept3.setHead("Dr. Leila Ben Mahmoud");
        departmentRepository.save(dept3);

        mockMvc.perform(get("/Depatment/getAllDepartment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Computer Science", "Engineering", "Business")));
    }
}