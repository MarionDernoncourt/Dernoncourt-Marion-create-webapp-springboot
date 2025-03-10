package repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.model.Person;
import com.SafetyNet.repository.InitializationListsRepository;
import com.SafetyNet.repository.MedicalRecordRepository;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordRepositoryTest {

	@InjectMocks
	private MedicalRecordRepository medicalRecordRepository;

	@Mock
	private InitializationListsRepository initializationListsRepository;

	List<MedicalRecord> mockMedRecords = new ArrayList<MedicalRecord>();

	@BeforeEach
	void setUp() {
		mockMedRecords.add(new MedicalRecord("John", "Doe",
				LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan")));
		mockMedRecords.add(new MedicalRecord("Jane", "Boyd",
				LocalDate.parse("03/06/1989", DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), List.of()));

		when(initializationListsRepository.getAllMedicalRecord()).thenReturn(mockMedRecords);
	}

	@Test
	public void getAllMedicalRecord() throws IOException {
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getAllMedicalRecord();

		assertEquals(mockMedRecords, medicalRecords);
	}

	@Test
	public void getMedicalRecord_ShouldReturnMedicalRecord() {
		MedicalRecord medRecord = medicalRecordRepository.getMedicalRecord("John", "Doe");

		assertEquals(mockMedRecords.get(0).getFirstName(), medRecord.getFirstName());
		assertEquals(mockMedRecords.get(0).getMedications(), medRecord.getMedications());
	}

	@Test
	public void getMedicalRecordTest_WithWrongArgument() {
		MedicalRecord medRecord = medicalRecordRepository.getMedicalRecord("Jane", "Pierce");

		assertNull(medRecord);
	}

	@Test
	public void createMedicalRecordTest() {
		MedicalRecord newMedicalRecord = new MedicalRecord("Mickey", "Mouse",
				LocalDate.parse("01/01/1995", DateTimeFormatter.ofPattern("MM/dd/yyyy")), List.of(""),
				List.of("nillacilan"));

		medicalRecordRepository.createMedicalRecord(newMedicalRecord);

		assertTrue(mockMedRecords.contains(newMedicalRecord));
	}

	@Test
	public void createMedicalRecordTest_WithWrongArgument() {
		MedicalRecord newMedicalRecord = new MedicalRecord("John", "Doe",
				LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan"));

		int listMedRecordSize = mockMedRecords.size();
		medicalRecordRepository.createMedicalRecord(newMedicalRecord);
		assertEquals(listMedRecordSize, mockMedRecords.size());
	}

	@Test
	public void updateMedicalRecord_ShouldUpdateMedicalRecord() {
		MedicalRecord medicalRecord = new MedicalRecord("John", "Doe",
				LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				List.of("aznol:350mg", "hydrapermazol:100mg"), List.of(""));

		MedicalRecord updatedMedRecord = medicalRecordRepository.updateMedicalRecord(medicalRecord);

		assertEquals(medicalRecord, updatedMedRecord);
	}

	@Test
	public void updateMedicalRecord_ForUnknownRecord() {
		MedicalRecord medicalRecord = new MedicalRecord("Minnie", "Lasouris",
				LocalDate.parse("03/06/1984", DateTimeFormatter.ofPattern("MM/dd/yyyy")),
				List.of("aznol:350mg", "hydrapermazol:100mg"), List.of(""));

		MedicalRecord updatedMedRecord = medicalRecordRepository.updateMedicalRecord(medicalRecord);

		assertNotEquals(medicalRecord, updatedMedRecord);
		assertNull(updatedMedRecord);
	}

	@Test
	public void deleteMedicalRecord() {
		boolean medRecordRemoved = medicalRecordRepository.deleteMedicalRecord("John", "Doe");
		assertTrue(medRecordRemoved);

	}
}
