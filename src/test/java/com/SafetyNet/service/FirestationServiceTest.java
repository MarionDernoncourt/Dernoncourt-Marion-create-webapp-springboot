package com.SafetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.SafetyNet.model.Firestation;

@SpringBootTest
public class FirestationServiceTest {

	@Autowired
	private FirestationService firestationService;



	@Test
	public void testGetAllFirestation() throws Exception {
		List<Firestation> stations = firestationService.getAllFirestation();
		assertEquals(13, stations.size());
	}

	@Test
	public void testGetStationByAddress() {
		Firestation station = firestationService.getFirestation_ByAddress("1509 Culver St");
		assertEquals(3, station.getStation());
	}

	@Test
	public void testCreateStation() throws Exception {
		List<Firestation> stations = firestationService.getAllFirestation();
		int sizeBefore = stations.size();
		Firestation newStation = firestationService.createFirestation(new Firestation("112 Fire St", 3));
		int sizeAfter = firestationService.getAllFirestation().size();
		assertNotEquals(sizeBefore, sizeAfter);
		assertTrue(stations.contains(newStation));
	}

	@Test
	public void testUpdateFirestation() {
		Firestation stationToUpdate = firestationService.getFirestation_ByAddress("1509 Culver St");
		stationToUpdate.setStation(2);
		Firestation stationUpdated = firestationService.updateFirestation(stationToUpdate);
		assertEquals(2, stationUpdated.getStation());
	}

	@Test
	public void testDeleteFirestation() {
		boolean stationRemoved = firestationService.deleteFirestation("951 LoneTree Rd");
		assertTrue(stationRemoved);
	}
}
