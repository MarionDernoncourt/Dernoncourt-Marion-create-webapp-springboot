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

import com.SafetyNet.dto.ChildrendInfoDTO;
import com.SafetyNet.dto.EmailInfoDTO;
import com.SafetyNet.dto.FireResidentInfoDTO;
import com.SafetyNet.dto.FirestationCoverageDTO;
import com.SafetyNet.dto.FloodHouseholdInfoDTO;
import com.SafetyNet.dto.PhoneNumberDTO;
import com.SafetyNet.dto.ResidentInfoLByLastNameDTO;
import com.SafetyNet.repository.DataLoaderRepository;

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
		assertEquals(4, coveredPersons.getNumberOfAdult());
	}

	@Test
	public void testGetChildrenInfoByAddress() {
		ChildrendInfoDTO coveredChildren = reportingService.getChildrenInfoByAddress("1509 Culver St");
		assertEquals(2, coveredChildren.getCoveredChildren().size());
	}

	@Test
	public void testGetPhoneNumberByFirestation() {
		PhoneNumberDTO phoneNumber = reportingService.getPhoneNumberByFirestation(2);
		assertEquals(4, phoneNumber.getPhoneNumber().size());
	}

	@Test
	public void testGetFireInfoByAddress() {
		FireResidentInfoDTO residentInfo = reportingService.getFireInfoByAddress("1509 Culver St");
		assertEquals(5, residentInfo.getResidents().size());
		assertEquals(List.of(3), residentInfo.getStationNumber());
	}
	
	@Test
	public void testGetFloodInfoByStation() {
		List<FloodHouseholdInfoDTO> householdsInfo = reportingService.getFloodInfobyStation(List.of(1,4));
		assertEquals(5, householdsInfo.size());
	}
	
	@Test
	public void testGetResidentInfoByLastName() {
		ResidentInfoLByLastNameDTO residents = reportingService.getResidentInfoByLastName("Cadigan");
		assertEquals("951 LoneTree Rd", residents.getResidents().get(0).getAddress());
	}
	
	@Test void testGetEmailByCity() {
		EmailInfoDTO emails = reportingService.getEmailByCity("Culver");
		assertEquals(23, emails.getEmails().size());
	}
}
