package com.SafetyNet.service;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service permettant de calculer l'âge d'une personne à partir de sa date de
 * naissance. Utilise {@link MedicalRecordService} pour récupérer les
 * informations médicales.
 */
@Service
public class AgeCalculatorService {

	@Autowired
	private MedicalRecordService medicalRecordService;

	/**
	 * Calcule l'âge d'une personne à partir de sa date de naissance.
	 *
	 * @param birthdate la date de naissance de la personne
	 * @return l'âge en années
	 * @throws IllegalArgumentException si la date est nulle ou dans le futur
	 */
	public int calculateAge(LocalDate birthdate) {

		LocalDate now = LocalDate.now();
		if (birthdate == null) {
			throw new IllegalArgumentException("La date de naissance ne peut pas être nulle");
		}
		if (birthdate.isAfter(now)) {
			throw new IllegalArgumentException("La date de naissance ne peut pas être dans le futur");
		}
		return Period.between(birthdate, now).getYears();
	}

	/**
	 * Calcule l'âge d'une personne en utilisant son prénom et son nom. La date de
	 * naissance est récupérée via le {@link MedicalRecordService}.
	 *
	 * @param firstName le prénom de la personne
	 * @param lastName  le nom de la personne
	 * @return l'âge en années
	 * @throws IllegalArgumentException si la date de naissance est introuvable
	 *                                  (null)
	 */
	public int calculatePersonAge(String firstName, String lastName) {

		LocalDate birthdate = medicalRecordService.getMedicalRecord(firstName, lastName).getBirthdate();
		if (birthdate == null) {
			throw new IllegalArgumentException("Birthdate est NULL");
		}
		return calculateAge(birthdate);
	}

}
