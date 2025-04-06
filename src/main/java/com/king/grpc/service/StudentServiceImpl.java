package com.king.grpc.service;

import com.king.grpc.department.*;
import com.king.grpc.entity.DepartmentEntity;
import com.king.grpc.repository.DepartmentRepository;
import com.king.grpc.student.*;
import com.king.grpc.entity.StudentEntity;
import com.king.grpc.repository.StudentRepository;
import com.king.grpc.student.Empty;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@GrpcService
public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    public StudentServiceImpl(StudentRepository studentRepository, DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void getStudent(GetStudentRequest request, StreamObserver<GetStudentResponse> responseObserver) {
        var id = request.getId();
        GetStudentResponse getStudentResponse = studentRepository.findById(id).map(this::buildResponse).orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        responseObserver.onNext(getStudentResponse);
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void createStudent(CreateStudentRequest request, StreamObserver<CreateStudentResponse> responseObserver) {
        Set<DepartmentEntity> dptList = validateDeptIds(request.getDepartmentIdList());
        if (Objects.isNull(dptList)){
            responseObserver.onError(new RuntimeException("Invalid dept id"));
            responseObserver.onCompleted();
            return;
        }
        StudentEntity build = StudentEntity.builder().age(request.getAge()).email(request.getEmail()).name(request.getName()).departments(dptList).build();
        StudentEntity student = studentRepository.save(build);
        CreateStudentResponse createStudentResponse = CreateStudentResponse.newBuilder()
                .setId(student.getStudentId())
                .setEmail(student.getEmail())
                .setName(student.getName())
                .setAge(student.getAge())
                .addAllDepartment(mapToDepartment(student.getDepartments()))
                .build();
        responseObserver.onNext(createStudentResponse);
        responseObserver.onCompleted();
    }

    private GetStudentResponse buildResponse(StudentEntity student) {
        return GetStudentResponse.newBuilder()
                .setId(student.getStudentId())
                .setName(student.getName())
                .setAge(student.getAge())
                .setEmail(student.getEmail())
                .addAllDepartment(mapToDepartment(student.getDepartments()))
                .build();
    }

    @Override
    public void listStudents(Empty request, StreamObserver<ListStudentsResponse> responseObserver) {
        List<GetStudentResponse> list = studentRepository.findAll().stream().map(this::buildResponse).toList();
        responseObserver.onNext(ListStudentsResponse.newBuilder().addAllStudents(list).build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateStudent(UpdateStudentRequest request, StreamObserver<CreateStudentResponse> responseObserver) {
        StudentEntity studentDb = studentRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getId()));
        Set<DepartmentEntity> dptList = validateDeptIds(request.getDepartmentIdList());
        if (Objects.isNull(dptList)){
            responseObserver.onError(new RuntimeException("Invalid dept id"));
            responseObserver.onCompleted();
            return;
        }

        studentDb.setEmail(request.getEmail());
        studentDb.setName(request.getName());
        studentDb.setAge(request.getAge());
        studentDb.setDepartments(dptList);

        StudentEntity student = studentRepository.save(studentDb);
        CreateStudentResponse createStudentResponse = CreateStudentResponse.newBuilder()
                .setId(student.getStudentId())
                .setEmail(student.getEmail())
                .setName(student.getName())
                .setAge(student.getAge())
                .addAllDepartment(mapToDepartment(student.getDepartments()))
                .build();
        responseObserver.onNext(createStudentResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteStudent(DeleteStudentRequest request, StreamObserver<DeleteStudentResponse> responseObserver) {
        StudentEntity studentDb = studentRepository.findById(request.getId()).orElse(null);
        if (studentDb == null){
            responseObserver.onNext(DeleteStudentResponse.newBuilder().setMessage("Student not found with id: " + request.getId()).setStatus(false).build());
            responseObserver.onCompleted();
            return;
        }
        studentRepository.delete(studentDb);

        responseObserver.onNext(DeleteStudentResponse.newBuilder().setMessage("Deleted successfully").setStatus(true).build());
        responseObserver.onCompleted();
    }

    private Set<DepartmentEntity> validateDeptIds(List<Long> deptIds) {
        Set<DepartmentEntity> dptList = deptIds.stream().map(id -> departmentRepository.findById(id).orElse(null)).collect(Collectors.toSet());
        if (dptList.stream().anyMatch(Objects::isNull)) {
            return null;
        }
        return dptList;
    }

    private Set<Department> mapToDepartment(Set<DepartmentEntity> departments) {
        return departments.stream().map(dpt -> Department.newBuilder()
                .setId(dpt.getDeptId())
                .setName(dpt.getName())
                .setLocation(dpt.getLocation())
                .build()).collect(Collectors.toSet());
    }
}
