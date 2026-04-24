package com.king.grpc;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RequiredArgsConstructor
@EnableTransactionManagement
@RestController
public class StudentCrudGrpcApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentCrudGrpcApplication.class, args);
	}

}
