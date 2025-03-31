package com.SafetyNet.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportingControllerIT {

	@Autowired
	private MockMvc mockMvc;


	@Test
	public void testGetFirestationCoverage() throws Exception {
		mockMvc.perform(get("/firestation").param("stationNumber", "1")).andExpect(status().isOk());
	}
	
	@Test
	public void getChildInfoByAddress() throws Exception {
		mockMvc.perform(get("/childAlert").param("address", "1509 Culver St")).andExpect(status().isOk()).andExpect(jsonPath("$.coveredChildren").isArray());
	}
	
	@Test
	public void testGetPhoneNumber() throws Exception {
		mockMvc.perform(get("/phoneAlert").param("firestation", "2")).andExpect(status().isOk()).andExpect(jsonPath("$.phoneNumber").isArray());
	}
	
	@Test
	public void getResidentInfoCaseOfFire() throws Exception {
		mockMvc.perform(get("/fire").param("address", "112 Steppes Pl")).andExpect(status().isOk()).andExpect(jsonPath("$.residents.length()").value(3));
	}
}
