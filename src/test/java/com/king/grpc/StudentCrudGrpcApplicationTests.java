package com.king.grpc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest
@Profile(value = "test")
class StudentCrudGrpcApplicationTests {

	@Test
	void contextLoads() {
	}

}
