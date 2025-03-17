package com.SafetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.SafetyNet.model.MedicalRecord;

@SpringBootTest
public class MedicalRecordServiceTest {

	@Autowired
	private MedicalRecordService medRecordService;

	@Test
	public void testGetAllMedicalRecords() throws IOException {
		List<MedicalRecord> medRecords = medRecordService.getAllMedicalRecord();
		assertTrue(!medRecords.isEmpty());
	}

	@Test
	public void testGetMedicalRecordByFirstNameAndLastName() throws IOException {
		MedicalRecord medRecord = medRecordService.getMedicalRecord("Jacob", "Boyd");
		assertEquals(LocalDate.of(1989, 3, 6), medRecord.getBirthdate());
	}

	@Test
	public void testCreateMedRecord() throws IOException {
		int sizeBefore = medRecordService.getAllMedicalRecord().size();
		medRecordService.createMedicalRecord(new MedicalRecord("Harry", "Potter",
				LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				List.of("hydrapermazol:100mg"), List.of("nillacilan")));
		int sizeAfter = medRecordService.getAllMedicalRecord().size();
		MedicalRecord created = medRecordService.getMedicalRecord("Harry", "Potter");
		assertEquals(sizeBefore + 1, sizeAfter);
		assertEquals("Harry", created.getFirstName());
		assertEquals("Potter", created.getLastName());
	}

	@Test
	public void testUpdateMedRecord() throws IOException {
		MedicalRecord medRecordToUpdate = medRecordService.getMedicalRecord("Jacob", "Boyd");
		medRecordToUpdate.setAllergies(List.of("nillacilan"));
		medRecordService.updateMedicalRecord(medRecordToUpdate);
		MedicalRecord medRecordUpdated = medRecordService.getMedicalRecord("Jacob", "Boyd");
		assertEquals(List.of("nillacilan"), medRecordUpdated.getAllergies());
	}

	@Test
	public void testDeleteMedRecord() throws IOException {
		boolean medRecordRemoved = medRecordService.deleteMedicalRecord("Clive", "Ferguson");
		assertTrue(medRecordRemoved);
		assertNull(medRecordService.getMedicalRecord("Clive", "Ferguson"));
	}

}
