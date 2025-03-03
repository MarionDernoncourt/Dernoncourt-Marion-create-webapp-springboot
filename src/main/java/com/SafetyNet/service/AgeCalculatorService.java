package com.SafetyNet.service;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Service;

@Service
public class AgeCalculatorService {


	public int calculateAge(LocalDate birthdate) throws Exception {

		if (birthdate == null) {
			throw new IllegalAccessException("La date de naissance ne peut pas être nulle");
		}
		return Period.between(birthdate, LocalDate.now()).getYears();
	}
	
	
}
