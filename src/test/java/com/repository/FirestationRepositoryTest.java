package com.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.repository.FirestationRepository;
import com.SafetyNet.repository.InitializationListsRepository;

@ExtendWith(MockitoExtension.class)
public class FirestationRepositoryTest {

	@InjectMocks
	private FirestationRepository firestationRepository;

	@Mock
	private InitializationListsRepository initializationListsRepository;

	List<Firestation> mockFirestations = new ArrayList<Firestation>();

	@BeforeEach
	void setUp() {
		mockFirestations.add(new Firestation("1509 Culver St", 3));
		mockFirestations.add(new Firestation("29 15th St", 2));

		when(initializationListsRepository.getAllFirestation()).thenReturn(mockFirestations);
	}

	@Test
	public void getAllFirestationsTest() {
		List<Firestation> firestations = firestationRepository.getAllFirestation();
		assertEquals(mockFirestations, firestations);
	}

	@Test
	public void getFirestationTest_ByAddress() {

		Firestation result = firestationRepository.getFirestation_ByAddress("1509 Culver St");
		assertEquals(mockFirestations.get(0), result);
	}

	@Test
	public void getFirestationTest_WithWrongAddress() {
		Firestation result = firestationRepository.getFirestation_ByAddress("12 Main st");
		assertNull(result);
	}

	@Test
	public void createFirestationTest() {
		Firestation newStation = new Firestation("12 St Laurent", 2);
		firestationRepository.createFirestation(newStation);
		assertTrue(mockFirestations.contains(newStation));
	}

	@Test
	public void createFirestationTest_withExistantAddress() {
		Firestation newStation = new Firestation("1509 Culver St", 5);
		int initialSize = mockFirestations.size();
		firestationRepository.createFirestation(newStation);
		assertEquals(initialSize, mockFirestations.size());
		assertFalse(mockFirestations.contains(newStation));
	}

	@Test
	public void updateFirestationNumberTest() {
		Firestation stationToUpdate = new Firestation("1509 Culver St", 8);
		Firestation updatedStation = firestationRepository.updateFirestation(stationToUpdate);
		assertNotNull(updatedStation);
		assertEquals(8, updatedStation.getStation());
		assertEquals("1509 Culver St", updatedStation.getAddress());
	}

	@Test
	public void updateFirestationTest_withWrongArgument() {
		Firestation stationToUpdate = new Firestation("123 South St", 2);
		Firestation updatedStation = firestationRepository.updateFirestation(stationToUpdate);
		assertNull(updatedStation);
		assertFalse(mockFirestations.contains(updatedStation));
	}

	@Test
	public void deleteFirestationTest() {
		Firestation stationToDelete = new Firestation("1509 Culver St", 3);
		boolean firestationRemoved = firestationRepository.deleteFirestation(stationToDelete);
		assertTrue(firestationRemoved);
		assertFalse(mockFirestations.contains(stationToDelete));
	}

	@Test
	void deleteFirestationTest_WithWrongArgument() {
		Firestation stationToDelete = new Firestation("1509 Culver St", 8);
		boolean firestationRemoved = firestationRepository.deleteFirestation(stationToDelete);
		assertFalse(firestationRemoved);

	}
}
