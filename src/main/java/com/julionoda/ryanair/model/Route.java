package com.julionoda.ryanair.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Represents a flight route.
 * 
 * @author jnoda
 *
 */
@Value
@Builder(builderClassName = "RouteBuilder")
@JsonDeserialize(builder = Route.RouteBuilder.class)
public class Route {
	/**
	 * Departure airport IATA code.
	 */
	@NonNull
	private String airportFrom;

	/**
	 * Arrival airport IATA code.
	 */
	@NonNull
	private String airportTo;

	/**
	 * Connecting airport IATA code.
	 */
	private String connectingAirport;

	/**
	 * Builder for {@link Route}.
	 * 
	 * @author jnoda
	 *
	 */
	@JsonPOJOBuilder(withPrefix = "")
	public static class RouteBuilder {
		// Lombok will add constructor, setters, build method
	}
}
