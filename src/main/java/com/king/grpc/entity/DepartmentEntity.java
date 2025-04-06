package com.king.grpc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "DEPARTMENT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "DEPT_ID", updatable = false)
    private Long deptId;

    private String name;

    private String location;

    @ManyToMany(mappedBy = "departments")
    Set<StudentEntity> students;

}