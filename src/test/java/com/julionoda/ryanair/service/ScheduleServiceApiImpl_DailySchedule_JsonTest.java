package com.julionoda.ryanair.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.julionoda.ryanair.service.ScheduleServiceApiImpl.DailySchedule;
import com.julionoda.ryanair.service.ScheduleServiceApiImpl.ScheduledFlight;

/**
 * Suite to test JSON deserialization of {@link DailySchedule}.
 * 
 * @author jnoda
 *
 */
@RunWith(SpringRunner.class)
@JsonTest
public class ScheduleServiceApiImpl_DailySchedule_JsonTest {
	@Autowired
	private JacksonTester<DailySchedule> json;

	/**
	 * Test that the daily schedule is correctly deserialized from JSON.
	 */
	@Test
	public void jsonDeserialize_Succeeds() throws Exception {
		String content = "{\"day\":\"12\",\"flights\":[{\"number\":\"7256\",\"departureTime\":\"14:25\",\"arrivalTime\":\"17:32\"}]}";
		DailySchedule expectedObject = new DailySchedule(12,
				Collections.singletonList(new ScheduledFlight("7256", LocalTime.of(14, 25), LocalTime.of(17, 32))));
		assertThat(this.json.parse(content)).isEqualToComparingFieldByField(expectedObject);
	}

	/**
	 * Test that the deserialization from JSON throws an exception if day is null.
	 */
	@Test
	public void jsonDeserialize_ThrowsException_IfNullDay() throws Exception {
		String content = "{\"flights\":[]}";
		assertThatThrownBy(() -> {
			this.json.parse(content);
		}).isInstanceOf(JsonMappingException.class).hasMessageContaining("day");
	}

	/**
	 * Test that the deserialization from JSON throws an exception if flights is
	 * null.
	 */
	@Test
	public void jsonDeserialize_ThrowsException_IfNullFlights() throws Exception {
		String content = "{\"day\":\"12\"}";
		assertThatThrownBy(() -> {
			this.json.parse(content);
		}).isInstanceOf(JsonMappingException.class).hasMessageContaining("flights");
	}
}
