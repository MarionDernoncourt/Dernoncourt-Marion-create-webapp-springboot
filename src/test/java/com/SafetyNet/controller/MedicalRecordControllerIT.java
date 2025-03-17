package com.SafetyNet.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.model.MedicalRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetAllMedicalRecords() throws Exception {
		mockMvc.perform(get("/medicalrecords")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName", is("John")));
	}

	@Test
	public void testGetMedicalRecordByFirstNameAndLastName() throws Exception {
		mockMvc.perform(get("/medicalrecord").param("firstName", "Jacob").param("lastName", "Boyd"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.firstName", is("Jacob")));
	}

	@Test
	public void testGetMedicalRecordwithNonExistantFirstNameAndLastName() throws Exception {
		mockMvc.perform(get("/medicalrecord").param("firstName", "Hermione").param("lastName", "Granger"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCreateMedicalRecord() throws Exception {
		MedicalRecord newMedRecord = new MedicalRecord("John", "Boyd",
				LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan"));
		String newMedRecordJson = objectMapper.writeValueAsString(newMedRecord);
		mockMvc.perform(post("/medicalrecord").contentType(MediaType.APPLICATION_JSON).content(newMedRecordJson))
				.andExpect(status().isCreated());
	}

	@Test
	public void testUpdateMedicalRecord() throws Exception {
		MedicalRecord updatedMedRecord = new MedicalRecord("Roger", "Boyd",
				LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy")), List.of("aznol:350mg"),
				List.of("nillacilan"));
		String updatedMedRecordJson = objectMapper.writeValueAsString(updatedMedRecord);
		mockMvc.perform(put("/medicalrecord").contentType(MediaType.APPLICATION_JSON).content(updatedMedRecordJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.medications[0]", is("aznol:350mg")));
	}

	@Test
	public void testUpdateMedicalRecordwithWrongFirstNameAndLastName() throws Exception {
		MedicalRecord updatedMedRecord = new MedicalRecord("Betty", "Boop",
				LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy")), List.of("aznol:350mg"),
				List.of("nillacilan"));
		String updatedMedRecordJson = objectMapper.writeValueAsString(updatedMedRecord);
		mockMvc.perform(put("/medicalrecord").contentType(MediaType.APPLICATION_JSON).content(updatedMedRecordJson))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void testDeleteMedicalRecord() throws Exception {
		mockMvc.perform(delete("/medicalrecord").param("firstName", "Eric").param("lastName", "Cadigan")).andExpect(status().isNoContent());
	}
	
	@Test
	public void testDeleteMedicalRecordwithWrongFirstNameAndLastName() throws Exception {
		mockMvc.perform(delete("/medicalrecord").param("firstName", "Albus").param("lastName", "Dumbledore")).andExpect(status().isNotFound());
	}

}
