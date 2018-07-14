package com.julionoda.ryanair.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test suite for {@link RouteServiceApiImpl}.
 * 
 * @author jnoda
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class RouteServiceApiImpl_IT {

	@Autowired
	private RouteService routeService;

	/**
	 * Tests that {@link RouteServiceApiImpl#findAll()} return results.
	 */
	@Test
	public void findAll_ReturnSomeResults() {
		assertThat(routeService.findAll()).isNotNull().isNotEmpty();
	}
}
