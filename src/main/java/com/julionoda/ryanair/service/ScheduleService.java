package com.julionoda.ryanair.service;

import com.julionoda.ryanair.model.Flight;

/**
 * Service for flight schedules.
 * 
 * @author jnoda
 *
 */
public interface ScheduleService {
	/**
	 * Finds all flights going from the departure to the arrival airport for the
	 * given year and month.
	 * 
	 * @param airportFrom
	 *            the departure airport
	 * @param airportTo
	 *            the arrival airport
	 * @param year
	 *            the year
	 * @param month
	 *            the month
	 * @return
	 */
	Iterable<Flight> findFlights(String airportFrom, String airportTo, int year, int month);
}
