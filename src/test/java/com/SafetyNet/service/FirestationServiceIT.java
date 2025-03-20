package com.SafetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.repository.DataLoaderRepository;
import com.SafetyNet.repository.FirestationRepository;

@SpringBootTest
public class FirestationServiceIT {

	@Autowired
	private FirestationService firestationService;

	@Autowired
	private FirestationRepository firestationRepository;

	@Autowired
	private DataLoaderRepository dataLoaderRepository;

	@BeforeEach
	void setUp() {
		dataLoaderRepository.setFirestations(null);
		dataLoaderRepository.setMedicalRecords(null);
		dataLoaderRepository.setPersons(null);
	}

	@Test
	public void testGetAllFirestation() {
		List<Firestation> stations = firestationService.getAllFirestation();
		assertTrue(!stations.isEmpty());
	}

	@Test
	public void testCreateStation() {
		List<Firestation> stations = firestationService.getAllFirestation();
		int sizeBefore = stations.size();
		Firestation newStation = firestationService.createFirestation(new Firestation("112 Fire St", 3));
		int sizeAfter = firestationService.getAllFirestation().size();
		assertNotEquals(sizeBefore, sizeAfter);
		assertTrue(stations.contains(newStation));
	}

	@Test
	public void testUpdateFirestation() {
		Firestation stationToUpdate = firestationService.findByAddress("1509 Culver St");
		stationToUpdate.setStation(2);
		firestationService.updateFirestation(stationToUpdate);
		Firestation stationUpdated = firestationService.findByAddress("1509 Culver St");
		assertEquals(2, stationUpdated.getStation());
	}

	@Test
	public void testDeleteFirestationWithAddress() {
		boolean stationRemoved = firestationService.deleteFirestation("951 LoneTree Rd", null);
		assertTrue(stationRemoved);
	}
	
	@Test 
	public void testDeleteFirestationWithStationNumber() {
		boolean stationRemoved = firestationService.deleteFirestation(null, 1);
		assertTrue(stationRemoved);
	}
	
	@Test
	public void testDeleteFirestationWithAddressAndStationNumber() {
		boolean stationRemoved = firestationService.deleteFirestation("1509 Culver St", 3);
		assertTrue(stationRemoved);
	}
}
