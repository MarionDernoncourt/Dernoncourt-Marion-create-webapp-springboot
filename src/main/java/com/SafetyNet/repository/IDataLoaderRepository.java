package com.SafetyNet.repository;

import java.util.List;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.model.Person;

public interface IDataLoaderRepository {

	public List<Person> getAllPersons();

	public List<Firestation> getAllFirestation();

	public List<MedicalRecord> getAllMedicalRecord();
}
