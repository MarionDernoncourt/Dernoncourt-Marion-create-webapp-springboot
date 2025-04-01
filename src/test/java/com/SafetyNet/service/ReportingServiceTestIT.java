package com.SafetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.SafetyNet.repository.DataLoaderRepository;

import dto.ChildrendInfoDTO;
import dto.FireResidentInfoDTO;
import dto.FirestationCoverageDTO;
import dto.FloodHouseholdInfoDTO;
import dto.PhoneNumberDTO;
import dto.ResidentInfoLByLastNameDTO;

@SpringBootTest
class ReportingServiceTestIT {

	@Autowired
	private ReportingService reportingService;
	@Autowired
	private 		DataLoaderRepository dataLoaderRepository;

	@BeforeEach
	void setUp () {
		dataLoaderRepository.setFirestations(null);
		dataLoaderRepository.setMedicalRecords(null);
		dataLoaderRepository.setPersons(null);
	}
	@Test
	public void testGetResidentCoveredByFirestation() {
		FirestationCoverageDTO coveredPersons = reportingService.getResidentCoveredByFirestation(2);
		assertTrue(coveredPersons.getCoveredResident().size() > 0);
		assertTrue(coveredPersons.getNumberOfAdult() > 0);
	}

	@Test
	public void testGetChildrenInfoByAddress() {
		ChildrendInfoDTO coveredChildren = reportingService.getChildrenInfoByAddress("1509 Culver St");
		assertEquals(2, coveredChildren.getCoveredChildren().size());
	}

	@Test
	public void testGetPhoneNumberByFirestation() {
		PhoneNumberDTO phoneNumber = reportingService.getPhoneNumberByFirestation(2);
		assertFalse((phoneNumber.getPhoneNumber().isEmpty()));
	}

	@Test
	public void testGetFireInfoByAddress() {
		FireResidentInfoDTO residentInfo = reportingService.getFireInfoByAddress("1509 Culver St");
		assertFalse(residentInfo.getResidents().isEmpty());
		assertEquals(List.of(3), residentInfo.getStationNumber());
	}
	
	@Test
	public void testGetFloodInfoByStation() {
		List<FloodHouseholdInfoDTO> householdsInfo = reportingService.getFloodInfobyStation(List.of(1,4));
		assertFalse(householdsInfo.isEmpty());
	}
	
	@Test
	public void testGetResidentInfoByLastName() {
		ResidentInfoLByLastNameDTO residents = reportingService.getResidentInfoByLastName("Cadigan");
		assertNotNull(residents);
	}
}
