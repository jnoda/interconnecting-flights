package com.julionoda.ryanair.service;

import java.time.LocalDateTime;

import com.julionoda.ryanair.model.Interconnection;

/**
 * Service for interconnections.
 * 
 * @author jnoda
 *
 */
public interface InterconnectionService {
	/**
	 * Finds all the direct or interconnecting flights (maximum 1 stop) departing
	 * from the given airport not earlier that the specified departure datetime and
	 * arriving to a given arrival airport not latter than the specified arrival
	 * datetime.
	 * 
	 * @param departure
	 *            the departure airport IATA code
	 * @param arrival
	 *            the arrival airport IATA code
	 * @param departureDateTime
	 *            the departure datetime
	 * @param arrivalDateTime
	 *            the arrival datetime
	 * @return an iterable of direct or interconnecting flights
	 */
	Iterable<Interconnection> findBy(String departure, String arrival, LocalDateTime departureDateTime,
			LocalDateTime arrivalDateTime);
}
