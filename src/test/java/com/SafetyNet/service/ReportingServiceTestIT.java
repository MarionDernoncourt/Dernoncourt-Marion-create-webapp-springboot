package com.SafetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dto.FirestationCoverageDTO;

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
}
