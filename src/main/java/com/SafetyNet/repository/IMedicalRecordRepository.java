package com.SafetyNet.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.SafetyNet.model.MedicalRecord;

@Repository
public interface IMedicalRecordRepository {

	public List<MedicalRecord> getAllMedicalRecord() throws IOException;

}
