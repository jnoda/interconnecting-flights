package com.julionoda.ryanair.service;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julionoda.ryanair.model.Flight;
import com.julionoda.ryanair.model.Interconnection;
import com.julionoda.ryanair.model.Route;

import lombok.Value;

/**
 * Implementation of {@link InterconnectionService}.
 * 
 * @author jnoda
 *
 */
@Service
public class InterconnectionServiceImpl implements InterconnectionService {
	/**
	 * The route service
	 */
	@Autowired
	private RouteService routeService;

	/**
	 * The schedule service
	 */
	@Autowired
	private ScheduleService scheduleService;

	@Override
	public Iterable<Interconnection> findBy(String departure, String arrival, LocalDateTime departureDateTime,
			LocalDateTime arrivalDateTime) {

		List<Interconnection> results = new ArrayList<>();

		// find direct flights
		results.addAll(findFlights(departure, arrival, departureDateTime, arrivalDateTime)
				.stream()
				// map the results to an interconnection object
				.map(f1 -> new Interconnection(Collections.singletonList(f1)))
				// collect them to add to the results
				.collect(toList()));

		// find eligible routes
		Set<Route> eligibleRoutes = StreamSupport
				.stream(routeService.findAll().spliterator(), true)
				.filter(isEligibleRoute(departure, arrival))
				.collect(toSet());

		// find interconnecting flights
		eligibleRoutes
				.stream()
				// join routes in a list by connecting airport
				.flatMap(r1 -> eligibleRoutes
						.stream()
						.filter(r2 -> r1.getAirportTo().equals(r2.getAirportFrom()))
						// map them to a tuple
						.map(r2 -> new RouteTuple(r1, r2)))
				.forEach(routeTuple -> {
					// we keep this collection to avoid multiple calls to the schedule service
					// during cross join with flatMap
					Collection<Flight> connectingFlights = findFlights(routeTuple.getNextRoute(), departureDateTime,
							arrivalDateTime);

					results.addAll(findFlights(routeTuple.getStartingRoute(), departureDateTime, arrivalDateTime)
							.stream()
							// cross join flights from the first route with flights from the second
							.flatMap(f1 -> connectingFlights
									.stream()
									// use only valid connections
									.filter(isValidConnectionWith(f1))
									// map the results to an interconnection object
									.map(f2 -> new Interconnection(Arrays.asList(f1, f2))))
							// collect them to add to the results
							.collect(toList()));
				});

		return results;
	}

	/**
	 * Determines if a route is eligible to be used as part of a travel route
	 * between the given departure and arrival airports.
	 * 
	 * @param departure
	 *            the departure airport IATA code
	 * @param arrival
	 *            the arrival airport IATA code
	 * @return true if the route is eligible; false, otherwise
	 */
	static Predicate<? super Route> isEligibleRoute(String departure, String arrival) {
		// TODO: In the real data, there are routes with empty (not null) connecting
		// airports. Should those be included too?
		return route -> (route.getConnectingAirport() == null)
				&& (departure.equals(route.getAirportFrom()) || arrival.equals(route.getAirportTo()));
	}

	/**
	 * Determines if a flight departs not earlier than the given departure datetime
	 * and arrive not after the given arrival datetime.
	 * 
	 * @param departureDateTime
	 *            the departure date and time
	 * @param arrivalDateTime
	 *            the arrival date and time
	 * @return true if the flight is in the desired timeframe; false, otherwise.
	 */
	static Predicate<? super Flight> isInTimeframe(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
		return flight -> !flight.getDepartureDateTime().isBefore(departureDateTime)
				&& !flight.getArrivalDateTime().isAfter(arrivalDateTime);
	}

	/**
	 * Determines if a flight is a valid connection to another flight. Checks that
	 * the difference between the arrival and the departure is at least 2 hours.
	 * 
	 * @param flight
	 *            the starting flight
	 * @return true if the connection is valid; false, otherwise
	 */
	static Predicate<? super Flight> isValidConnectionWith(Flight flight) {
		return f2 -> f2.getDepartureDateTime().isAfter(flight.getArrivalDateTime().plusHours(2));
	}

	/**
	 * Finds all the flights that departs and arrives from the given airports, also
	 * that depart not earlier than the given departure datetime and arrive not
	 * after the given arrival datetime.
	 * 
	 * @param departure
	 *            the departure airport
	 * @param arrival
	 *            the arrival airport
	 * @param departureDateTime
	 *            the departure date and time
	 * @param arrivalDateTime
	 *            the arrival date and time
	 * @return a set of flights with the desired restrictions
	 */
	Collection<Flight> findFlights(String departure, String arrival, LocalDateTime departureDateTime,
			LocalDateTime arrivalDateTime) {
		List<Flight> result = new ArrayList<>();

		// the difference between departureDate and arrival date can span several months
		LocalDate processingDate = departureDateTime.toLocalDate();
		do {
			result
					.addAll(StreamSupport
							.stream(scheduleService
									.findFlights(departure, arrival, processingDate.getYear(),
											processingDate.getMonthValue())
									.spliterator(), true)
							.filter(isInTimeframe(departureDateTime, arrivalDateTime))
							.collect(toList()));
			processingDate = processingDate.plusMonths(1);
		} while ((processingDate.getYear() <= arrivalDateTime.getYear())
				&& (processingDate.getMonthValue() <= arrivalDateTime.getMonthValue()));

		return result;
	}

	/**
	 * Finds all the flights on the given route that depart not earlier than the
	 * given departure datetime and arrive not after the given arrival datetime.
	 * 
	 * @param route
	 *            the route
	 * @param departureDateTime
	 *            the departure date and time
	 * @param arrivalDateTime
	 *            the arrival date and time
	 * @return a set of flights with the desired restrictions
	 */
	Collection<Flight> findFlights(Route route, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
		return findFlights(route.getAirportFrom(), route.getAirportTo(), departureDateTime, arrivalDateTime);
	}

	/**
	 * Represents a tuple of routes.
	 * 
	 * <p>
	 * This is an inner class because it is only used here and is not relevant to
	 * the rest of the application.
	 * </p>
	 * 
	 * @author jnoda
	 */
	@Value
	private static class RouteTuple {
		/**
		 * The starting route.
		 */
		private Route startingRoute;

		/**
		 * The next route.
		 */
		private Route nextRoute;
	}
}
