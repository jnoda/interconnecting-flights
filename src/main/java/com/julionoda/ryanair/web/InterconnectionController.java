package com.julionoda.ryanair.web;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julionoda.ryanair.model.Interconnection;
import com.julionoda.ryanair.service.InterconnectionService;

import lombok.Value;

/**
 * REST controller for interconnecting flights.
 * 
 * @author jnoda
 *
 */
@RestController
public class InterconnectionController {
	@Autowired
	private InterconnectionService interconnectionService;

	/**
	 * Finds interconnected flights with the given restrictions.
	 * 
	 * @param form
	 *            the restrictions for the flights
	 * @return an iterable of interconnected flights with the given restrictions.
	 */
	@GetMapping("interconnections")
	public Iterable<Interconnection> findBy(@Valid InterconnectionsQueryForm form) {
		return interconnectionService.findBy(form.getDeparture(), form.getArrival(), form.getDepartureDateTime(),
				form.getArrivalDateTime());
	}

	/**
	 * Holds the restrictions used to find interconnecting flights.
	 * 
	 * @author jnoda
	 *
	 */
	@Value
	static class InterconnectionsQueryForm {
		/**
		 * Desired departure airport IATA code.
		 */
		@NotNull
		@Pattern(regexp = "^[A-Z]{3}$")
		private String departure;

		/**
		 * Desired arrival airport IATA code.
		 */
		@NotNull
		@Pattern(regexp = "^[A-Z]{3}$")
		private String arrival;

		/**
		 * Desired minimum date and time of departure, on the departure airport
		 * timezone.
		 */
		@NotNull
		private LocalDateTime departureDateTime;

		/**
		 * Desired maximum date and time of arrival, on the arrival airport timezone.
		 */
		@NotNull
		private LocalDateTime arrivalDateTime;
	}
}
