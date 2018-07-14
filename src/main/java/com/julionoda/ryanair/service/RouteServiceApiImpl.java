package com.julionoda.ryanair.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.julionoda.ryanair.model.Route;

/**
 * Implementation of {@link RouteService} that consumes the data from an
 * external routes API.
 * 
 * @author jnoda
 *
 */
@Service
public class RouteServiceApiImpl implements RouteService {
	/**
	 * Endpoint of the routes API.
	 */
	@Value("${routesApiEndpoint}")
	private String routesApiEndpoint;

	/**
	 * REST template to query the external API.
	 */
	private final RestTemplate restTemplate;

	/**
	 * Class constructor.
	 */
	public RouteServiceApiImpl() {
		// We might have used a global RestTemplate or RestTemplateBuilder, but that
		// would add some complexity to integration tests; and don't seem needed for
		// this simple scenario.
		this.restTemplate = new RestTemplate();
	}

	@Cacheable("routes")
	@Override
	public Iterable<Route> findAll() {
		ResponseEntity<Iterable<Route>> response = restTemplate.exchange(routesApiEndpoint, HttpMethod.GET, null,
				new ParameterizedTypeReference<Iterable<Route>>() {
				});
		return response.getBody();
	}
}
