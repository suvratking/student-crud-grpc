package com.king.grpc.service;

import com.king.grpc.department.*;
import com.king.grpc.entity.DepartmentEntity;
import com.king.grpc.repository.DepartmentRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class DepartmentService extends DepartmentServiceGrpc.DepartmentServiceImplBase {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void createDepartment(CreateDepartmentRequest request, StreamObserver<CreateDepartmentResponse> responseObserver) {
        DepartmentEntity build = DepartmentEntity.builder().name(request.getName()).location(request.getLocation()).build();
        DepartmentEntity department = departmentRepository.save(build);
        responseObserver.onNext(this.mapToResponse(department));
        responseObserver.onCompleted();
    }

    @Override
    public void getDepartment(GetDepartmentRequest request, StreamObserver<GetDepartmentResponse> responseObserver) {
        super.getDepartment(request, responseObserver);
    }

    @Override
    public void updateDepartment(UpdateDepartmentRequest request, StreamObserver<CreateDepartmentResponse> responseObserver) {
        super.updateDepartment(request, responseObserver);
    }

    @Override
    public void deleteDepartment(DeleteDepartmentRequest request, StreamObserver<DeleteDepartmentResponse> responseObserver) {
        super.deleteDepartment(request, responseObserver);
    }

    @Override
    public void listDepartments(Empty request, StreamObserver<ListDepartmentsResponse> responseObserver) {
        super.listDepartments(request, responseObserver);
    }

    private CreateDepartmentResponse mapToResponse(DepartmentEntity department) {
        return CreateDepartmentResponse.newBuilder()
                .setId(department.getDeptId())
                .setName(department.getName())
                .setLocation(department.getLocation())
                .build();
    }
}
