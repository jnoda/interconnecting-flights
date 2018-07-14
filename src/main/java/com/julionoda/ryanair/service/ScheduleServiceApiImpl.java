package com.julionoda.ryanair.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.julionoda.ryanair.model.Flight;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.Builder;
import lombok.NonNull;

/**
 * Implementation of {@link ScheduleService} that consumes the data from an
 * external schedules API.
 * 
 * @author jnoda
 *
 */
@Service
public class ScheduleServiceApiImpl implements ScheduleService {
	/**
	 * Endpoint of the schedules API.
	 */
	@Value("${schedulesApiEndpoint}")
	private String schedulesApiEndpoint;

	/**
	 * REST template to query the external API.
	 */
	private final RestTemplate restTemplate;

	/**
	 * Class constructor.
	 */
	public ScheduleServiceApiImpl() {
		// We might have used a global RestTemplate or RestTemplateBuilder, but that
		// would add some complexity to integration tests; and don't seem needed for
		// this simple scenario.
		this.restTemplate = new RestTemplate();
	}

	@HystrixCommand(fallbackMethod = "findFlightsFallback")
	@Override
	public Iterable<Flight> findFlights(String airportFrom, String airportTo, int year, int month) {
		ResponseEntity<MonthlySchedule> response = restTemplate.exchange(schedulesApiEndpoint, HttpMethod.GET, null,
				new ParameterizedTypeReference<MonthlySchedule>() {
				}, airportFrom, airportTo, year, month);
		return extractFlights(year, airportFrom, airportTo, response.getBody());
	}

	/**
	 * Fallback implementation of {@link #findFlights(String, String, int, int)} to
	 * use if the external API has problems. This allows to keep processing data
	 * even if the endpoint fails.
	 * 
	 * @return an empty list
	 */
	public Iterable<Flight> findFlightsFallback(String airportFrom, String airportTo, int year, int month) {
		return new ArrayList<>();
	}

	/**
	 * Extracts flights from a given monthly schedule.
	 * 
	 * @param year
	 *            the year
	 * @param airportFrom
	 *            the departure airport IATA code
	 * @param airportTo
	 *            the arrival airport IATA code
	 * @param schedule
	 *            the monthly schedule
	 * @return an iterable of flights in the given schedule
	 */
	static Iterable<Flight> extractFlights(int year, String airportFrom, String airportTo, MonthlySchedule schedule) {
		return StreamSupport
				.stream(schedule.getDays().spliterator(), true)
				.flatMap(dailySchedule -> StreamSupport
						.stream(dailySchedule.getFlights().spliterator(), true)
						.map(scheduledFlight -> createFlight(year, schedule.getMonth(), dailySchedule.getDay(),
								airportFrom, airportTo, scheduledFlight)))
				.collect(Collectors.toList());
	}

	/**
	 * Creates a {@link Flight} from a given {@link ScheduledFlight}.
	 * 
	 * @param year
	 *            the year
	 * @param month
	 *            the month
	 * @param day
	 *            the day
	 * @param departureAirport
	 *            the departure airport IATA code
	 * @param arrivalAirport
	 *            the arrival airport IATA code
	 * @param scheduledFlight
	 *            the scheduled flight
	 * @return a new {@link Flight} from a given {@link ScheduledFlight}.
	 */
	static Flight createFlight(int year, int month, int day, String departureAirport, String arrivalAirport,
			ScheduledFlight scheduledFlight) {
		LocalDate departureDate = LocalDate.of(year, month, day);
		return new Flight(scheduledFlight.getNumber(), departureAirport, arrivalAirport,
				LocalDateTime.of(departureDate, scheduledFlight.getDepartureTime()),
				LocalDateTime.of(
						(scheduledFlight.getArrivalTime().isAfter(scheduledFlight.getDepartureTime())) ? departureDate
								: departureDate.plusDays(1),
						scheduledFlight.getArrivalTime()));
	}

	/**
	 * Represents a monthly schedule of flights.
	 * 
	 * <p>
	 * This is an inner class because it is only used for deserialization and not
	 * relevant to the rest of the application.
	 * </p>
	 * 
	 * @author jnoda
	 *
	 */
	@lombok.Value
	@Builder(builderClassName = "MonthlyScheduleBuilder")
	@JsonDeserialize(builder = MonthlySchedule.MonthlyScheduleBuilder.class)
	static class MonthlySchedule {
		/**
		 * The month of the year
		 */
		@NonNull
		private Integer month;

		/**
		 * The daily schedules for the month.
		 */
		@NonNull
		private Iterable<DailySchedule> days;

		/**
		 * Builder for {@link MonthlySchedule}.
		 * 
		 * @author jnoda
		 *
		 */
		@JsonPOJOBuilder(withPrefix = "")
		public static class MonthlyScheduleBuilder {
			// Lombok will add constructor, setters, build method
		}
	}

	/**
	 * Represents a daily flight schedule.
	 * 
	 * <p>
	 * This is an inner class because it is only used for deserialization and not
	 * relevant to the rest of the application.
	 * </p>
	 * 
	 * @author jnoda
	 *
	 */
	@lombok.Value
	@Builder(builderClassName = "DailyScheduleBuilder")
	@JsonDeserialize(builder = DailySchedule.DailyScheduleBuilder.class)
	static class DailySchedule {
		/**
		 * The day of the month.
		 */
		@NonNull
		private Integer day;

		/**
		 * The flights scheduled for the day
		 */
		@NonNull
		private Iterable<ScheduledFlight> flights;

		/**
		 * Builder for {@link DailySchedule}.
		 * 
		 * @author jnoda
		 *
		 */
		@JsonPOJOBuilder(withPrefix = "")
		public static class DailyScheduleBuilder {
			// Lombok will add constructor, setters, build method
		}
	}

	/**
	 * Represents a scheduled flight.
	 * 
	 * <p>
	 * This is an inner class because it is only used for deserialization and not
	 * relevant to the rest of the application.
	 * </p>
	 * 
	 * @author jnoda
	 *
	 */
	@lombok.Value
	@Builder(builderClassName = "ScheduledFlightBuilder")
	@JsonDeserialize(builder = ScheduledFlight.ScheduledFlightBuilder.class)
	static class ScheduledFlight {
		/**
		 * Flight number
		 */
		@NonNull
		private String number;

		/**
		 * Departure time in the departure airport timezone.
		 */
		@NonNull
		private LocalTime departureTime;

		/**
		 * Arrival time in the arrival airport timezone.
		 */
		@NonNull
		private LocalTime arrivalTime;

		/**
		 * Builder for {@link ScheduledFlight}.
		 * 
		 * @author jnoda
		 *
		 */
		@JsonPOJOBuilder(withPrefix = "")
		public static class ScheduledFlightBuilder {
			// Lombok will add constructor, setters, build method
		}
	}
}
