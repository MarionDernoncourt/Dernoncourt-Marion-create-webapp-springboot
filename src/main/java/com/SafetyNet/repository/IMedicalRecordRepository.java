package com.SafetyNet.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.SafetyNet.model.MedicalRecord;

@Repository
public interface IMedicalRecordRepository {

	public List<MedicalRecord> getAllMedicalRecord() throws IOException;

	public MedicalRecord getMedicalRecord (String firstName, String lastName);
	
	public void createMedicalRecord (MedicalRecord medicalRecord);
	
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);
	
	public boolean deleteMedicalRecord(String firstName, String lastName);
}
