package com.SafetyNet.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.model.Firestation;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class FirestationControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetAllFirestation() throws Exception {
		mockMvc.perform(get("/firestations")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].address", is("1509 Culver St")));
	}
	
	@Test
	public void testGetFirestationByAddress() throws Exception {
		mockMvc.perform(get("/firestation").param("address", "1509 Culver St").param("station", "3")).andExpect(status().isOk()).andExpect(jsonPath("$.address", is("1509 Culver St")));
	}
	
	@Test 
	public void testGetFirestationwithWrongAddress() throws Exception {
		mockMvc.perform(get("/firestation").param("address", "123 Main St").param("station", "1")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testCreateFirestation () throws Exception {
		String newFirestation = objectMapper.writeValueAsString(new Firestation("18 bomberos St", 2));
		mockMvc.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON).content(newFirestation)).andExpect(status().isCreated());
	}
	
	@Test
	public void testUpdateFirestation() throws Exception {
		String updatedFirestation = objectMapper.writeValueAsString(new Firestation("1509 Culver St", 1));
		mockMvc.perform(put("/firestation").contentType(MediaType.APPLICATION_JSON).content(updatedFirestation)).andExpect(status().isOk()).andExpect(jsonPath("$.station", is(1)));
	}
	
	@Test 
	public void testUpdateFirestationwithNonExistantStation() throws Exception {
		String updatedFirestation = objectMapper.writeValueAsString(new Firestation("644 Gershwin Cir", 1));
		mockMvc.perform(put("/fierstation").contentType(MediaType.APPLICATION_JSON).content(updatedFirestation)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testDeleteFirestation() throws Exception {
		String firestationToDelete = objectMapper.writeValueAsString(new Firestation("748 Townings Dr", 3));
		mockMvc.perform(delete("/firestation").contentType(MediaType.APPLICATION_JSON).content(firestationToDelete)).andExpect(status().isNoContent());
	}
	
	@Test
	public void testDeleteFirestationwithNonExistantArgument() throws Exception {
		String firestationToDelete = objectMapper.writeValueAsString(new Firestation("159 Culver St", 1));
		mockMvc.perform(delete("/firestation").contentType(MediaType.APPLICATION_JSON).content(firestationToDelete)).andExpect(status().isNotFound());
	}
	
	
}
