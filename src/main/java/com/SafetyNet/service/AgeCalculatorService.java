package com.SafetyNet.service;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Service;

@Service
public class AgeCalculatorService {

	public int calculateAge(LocalDate birthdate) throws Exception {
	
		LocalDate now = LocalDate.now()	;
		if (birthdate == null) {
			throw new IllegalArgumentException("La date de naissance ne peut pas être nulle");
		} 
		if (birthdate.isAfter(now)) {
			throw new IllegalArgumentException("La date de naissance ne peut pas être dans le futur");
		}
		return  Period.between(birthdate, now).getYears();
	}
}
