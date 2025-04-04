package com.SafetyNet.repository;

import java.util.List;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.model.Person;

/**
 * Interface définissant les méthodes de chargement des données depuis un fichier
 * JSON
 */
public interface IDataLoaderRepository {
	/**
	 * Récupère la liste de personnes.
	 * 
	 * @return une liste de {@link Person}
	 */
	public List<Person> getAllPersons();
	

	/**
	 * Récupère la liste des casernes de pompiers
	 * 
	 * @return une liste de {@link Firestation}
	 */
	public List<Firestation> getAllFirestation();

	
	/**
	 * Récupère la liste des dossiers médicaux
	 * 
	 * @return une liste de {@link MedicalRecord}
	 */
	public List<MedicalRecord> getAllMedicalRecord();
	
}
