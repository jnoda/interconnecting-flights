package com.julionoda.ryanair.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Suite to test JSON serialization of {@link Interconnection}
 * 
 * @author jnoda
 *
 */
@RunWith(SpringRunner.class)
@JsonTest
public class Interconnection_JsonTest {
	@Autowired
	private JacksonTester<Interconnection> json;

	/**
	 * Test that the interconnection is correctly serialized to JSON.
	 */
	@Test
	public void testSerialize() throws Exception {
		Interconnection details = new Interconnection(Collections.singletonList(new Flight("234", "DUB", "MAD",
				LocalDateTime.of(2018, 7, 15, 23, 7), LocalDateTime.of(2018, 7, 16, 8, 6))));
		assertThat(this.json.write(details)).isEqualToJson("expectedInterconnection.json");
	}
}
