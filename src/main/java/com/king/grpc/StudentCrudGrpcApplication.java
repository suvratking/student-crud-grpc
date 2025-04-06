package com.king.grpc;

import com.github.javafaker.Faker;
import com.king.grpc.entity.StudentEntity;
import com.king.grpc.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@RequiredArgsConstructor
@EnableTransactionManagement
public class StudentCrudGrpcApplication {

	private final StudentRepository studentRepository;

	public static void main(String[] args) {
		SpringApplication.run(StudentCrudGrpcApplication.class, args);
	}

//	@PostConstruct
	public void insert() {
		Faker fake = Faker.instance();

		StudentEntity build = StudentEntity.builder().name(fake.name().name()).email(fake.internet().emailAddress()).age(fake.number().numberBetween(0, 80)).build();
		studentRepository.save(build);
	}

}
