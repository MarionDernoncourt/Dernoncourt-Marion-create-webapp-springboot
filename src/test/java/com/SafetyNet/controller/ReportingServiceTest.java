package com.SafetyNet.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.service.ReportingService;

@WebMvcTest(ReportingController.class)
public class ReportingServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ReportingService reportingService;

	@Test
	public void testGetResidentCoveredByFirestation() throws Exception {
		when(reportingService.getResidentCoveredByFirestation(anyInt()))
				.thenThrow(new RuntimeException("Erreur simulée"));
		mockMvc.perform(get("/firestation").param("stationNumber", "2")).andExpect(status().isInternalServerError());
	}

	@Test
	public void testGetChildrenInfoByAddress() throws Exception {
		when(reportingService.getChildrenInfoByAddress(anyString())).thenThrow(new RuntimeException("Erreur simulée"));
		mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testGetPhoneNumberByFirestation() throws Exception {
		when(reportingService.getPhoneNumberByFirestation(anyInt())).thenThrow(new RuntimeException("Erreur Simulée"));
		mockMvc.perform(get("/phoneAlert").param("firestation", "2")).andExpect(status().isInternalServerError());
	}

	@Test
	public void testGetFireInfoByAddress() throws Exception {
		when(reportingService.getFireInfoByAddress(anyString())).thenThrow(new RuntimeException("Erreur simulée"));
		mockMvc.perform(get("/fire").param("address", "1509 Culver St")).andExpect(status().isInternalServerError());
	}

	@Test
	public void testGetFloodInfobyStation() throws Exception {
		when(reportingService.getFloodInfobyStation(anyList())).thenThrow(new RuntimeException("Erreur simulée"));
		mockMvc.perform(get("/flood/stations").param("stations", "1", "2")).andExpect(status().isInternalServerError());
	}

	@Test
	public void testGetResidentInfoByLastName() throws Exception {
		when(reportingService.getResidentInfoByLastName(anyString())).thenThrow(new RuntimeException("Erreur simulée"));
		mockMvc.perform(get("/personInfolastName=Boyd")).andExpect(status().isInternalServerError());
	}

	@Test
	public void testGetEmailByCity() throws Exception {
		when(reportingService.getEmailByCity(anyString())).thenThrow(new RuntimeException("Erreur simulée"));
		mockMvc.perform(get("/communityEmail").param("city", "Culver")).andExpect(status().isInternalServerError());
	}

}