package com.SafetyNet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dto.ChildAlertDTO;
import dto.FirestationCoverageDTO;
import dto.PhoneNumberDTO;

@SpringBootTest
class ReportingServiceTestIT {

	@Autowired
	private ReportingService reportingService;

	@Test
	public void testGetFirestationCoverage() {
		FirestationCoverageDTO coveredPersons = reportingService.getFirestationCoverage(2);
		assertEquals(5, coveredPersons.getCoveredResident().size());
		assertEquals(4, coveredPersons.getNumberOfAdult());
	}

	@Test
	public void testGetChildAlert() {
		ChildAlertDTO coveredChildren = reportingService.getChildAlert("1509 Culver St");
		assertEquals(2, coveredChildren.getCoveredChildren().size());
	}

	@Test
	public void testGetPhoneNumber() {
		PhoneNumberDTO phoneNumber = reportingService.getPhoneNumber(2);
		assertFalse((phoneNumber.getPhoneNumber().isEmpty()));

	}
}
