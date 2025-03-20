package com.SafetyNet.service;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgeCalculatorService {

	@Autowired
	private MedicalRecordService medicalRecordService;

	public int calculateAge(LocalDate birthdate)  {

		LocalDate now = LocalDate.now();
		if (birthdate == null) {
			throw new IllegalArgumentException("La date de naissance ne peut pas être nulle");
		}
		if (birthdate.isAfter(now)) {
			throw new IllegalArgumentException("La date de naissance ne peut pas être dans le futur");
		}
		return Period.between(birthdate, now).getYears();
	}



	public int calculatePersonAge(String firstName, String lastName) {
System.out.println(firstName);
		LocalDate birthdate = medicalRecordService.getMedicalRecord(firstName, lastName).getBirthdate();
		if (birthdate == null) {
			throw new IllegalArgumentException("Birthdate est NULL");
		}
		return calculateAge(birthdate);
	}

}
