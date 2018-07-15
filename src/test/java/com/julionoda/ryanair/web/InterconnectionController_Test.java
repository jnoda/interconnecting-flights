package com.julionoda.ryanair.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.julionoda.ryanair.model.Flight;
import com.julionoda.ryanair.model.Interconnection;
import com.julionoda.ryanair.service.InterconnectionService;

/**
 * Unit test suite for {@link InterconnectionController}.
 * 
 * @author jnoda
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(InterconnectionController.class)
public class InterconnectionController_Test {

	@Autowired
	private MockMvc mvc;

	/**
	 * Mocked interconnection service.
	 */
	@MockBean
	private InterconnectionService interconnectionService;

	/**
	 * Gets a valid map to be used as parameters for
	 * {@link InterconnectionController#findBy(com.julionoda.ryanair.web.InterconnectionController.InterconnectionsQueryForm)}.
	 */
	private MultiValueMap<String, String> getFindByTestParams() {
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("departure", "DUB");
		paramMap.set("arrival", "MAD");
		paramMap.set("departureDateTime", "2018-07-15T01:00");
		paramMap.set("arrivalDateTime", "2018-07-15T16:00");
		return paramMap;
	}

	/**
	 * Tests that the interconnections endpoint provides at least a correct
	 * interconnection.
	 */
	@Test
	public void findBy_Succeeded() throws Exception {
		MultiValueMap<String, String> params = getFindByTestParams();

		given(this.interconnectionService.findBy(params.getFirst("departure"), params.getFirst("arrival"),
				LocalDateTime.parse(params.getFirst("departureDateTime")),
				LocalDateTime.parse(params.getFirst("arrivalDateTime")))).willReturn(
						Collections.singleton(new Interconnection(Collections.singletonList(new Flight("1234", "DUB",
								"MAD", LocalDateTime.now(), LocalDateTime.now().plusHours(3))))));

		this.mvc
				.perform(get("/interconnections").params(params).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				// only need to test one property to verify the object is returned, actual
				// verification of the deserialization will be done on the corresponding {link
				// Interconnection_JsonTest}
				.andExpect(jsonPath("$[0].stops").value(0));
	}

	/**
	 * Tests that the interconnections endpoint shows a validation error if the
	 * departure parameter is not provided.
	 */
	@Test
	public void findBy_ReturnValidationError_IfNoDeparture() throws Exception {
		MultiValueMap<String, String> params = getFindByTestParams();
		params.remove("departure");

		this.mvc.perform(get("/interconnections").params(params).accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isBadRequest());
	}

	/**
	 * Tests that the interconnections endpoint shows a validation error if the
	 * arrival parameter is not provided.
	 */
	@Test
	public void findBy_ReturnValidationError_IfNoArrival() throws Exception {
		MultiValueMap<String, String> params = getFindByTestParams();
		params.remove("arrival");

		this.mvc.perform(get("/interconnections").params(params).accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isBadRequest());
	}

	/**
	 * Tests that the interconnections endpoint shows a validation error if the
	 * departureDateTime parameter is not provided.
	 */
	@Test
	public void findBy_ReturnValidationError_IfNoDepartureDateTime() throws Exception {
		MultiValueMap<String, String> params = getFindByTestParams();
		params.remove("departureDateTime");

		this.mvc.perform(get("/interconnections").params(params).accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isBadRequest());
	}

	/**
	 * Tests that the interconnections endpoint shows a validation error if the
	 * arrivalDateTime parameter is not provided.
	 */
	@Test
	public void findBy_ReturnValidationError_IfNoArrivalDateTime() throws Exception {
		MultiValueMap<String, String> params = getFindByTestParams();
		params.remove("arrivalDateTime");

		this.mvc.perform(get("/interconnections").params(params).accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isBadRequest());
	}

	/**
	 * Tests that the interconnections endpoint shows a validation error if the
	 * departure parameter is not an IATA code.
	 * 
	 * TODO: More tests like this can be done to try multiple combinations.
	 */
	@Test
	public void findBy_ReturnValidationError_IfDepartureNotIataCode() throws Exception {
		MultiValueMap<String, String> params = getFindByTestParams();
		params.set("departure", "n12");

		this.mvc.perform(get("/interconnections").params(params).accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isBadRequest());
	}

	/**
	 * Tests that the interconnections endpoint shows a validation error if the
	 * arrival parameter is not an IATA code.
	 * 
	 * TODO: More tests like this can be done to try multiple combinations.
	 */
	@Test
	public void findBy_ReturnValidationError_IfArrivalNoIataCode() throws Exception {
		MultiValueMap<String, String> params = getFindByTestParams();
		params.set("arrival", "MAD1");

		this.mvc.perform(get("/interconnections").params(params).accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isBadRequest());
	}
}
