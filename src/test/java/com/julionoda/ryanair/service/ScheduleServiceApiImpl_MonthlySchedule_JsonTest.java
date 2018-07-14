package com.julionoda.ryanair.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.julionoda.ryanair.service.ScheduleServiceApiImpl.DailySchedule;
import com.julionoda.ryanair.service.ScheduleServiceApiImpl.MonthlySchedule;

/**
 * Suite to test JSON deserialization of {@link MonthlySchedule}.
 * 
 * @author jnoda
 *
 */
@RunWith(SpringRunner.class)
@JsonTest
public class ScheduleServiceApiImpl_MonthlySchedule_JsonTest {
	@Autowired
	private JacksonTester<MonthlySchedule> json;

	/**
	 * Test that the monthly schedule is correctly deserialized from JSON.
	 */
	@Test
	public void jsonDeserialize_Succeeds() throws Exception {
		String content = "{\"month\":\"3\",\"days\":[{\"day\":12,\"flights\":[]}]}";
		MonthlySchedule expectedObject = new MonthlySchedule(3,
				Collections.singletonList(new DailySchedule(12, Collections.emptyList())));
		assertThat(this.json.parse(content)).isEqualToComparingFieldByField(expectedObject);
	}

	/**
	 * Test that the deserialization from JSON throws an exception if month is null.
	 */
	@Test
	public void jsonDeserialize_ThrowsException_IfNullMonth() throws Exception {
		String content = "{\"days\":[]}";
		assertThatThrownBy(() -> {
			this.json.parse(content);
		}).isInstanceOf(JsonMappingException.class).hasMessageContaining("month");
	}

	/**
	 * Test that the deserialization from JSON throws an exception if days is null.
	 */
	@Test
	public void jsonDeserialize_ThrowsException_IfNullDays() throws Exception {
		String content = "{\"month\":\"12\"}";
		assertThatThrownBy(() -> {
			this.json.parse(content);
		}).isInstanceOf(JsonMappingException.class).hasMessageContaining("days");
	}
}
