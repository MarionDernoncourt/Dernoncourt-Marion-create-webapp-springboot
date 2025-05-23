package com.SafetyNet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
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

import com.SafetyNet.model.Person;
import com.SafetyNet.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

	@Autowired
	private MockMvc mockMvc; // permet d'appeler perform qui déclenche la requete

	@MockBean
	private PersonService personService;

	List<Person> mockPersons = new ArrayList<Person>();

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setUp() {
		mockPersons = List.of(
				new Person("John", "Doe", "123 Main St", "city", 12345, "123-456-789", "john@example.com"),
				new Person("Jane", "Doe", "456 North St", "city", 98765, "987-654-321", "jane@example.com"));

	}

	@Test
	public void testGetAllPersons() throws Exception {
		when(personService.getAllPersons()).thenReturn(mockPersons);
		MvcResult result = mockMvc.perform(get("/persons")).andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		assertEquals(content,
				"[{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"123 Main St\",\"city\":\"city\",\"zip\":12345,\"phone\":\"123-456-789\",\"email\":\"john@example.com\"},{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"address\":\"456 North St\",\"city\":\"city\",\"zip\":98765,\"phone\":\"987-654-321\",\"email\":\"jane@example.com\"}]");
	}

	@Test
	public void testGetAllPersonsEmpty() throws Exception {
		when(personService.getAllPersons()).thenReturn(List.of());
		MvcResult result = mockMvc.perform(get("/persons")).andExpect(status().isNotFound()).andReturn();
		String content = result.getResponse().getContentAsString();
		assertEquals(content, "");
	}

	@Test
	public void testGetAllPerson_ShouldReturnInternalServerError_WhenExceptionIsThrown() throws Exception {
		when(personService.getAllPersons()).thenThrow(new RuntimeException("Erreur Simulée"));
		mockMvc.perform(get("/persons")).andExpect(status().isInternalServerError());
	}

	@Test
	public void testCreatePerson() throws Exception {
		Person person = new Person("Harry", "Potter", "12 Pourdlard St", "London", 12345, "123-456-987",
				"potter@email.com");
		String personJson = objectMapper.writeValueAsString(person);

		when(personService.createPerson(any(Person.class))).thenReturn(person);

		mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON).content(personJson))
				.andExpect(status().isCreated());
	}

	@Test
	public void testCreatePerson_ShouldReturnInternalServerError_WhenExceptionIsThrown() throws Exception {
		Person person = new Person("Harry", "Potter", "12 Pourdlard St", "London", 12345, "123-456-987",
				"potter@email.com");
		String personJson = objectMapper.writeValueAsString(person);

		when(personService.createPerson(any(Person.class))).thenThrow(new RuntimeException("Erreur simulée"));

		mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON).content(personJson))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testUpdatePerson() throws Exception {
		Person updatedPerson = new Person("John", "Doe", "321 Updated St", "city", 12345, "123-456-789",
				"john@example.com");
		String updatedPersonJson = objectMapper.writeValueAsString(updatedPerson);
		when(personService.updatePerson(any(Person.class))).thenReturn(updatedPerson);

		MvcResult result = mockMvc
				.perform(put("/person").contentType(MediaType.APPLICATION_JSON).content(updatedPersonJson)).andReturn();

		assertEquals(200, result.getResponse().getStatus());
		assertEquals(updatedPersonJson, result.getResponse().getContentAsString());
	}

	@Test
	public void testUpdatePersonwithWrongArgument() throws Exception {
		Person updatedPerson = new Person("John", "Wayne", "321 Updated St", "city", 12345, "123-456-789",
				"john@example.com");
		String updatedPersonJson = objectMapper.writeValueAsString(updatedPerson);
		when(personService.updatePerson(any(Person.class))).thenReturn(null);

		MvcResult result = mockMvc
				.perform(put("/person").contentType(MediaType.APPLICATION_JSON).content(updatedPersonJson)).andReturn();

		assertEquals(404, result.getResponse().getStatus());
	}

	@Test
	public void testUpdatePerson_ShouldReturnInternalServerError_WhenExceptionIsThrown() throws Exception {
		Person updatedPerson = new Person("John", "Doe", "321 Updated St", "city", 12345, "123-456-789",
				"john@example.com");
		String updatedPersonJson = objectMapper.writeValueAsString(updatedPerson);
		when(personService.updatePerson(any(Person.class))).thenThrow(new RuntimeException("Erreur simulée"));

		mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON).content(updatedPersonJson))
				.andExpect(status().isInternalServerError());

	}

	@Test
	public void testDeletePerson() throws Exception {
		when(personService.deletePerson("John", "Doe")).thenReturn(true);
		mockMvc.perform(delete("/person").param("firstName", "John").param("lastName", "Doe"))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testDeletePersonwithWrongArgument() throws Exception {
		when(personService.deletePerson("John", "Doe")).thenReturn(false);
		mockMvc.perform(delete("/person").param("firstName", "John").param("lastName", "Doe"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeletePerson_ShouldReturnInternalServerError_WhenExceptionIsThrown() throws Exception {
		when(personService.deletePerson("John", "Doe")).thenThrow(new RuntimeException("Erreur simulée"));
		mockMvc.perform(delete("/person").param("firstName", "John").param("lastName", "Doe"))
				.andExpect(status().isInternalServerError());
	}

}
