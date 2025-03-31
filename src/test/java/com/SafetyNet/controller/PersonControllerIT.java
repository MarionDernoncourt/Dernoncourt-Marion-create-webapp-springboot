package com.SafetyNet.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.model.Person;
import com.SafetyNet.repository.DataLoaderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerIT {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private 		DataLoaderRepository dataLoaderRepository;


	@BeforeEach
	void setUp () {
		dataLoaderRepository.setFirestations(null);
		dataLoaderRepository.setMedicalRecords(null);
		dataLoaderRepository.setPersons(null);
	}
	@Test
	public void testGetAllPersons() throws Exception {
		mockMvc.perform(get("/persons")).andExpect(status().isOk()).andExpect(jsonPath("$[0].firstName", is("John")));
	}


	@Test
	public void testCreatePerson() throws Exception {
		Person newPerson = new Person("John", "Doe", "123 Main St", "city", 12345, "123-456-789", "john@example.com");
		String newPersonJson = objectMapper.writeValueAsString(newPerson);
		mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON).content(newPersonJson))
				.andExpect(status().isCreated());
	}

	@Test
	public void testUpdatePerson() throws Exception {
		Person updatedPerson = new Person("John", "Boyd", "123 Main St", "city", 12345, "123-456-789",
				"boyd@example.com");
		String updatedPersonJson = objectMapper.writeValueAsString(updatedPerson);
		mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON).content(updatedPersonJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.email", is("boyd@example.com")));
	}

	@Test
	public void testUpdatePersonwithNonExistantPerson() throws Exception {
		Person updatedPerson = new Person("Hermione", "Granger", "123 Main St", "city", 12345, "123-456-789",
				"boyd@example.com");
		String updatedPersonJson = objectMapper.writeValueAsString(updatedPerson);
		mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON).content(updatedPersonJson))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeletePerson() throws Exception {
		mockMvc.perform(delete("/person").param("firstName", "John").param("lastName", "Boyd"))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testDeletePersonwitNonExitantPerson() throws Exception {
		mockMvc.perform(delete("/person").param("firstName", "Ron").param("lastName", "Weasley"))
				.andExpect(status().isNotFound());
	}

}
