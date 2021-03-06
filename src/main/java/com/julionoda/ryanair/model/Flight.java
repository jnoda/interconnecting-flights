package com.julionoda.ryanair.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.NonNull;
import lombok.Value;

/**
 * Represents a flight.
 * 
 * @author jnoda
 *
 */
@Value
public class Flight {
	/**
	 * Flight number
	 */
	@NonNull
	private String number;

	/**
	 * Departure airport IATA code.
	 */
	@NonNull
	private String departureAirport;

	/**
	 * Arrival airport IATA code.
	 */
	@NonNull
	private String arrivalAirport;

	/**
	 * Departure date and time in the departure airport timezone.
	 */
	@NonNull
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime departureDateTime;

	/**
	 * Arrival date and time in the arrival airport timezone.
	 */
	@NonNull
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime arrivalDateTime;
}
