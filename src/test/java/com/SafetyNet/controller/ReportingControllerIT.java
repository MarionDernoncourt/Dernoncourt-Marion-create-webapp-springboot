package com.SafetyNet.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.repository.DataLoaderRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportingControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private 		DataLoaderRepository dataLoaderRepository;

	@BeforeEach
	void setUp () {
		dataLoaderRepository.setFirestations(null);
		dataLoaderRepository.setMedicalRecords(null);
		dataLoaderRepository.setPersons(null);
	}

	@Test
	public void testGetResidentCoveredByFirestation() throws Exception {
		mockMvc.perform(get("/firestation").param("stationNumber", "1")).andExpect(status().isOk()).andExpect(jsonPath("$.numberOfAdult").value(5));
	}
	
	@Test
	public void testGetChildrenInfoByAddress() throws Exception {
		mockMvc.perform(get("/childAlert").param("address", "1509 Culver St")).andExpect(status().isOk()).andExpect(jsonPath("$.coveredChildren.length()").value(2));
	}
	
	@Test
	public void testGetPhoneNumberByFirestation() throws Exception {
		mockMvc.perform(get("/phoneAlert").param("firestation", "2")).andExpect(status().isOk()).andExpect(jsonPath("$.phoneNumber.length()").value(4));
	}
	
	@Test
	public void testGetFireInfoByAddress() throws Exception {
		mockMvc.perform(get("/fire").param("address", "112 Steppes Pl")).andExpect(status().isOk()).andExpect(jsonPath("$.residents.length()").value(3));
	}
	
	@Test
	public void testGetFloodInfoByStation() throws Exception {
		mockMvc.perform(get("/flood").param("stations", "1,2")).andExpect(status().isOk()).andExpect(jsonPath("$[*].residents[0].lastName").value(hasItem("Duncan")));
	}
	
	@Test
	public void testGetResidentInfoByLastName() throws Exception {
		mockMvc.perform(get("/personInfolastName={lastName}", "Boyd")).andExpect(status().isOk()).andExpect(jsonPath("$.residents.length()").value(6)).andExpect(jsonPath("$.residents[0].lastName").value("Boyd"));
	}
	
	@Test
	public void testGetEmailInfoByCity() throws Exception {
		mockMvc.perform(get("/communityEmail").param("city", "Culver")).andExpect(status().isOk()).andExpect(jsonPath("$.emails.length()").value(23));
		
	}
}
