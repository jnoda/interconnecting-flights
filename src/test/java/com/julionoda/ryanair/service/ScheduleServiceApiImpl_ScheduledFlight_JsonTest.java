package com.julionoda.ryanair.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.julionoda.ryanair.service.ScheduleServiceApiImpl.ScheduledFlight;

/**
 * Suite to test JSON deserialization of {@link ScheduledFlight}.
 * 
 * @author jnoda
 *
 */
@RunWith(SpringRunner.class)
@JsonTest
public class ScheduleServiceApiImpl_ScheduledFlight_JsonTest {
	@Autowired
	private JacksonTester<ScheduledFlight> json;

	/**
	 * Test that the scheduled flight is correctly deserialized from JSON.
	 */
	@Test
	public void jsonDeserialize_Succeeds() throws Exception {
		String content = "{\"number\":\"1234\",\"departureTime\":\"14:25\",\"arrivalTime\":\"17:32\"}";
		ScheduledFlight expectedObject = new ScheduledFlight("1234", LocalTime.of(14, 25), LocalTime.of(17, 32));
		assertThat(this.json.parse(content)).isEqualToComparingFieldByField(expectedObject);
	}

	/**
	 * Test that the deserialization from JSON throws an exception if number is
	 * null.
	 */
	@Test
	public void jsonDeserialize_ThrowsException_IfNullNumber() throws Exception {
		String content = "{\"departureTime\":\"14:25\",\"arrivalTime\":\"17:32\"}";
		assertThatThrownBy(() -> {
			this.json.parse(content);
		}).isInstanceOf(JsonMappingException.class).hasMessageContaining("number");
	}

	/**
	 * Test that the deserialization from JSON throws an exception if departureTime
	 * is null.
	 */
	@Test
	public void jsonDeserialize_ThrowsException_IfNullDepartureTime() throws Exception {
		String content = "{\"number\":\"1234\",\"arrivalTime\":\"17:32\"}";
		assertThatThrownBy(() -> {
			this.json.parse(content);
		}).isInstanceOf(JsonMappingException.class).hasMessageContaining("departureTime");
	}

	/**
	 * Test that the deserialization from JSON throws an exception if departureTime
	 * is null.
	 */
	@Test
	public void jsonDeserialize_ThrowsException_IfNullArrivalTime() throws Exception {
		String content = "{\"number\":\"1234\",\"departureTime\":\"14:25\"}";
		assertThatThrownBy(() -> {
			this.json.parse(content);
		}).isInstanceOf(JsonMappingException.class).hasMessageContaining("arrivalTime");
	}
}
