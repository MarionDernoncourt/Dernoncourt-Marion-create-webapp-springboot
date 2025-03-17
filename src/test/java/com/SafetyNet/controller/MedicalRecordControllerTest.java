package com.SafetyNet.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.service.MedicalRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MedicalRecordService medicalRecordService;



	private ObjectMapper objectMapper = new ObjectMapper();

	List<MedicalRecord> mockMedRecord = new ArrayList<MedicalRecord>();

	@BeforeEach
	void setUp() {
		mockMedRecord = List.of(
				new MedicalRecord("John", "Boyd",
						LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy")),
						List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan")),
				new MedicalRecord("Jacob", "Boyd",
						LocalDate.parse("03/06/1989", DateTimeFormatter.ofPattern("MM/dd/yyyy")),
						List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), List.of()));

		objectMapper.registerModule(new JavaTimeModule());
	}

	@Test
	public void testGetAllMedicalRecords() throws Exception {
		when(medicalRecordService.getAllMedicalRecord()).thenReturn(mockMedRecord);
		mockMvc.perform(get("/medicalrecords")).andExpect(status().isOk());
	}

	@Test
	public void testGetAllMedicalRecordsFromEmptyList() throws Exception {
		when(medicalRecordService.getAllMedicalRecord()).thenReturn(List.of());
		mockMvc.perform(get("/medicalrecords")).andExpect(status().isNotFound());
	}

	@Test
	public void testGetMedicalRecordByFirstnameAndLastname() throws Exception {
		when(medicalRecordService.getMedicalRecord(anyString(), anyString())).thenReturn(mockMedRecord.get(0));
		mockMvc.perform(get("/medicalrecord").param("firstName", "John").param("lastName", "Boyd"))
				.andExpect(status().isOk());

	}

	@Test
	public void testGetMedicalRecordByFirstnameAndLastName_withWrongArgument() throws Exception {
		when(medicalRecordService.getMedicalRecord(anyString(), anyString())).thenReturn(null);
		mockMvc.perform(get("/medicalrecord").param("firstName", "Jane").param("lastName", "Boyd"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCreateMedicalRecord() throws Exception {
		MedicalRecord newMedRecord = new MedicalRecord("Harry", "Potter",
				LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				List.of("hydrapermazol:100mg"), List.of("nillacilan"));
		String newMedRecordJson = objectMapper.writeValueAsString(newMedRecord);
		when(medicalRecordService.createMedicalRecord(newMedRecord)).thenReturn(newMedRecord);
		mockMvc.perform(post("/medicalrecord").contentType(MediaType.APPLICATION_JSON).content(newMedRecordJson))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.firstName", is("Harry")));
	}

	@Test
	public void testUpdateMedicalRecord() throws Exception {
		MedicalRecord medRecordToUpdate = new MedicalRecord("John", "Boyd",
				LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy")), List.of("doliprane 1000mg"),
				List.of("nillacilan"));
		String medRecordToUpdateJson = objectMapper.writeValueAsString(medRecordToUpdate);
		when(medicalRecordService.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(medRecordToUpdate);
		mockMvc.perform(put("/medicalrecord").contentType(MediaType.APPLICATION_JSON).content(medRecordToUpdateJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.medications[0]").value("doliprane 1000mg"));
	}

	@Test
	public void testUpdateMedicalRecordwithWrongArgument() throws Exception {
		MedicalRecord medRecordToUpdate = new MedicalRecord("Hermione", "Granger",
				LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy")), List.of("doliprane 1000mg"),
				List.of("nillacilan"));
		String medRecordToUpdateJson = objectMapper.writeValueAsString(medRecordToUpdate);
		when(medicalRecordService.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(null);
		mockMvc.perform(put("/medicalrecord").contentType(MediaType.APPLICATION_JSON).content(medRecordToUpdateJson))
				.andExpect(status().isNotFound());
		;
	}

	@Test
	public void testDeleteMedicalRecord() throws Exception {
		when(medicalRecordService.deleteMedicalRecord("John", "Boyd")).thenReturn(true);
		mockMvc.perform(delete("/medicalrecord").param("firstName", "John").param("lastName", "Boyd")).andExpect(status().isNoContent());
	}
	
	@Test
	public void testDeleteMedicalRecordwithWrongArgument() throws Exception {
		when(medicalRecordService.deleteMedicalRecord("John", "Boyd")).thenReturn(false);
		mockMvc.perform(delete("/medicalrecord").param("firstName", "John").param("lastName", "Boyd")).andExpect(status().isNotFound());
	}
}
