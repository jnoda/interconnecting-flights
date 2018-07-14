package com.julionoda.ryanair.service;

import com.julionoda.ryanair.model.Route;

/**
 * Service for routes.
 * 
 * @author jnoda
 *
 */
public interface RouteService {
	/**
	 * Finds all available routes.
	 * 
	 * @return an iterable of routes
	 */
	Iterable<Route> findAll();
}
