package com.julionoda.ryanair.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Suite to test JSON deserialization of {@link Route}.
 * 
 * @author jnoda
 *
 */
@RunWith(SpringRunner.class)
@JsonTest
public class Route_JsonTest {
	@Autowired
	private JacksonTester<Route> json;

	/**
	 * Test that the route is correctly deserialized from JSON.
	 */
	@Test
	public void jsonDeserialize_Succeeds() throws Exception {
		String content = "{\"airportFrom\":\"DUB\",\"airportTo\":\"MAD\",\"connectingAirport\":\"BCN\"}";
		Route expectedObject = new Route("DUB", "MAD", "BCN");
		assertThat(this.json.parse(content)).isEqualToComparingFieldByField(expectedObject);
	}

	/**
	 * Test that the route deserialization from JSON throws an exception if
	 * airportFrom is null.
	 */
	@Test
	public void jsonDeserialize_ThrowsException_IfNullAirportFrom() throws Exception {
		String content = "{\"airportTo\":\"MAD\"}";
		assertThatThrownBy(() -> {
			this.json.parse(content);
		}).isInstanceOf(JsonMappingException.class).hasMessageContaining("airportFrom");
	}

	/**
	 * Test that the route deserialization from JSON throws an exception if
	 * airportTo is null.
	 */
	@Test
	public void jsonDeserialize_ThrowsException_IfNullAirportTo() throws Exception {
		String content = "{\"airportFrom\":\"MAD\"}";
		assertThatThrownBy(() -> {
			this.json.parse(content);
		}).isInstanceOf(JsonMappingException.class).hasMessageContaining("airportTo");
	}
}
