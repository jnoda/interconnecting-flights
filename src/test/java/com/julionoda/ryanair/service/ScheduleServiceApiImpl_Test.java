package com.julionoda.ryanair.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import org.junit.Test;

import com.julionoda.ryanair.model.Flight;
import com.julionoda.ryanair.service.ScheduleServiceApiImpl.DailySchedule;
import com.julionoda.ryanair.service.ScheduleServiceApiImpl.MonthlySchedule;
import com.julionoda.ryanair.service.ScheduleServiceApiImpl.ScheduledFlight;

/**
 * Test suite for {@link ScheduleServiceApiImpl}.
 * 
 * @author jnoda
 *
 */
public class ScheduleServiceApiImpl_Test {

	/**
	 * Test that
	 * {@link ScheduleServiceApiImpl#createFlight(int, int, int, ScheduledFlight)}
	 * creates a new {@link Flight} correctly.
	 */
	@Test
	public void createFlight_Succeed() {
		ScheduledFlight scheduledFlight = new ScheduledFlight("1234", LocalTime.of(13, 22), LocalTime.of(17, 44));
		Flight flight = ScheduleServiceApiImpl.createFlight(2018, 10, 24, "DUB", "MAD", scheduledFlight);

		assertNotNull(flight);
		assertEquals(scheduledFlight.getNumber(), flight.getNumber());
		assertEquals(LocalDate.of(2018, 10, 24), flight.getDepartureDateTime().toLocalDate());
		assertEquals(scheduledFlight.getDepartureTime(), flight.getDepartureDateTime().toLocalTime());
		assertEquals(LocalDate.of(2018, 10, 24), flight.getArrivalDateTime().toLocalDate());
		assertEquals(scheduledFlight.getArrivalTime(), flight.getArrivalDateTime().toLocalTime());
		assertEquals("DUB", flight.getDepartureAirport());
		assertEquals("MAD", flight.getArrivalAirport());
	}

	/**
	 * Test that
	 * {@link ScheduleServiceApiImpl#createFlight(int, int, int, ScheduledFlight)}
	 * increase the arrival date if arrival time in the scheduled flight is before
	 * the departure time.
	 * 
	 * TODO Check if this is correct
	 */
	@Test
	public void createFlight_IncreasesArrivalDate_IfArrivalTimeBeforeDepartureTime() {
		ScheduledFlight scheduledFlight = new ScheduledFlight("1234", LocalTime.of(23, 22), LocalTime.of(3, 44));
		Flight flight = ScheduleServiceApiImpl.createFlight(2017, 12, 31, "DUB", "MAD", scheduledFlight);

		assertEquals(LocalDate.of(2017, 12, 31), flight.getDepartureDateTime().toLocalDate());
		assertEquals(LocalDate.of(2018, 01, 01), flight.getArrivalDateTime().toLocalDate());
	}

	/**
	 * Test that {@link ScheduleServiceApiImpl#extractFlights(int, MonthlySchedule)}
	 * extracts the {@link Flight} correctly.
	 */
	@Test
	public void extractFlights_Succeed() {
		ScheduledFlight scheduledFlight = new ScheduledFlight("1234", LocalTime.of(13, 22), LocalTime.of(17, 44));
		DailySchedule dailySchedule = new DailySchedule(24, Collections.singletonList(scheduledFlight));
		MonthlySchedule monthlySchedule = new MonthlySchedule(10, Collections.singletonList(dailySchedule));

		Iterable<Flight> result = ScheduleServiceApiImpl.extractFlights(2018, "DUB", "MAD", monthlySchedule);
		assertThat(result).isNotNull().isNotEmpty();

		Flight flight = result.iterator().next();
		// only need to test one property to verify the object is returned, actual
		// verification will be done on {link #createFlight_Succeed()}
		assertEquals(scheduledFlight.getNumber(), flight.getNumber());

	}
}
