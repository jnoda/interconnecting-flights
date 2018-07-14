package com.julionoda.ryanair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test suite for {@link RyanairApplication}.
 * 
 * @author jnoda
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RyanairApplication_IT {

	/**
	 * Tests that the Spring Boot context loads correctly.
	 */
	@Test
	public void contextLoads() {
	}
}
