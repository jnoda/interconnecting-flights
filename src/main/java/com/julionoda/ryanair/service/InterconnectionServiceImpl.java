package com.julionoda.ryanair.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.julionoda.ryanair.model.Interconnection;

/**
 * Implementation of {@link InterconnectionService}.
 * 
 * @author jnoda
 *
 */
@Service
public class InterconnectionServiceImpl implements InterconnectionService {

	@Override
	public Iterable<Interconnection> findBy(String departure, String arrival, LocalDateTime departureDateTime,
			LocalDateTime arrivalDateTime) {
		// TODO Auto-generated method stub
		return null;
	}
}
