package com.SafetyNet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.service.FirestationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(FirestationController.class)
public class FirestationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirestationService firestationService;

	@InjectMocks
	private FirestationController firestationController;

	private ObjectMapper objectMapper = new ObjectMapper();

	List<Firestation> mockFirestation = new ArrayList<Firestation>();

	@BeforeEach
	void setUp() {
		mockFirestation = List.of(new Firestation("1509 Culver St", 3), new Firestation("29 15th St", 2));
	}

	@Test
	public void testGetAllFirestation() throws Exception {
		when(firestationService.getAllFirestation()).thenReturn(mockFirestation);
		mockMvc.perform(get("/firestations")).andExpect(status().isOk());
	}

	@Test
	public void testGetFirestationByAddress() throws Exception {
		String foundedFirestationInJson = objectMapper.writeValueAsString(mockFirestation.get(0));
		when(firestationService.getFirestation_ByAddress(anyString())).thenReturn(mockFirestation.get(0));
		MvcResult result = mockMvc.perform(get("/firestation").param("address", "1509 Culver St")).andReturn();
		assertEquals(foundedFirestationInJson, result.getResponse().getContentAsString());
	}

	@Test
	public void testGetFirestation_withWrongAddress() throws Exception {
		when(firestationService.getFirestation_ByAddress(anyString())).thenReturn(null);
		mockMvc.perform(get("/firestation").param("address", "123 North St")).andExpect(status().isNotFound());
	}

	@Test
	public void testCreateFirestation() throws Exception {
		Firestation newStation = new Firestation("123 North St", 3);
		String newStationJson = objectMapper.writeValueAsString(newStation);
		when(firestationService.createFirestation(any(Firestation.class))).thenReturn(newStation);
		mockMvc.perform(post("/firestation")
	            .contentType(MediaType.APPLICATION_JSON)  
	            .content(newStationJson))  
	            .andExpect(status().isCreated());
	}
	
	@Test
	public void testUpdateFirestation() throws Exception {
		Firestation updatedFirestation = new Firestation("18 Bomberos St", 1);
		String updatedFirestationJson = objectMapper.writeValueAsString(updatedFirestation);
		when(firestationService.updateFirestation(any(Firestation.class))).thenReturn(updatedFirestation);
		mockMvc.perform(put("/firestation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedFirestationJson))
			.andExpect(status().isOk());
	}
	
	@Test
	public void testUpdateFirestation_withWrongArgument() throws Exception {
		Firestation updatedFirestation = new Firestation("18 Bomberos St", 1);
		String updatedFirestationJson = objectMapper.writeValueAsString(updatedFirestation);
		when(firestationService.updateFirestation(any(Firestation.class))).thenReturn(null);
		mockMvc.perform(put("/firestation").contentType(MediaType.APPLICATION_JSON).content(updatedFirestationJson)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testDeleteFirestation() throws Exception {
		Firestation stationToDelete = mockFirestation.get(0);
		when(firestationService.deleteFirestation(anyString())).thenReturn(true);
		mockMvc.perform(delete("/firestation").param("address", stationToDelete.getAddress())).andExpect(status().isNoContent());
	}
	
	@Test
	public void testDeleteFirestation_withWrongArgument() throws Exception {
		Firestation stationToDelete = mockFirestation.get(0);
		when(firestationService.deleteFirestation(anyString())).thenReturn(false);
		mockMvc.perform(delete("/firestation").param("address", stationToDelete.getAddress())).andExpect(status().isNotFound());
	
	}
}
