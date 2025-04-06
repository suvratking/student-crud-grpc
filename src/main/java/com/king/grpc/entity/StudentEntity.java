package com.king.grpc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "STUDENT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)//, generator = "student_seq")
    @Column(name = "STUDENT_ID", updatable = false)
    private Long studentId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "AGE")
    private Integer age;

    /*@OneToOne
    @JoinColumn(name = "DEPT_ID", referencedColumnName = "DEPT_ID", foreignKey = @ForeignKey(name = "FK_DEPT_ID", value = ConstraintMode.CONSTRAINT))
    private DepartmentEntity department;*/

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "STUDENT_DEPT", joinColumns = @JoinColumn(name = "STUDENT_ID"), inverseJoinColumns = @JoinColumn(name = "DEPT_ID"))
    private Set<DepartmentEntity> departments;

}