package com.king.grpc.entity;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EntityModelUnitTest {

    @Test
    void studentEntityBuilderAndAccessorsWork() {
        DepartmentEntity department = new DepartmentEntity();
        department.setDeptId(100L);
        department.setName("Computer Science");
        department.setLocation("Block A");

        Set<DepartmentEntity> departments = new HashSet<>();
        departments.add(department);

        StudentEntity student = StudentEntity.builder()
                .studentId(1L)
                .name("Alice")
                .email("alice@example.com")
                .age(20)
                .departments(departments)
                .build();

        assertEquals(1L, student.getStudentId());
        assertEquals("Alice", student.getName());
        assertEquals("alice@example.com", student.getEmail());
        assertEquals(20, student.getAge());
        assertEquals(1, student.getDepartments().size());

        student.setName("Alice Updated");
        student.setEmail("alice.updated@example.com");
        student.setAge(21);
        assertEquals("Alice Updated", student.getName());
        assertEquals("alice.updated@example.com", student.getEmail());
        assertEquals(21, student.getAge());

        assertTrue(student.toString().contains("Alice Updated"));
    }

    @Test
    void departmentEntityConstructorsAndAccessorsWork() {
        DepartmentEntity noArgs = new DepartmentEntity();
        noArgs.setDeptId(2L);
        noArgs.setName("Math");
        noArgs.setLocation("Block B");
        noArgs.setStudents(new HashSet<>());

        assertEquals(2L, noArgs.getDeptId());
        assertEquals("Math", noArgs.getName());
        assertEquals("Block B", noArgs.getLocation());
        assertNotNull(noArgs.getStudents());

        DepartmentEntity allArgs = new DepartmentEntity(3L, "Physics", "Block C", new HashSet<>());
        assertEquals(3L, allArgs.getDeptId());
        assertEquals("Physics", allArgs.getName());
        assertEquals("Block C", allArgs.getLocation());
        assertNotNull(allArgs.getStudents());

        DepartmentEntity built = DepartmentEntity.builder()
                .deptId(4L)
                .name("Chemistry")
                .location("Block D")
                .students(new HashSet<>())
                .build();
        assertEquals(4L, built.getDeptId());
        assertEquals("Chemistry", built.getName());
        assertEquals("Block D", built.getLocation());
        assertNotNull(built.getStudents());
    }
}
