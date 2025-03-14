package com.SafetyNet.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
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
import org.springframework.test.web.servlet.MockMvc;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.service.MedicalRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MedicalRecordService medicalRecordService;

	@InjectMocks
	private MedicalRecordController medicalRecordController;

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
	}
	
	@Test
	public void testGetAllMedicalRecords() throws Exception {
		when(medicalRecordService.getAllMedicalRecord()).thenReturn(mockMedRecord);
		mockMvc.perform(get("/medicalrecords")).andExpect(status().isOk());
	}
	
	@Test 
	public void testGetAllMedicalRecords_FromEmptyList() throws Exception {
		when(medicalRecordService.getAllMedicalRecord()).thenReturn(List.of());
		mockMvc.perform(get("/medicalrecords")).andExpect(status().isNotFound());
	}
	
	@Test
	public void getMedicalRecord_ByFirstnameAndLastname() throws Exception {
		when(medicalRecordService.getMedicalRecord(anyString(), anyString())).thenReturn(mockMedRecord.get(0));
		mockMvc.perform(get("/medicalrecord").param("firstName", "John").param("lastName", "Boyd")).andExpect(status().isOk());
	}
	
	@Test
	public void getMedicalRecord_ByFirstnameAndLastName_withWrongArgument() throws Exception {
		when(medicalRecordService.getMedicalRecord(anyString(), anyString())).thenReturn(null);
		mockMvc.perform(get("/medicalrecord").param("firstName", "Jane").param("lastName", "Boyd")).andExpect(status().isNotFound());
	}

}
