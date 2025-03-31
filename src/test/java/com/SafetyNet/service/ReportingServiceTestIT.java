package com.SafetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dto.ChildrendInfoDTO;
import dto.FireInfoDTO;
import dto.FirestationCoverageDTO;
import dto.PhoneNumberDTO;

@SpringBootTest
class ReportingServiceTestIT {

	@Autowired
	private ReportingService reportingService;

	@Test
	public void testGetFirestationCoverage() {
		FirestationCoverageDTO coveredPersons = reportingService.getFirestationCoverage(2);
		assertTrue(coveredPersons.getCoveredResident().size() > 0);
		assertTrue(coveredPersons.getNumberOfAdult() > 0);
	}

	@Test
	public void getChildInfoByAddress() {
		ChildrendInfoDTO coveredChildren = reportingService.getChildInfoByAddress("1509 Culver St");
		assertEquals(2, coveredChildren.getCoveredChildren().size());
	}

	@Test
	public void testGetPhoneNumber() {
		PhoneNumberDTO phoneNumber = reportingService.getPhoneNumber(2);
		assertFalse((phoneNumber.getPhoneNumber().isEmpty()));
	}

	@Test
	public void getResidentInfoCaseOfFire() {
		FireInfoDTO residentInfo = reportingService.getResidentInfoCaseOfFire("1509 Culver St");
		assertFalse(residentInfo.getResidents().isEmpty());
		assertEquals(List.of(3), residentInfo.getStationNumber());
	}
}
