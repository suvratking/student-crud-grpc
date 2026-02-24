package com.king.grpc;

import com.king.grpc.entity.StudentEntity;
import com.king.grpc.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class StudentCrudGrpcApplicationUnitTest {

    @Mock
    private StudentRepository studentRepository;

    @Test
    void helloReturnsExpectedMessage() {
        StudentCrudGrpcApplication app = new StudentCrudGrpcApplication(studentRepository);

        String response = app.hello();

        assertEquals("Hello World", response);
    }

    @Test
    void insertSavesGeneratedStudent() {
        StudentCrudGrpcApplication app = new StudentCrudGrpcApplication(studentRepository);

        app.insert();

        ArgumentCaptor<StudentEntity> captor = ArgumentCaptor.forClass(StudentEntity.class);
        verify(studentRepository).save(captor.capture());
        StudentEntity saved = captor.getValue();
        assertNotNull(saved);
        assertNotNull(saved.getName());
        assertFalse(saved.getName().isBlank());
        assertNotNull(saved.getEmail());
        assertFalse(saved.getEmail().isBlank());
        assertNotNull(saved.getAge());
        assertTrue(saved.getAge() >= 0 && saved.getAge() < 80);
    }

    @Test
    void mainDelegatesToSpringApplicationRun() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            String[] args = new String[]{"--spring.main.banner-mode=off"};

            StudentCrudGrpcApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(eq(StudentCrudGrpcApplication.class), eq(args)));
        }
    }
}
