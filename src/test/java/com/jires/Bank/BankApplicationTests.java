package com.jires.Bank;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class BankApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void mainMethodExecutesWithoutException() {
		try {
			BankApplication.main(new String[]{});
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
		assertTrue(true);
	}




}
