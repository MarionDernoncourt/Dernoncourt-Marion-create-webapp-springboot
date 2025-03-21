package com.SafetyNet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.SafetyNet.model.MedicalRecord;

@Repository
public interface IMedicalRecordRepository {

	public List<MedicalRecord> getAllMedicalRecord() ;

	public MedicalRecord getMedicalRecord (String firstName, String lastName);
	
	public MedicalRecord createMedicalRecord (MedicalRecord medicalRecord);
	
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);
	
	public boolean deleteMedicalRecord(String firstName, String lastName);
}
