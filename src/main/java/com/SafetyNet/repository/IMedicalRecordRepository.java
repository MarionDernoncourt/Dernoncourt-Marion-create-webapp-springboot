package com.SafetyNet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.model.MedicalRecord;

/**
 * Interface définissant les différentes opérations de gestion des données
 * relatives aux dossiers médicaux (récupération, mise à jour, création et
 * suppression)
 */
@Repository
public interface IMedicalRecordRepository {

	/**
	 * Récupère la liste de toutes les dossier médicaux
	 * 
	 * @return une liste de {@link MedicalRecord }
	 */
	public List<MedicalRecord> getAllMedicalRecord();

	/**
	 * Récupère la liste d'un dossier médical
	 * 
	 * @param firstName : prénom du résident recherché
	 * @param lastName  : nom du résident recherché
	 * @return un dossier medical MedicalRecord {@link MedicalRecord }
	 */
	public MedicalRecord getMedicalRecord(String firstName, String lastName);

	/**
	 * Création d'un nouveau dossier médical
	 * 
	 * @param medicalRecord : nouveau dossier médical
	 * @return Firestation : le dossier créé
	 */
	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord);

	/**
	 * Mise à jour d'un dossier médical
	 * 
	 * @param medicalRecord : nouvelles informations concernant le dossier medical
	 * @return MedicalRecord : le dossier médical mis à jour
	 */
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);

	/**
	 * Suppression d'un dossier médical
	 * 
	 * @param firstName : prénom du résident recherché pour suppression de son
	 *                  dossier médical
	 * @param lastName  : nom du résident recherché pour suppression de son dossier
	 *                  médical
	 * @return boolean : True si le dossier médical a été supprimé, sinon False.
	 */
	public boolean deleteMedicalRecord(String firstName, String lastName);
}
