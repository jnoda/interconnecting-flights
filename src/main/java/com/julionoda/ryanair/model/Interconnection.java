package com.julionoda.ryanair.model;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Represents a flight interconnection.
 * 
 * @author jnoda
 *
 */
@Value
@RequiredArgsConstructor
public class Interconnection {
	/**
	 * Flight legs.
	 */
	@NonNull
	private List<Flight> legs;
	
	/**
	 * Number of stops.
	 */
	public int getStops() {
		return legs.size() - 1;
	}
}
