package com.king.grpc;

import com.king.grpc.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class StudentCrudGrpcApplicationUnitTest {

    @Mock
    private StudentRepository studentRepository;

    @Test
    void mainDelegatesToSpringApplicationRun() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            String[] args = new String[]{"--spring.main.banner-mode=off"};

            StudentCrudGrpcApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(eq(StudentCrudGrpcApplication.class), eq(args)));
        }
    }
}
