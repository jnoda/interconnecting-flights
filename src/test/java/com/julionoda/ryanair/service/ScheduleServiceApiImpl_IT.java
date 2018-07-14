package com.julionoda.ryanair.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test suite for {@link ScheduleServiceApiImpl}.
 * 
 * @author jnoda
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class ScheduleServiceApiImpl_IT {

	@Autowired
	private ScheduleService scheduleService;

	/**
	 * Tests that
	 * {@link ScheduleServiceApiImpl#findFlights(String, String, int, int)} return
	 * results.
	 */
	@Test
	public void findFlights_ReturnSomeResults() {
		LocalDate now = LocalDate.now();
		assertThat(scheduleService.findFlights("DUB", "MAD", now.getYear(), now.getMonthValue()))
				.isNotNull()
				.isNotEmpty();
	}
}
