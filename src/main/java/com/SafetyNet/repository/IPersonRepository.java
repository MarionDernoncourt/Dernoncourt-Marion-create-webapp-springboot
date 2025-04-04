package com.SafetyNet.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.model.Person;

/**
 * Interface définissant les différentes opérations de gestion des données
 * relatives aux personnes (récupération, mise à jour, création et suppression)
 */
@Repository
public interface IPersonRepository {

	/**
	 * Récupère la liste de toutes les personnes
	 * 
	 * @return une liste de {@link Person }
	 */
	public List<Person> getAllPersons() throws IOException;

	/**
	 * Récupère une personne
	 * 
	 * @param firstName : prénom du résident recherché
	 * @param lastName  : nom du résident recherché
	 * @return un objet Person {@link Person }
	 */
	public Person getPersonByFirstNameAndLastName(String firstName, String lastName) throws IOException;

	/**
	 * Création d'une nouvelle personne
	 * 
	 * @param Person : nouvelle personne
	 * @return Person : la personne créée
	 */
	public Person createPerson(Person person) throws IOException;

	/**
	 * Mise à jour d'une personne
	 * 
	 * @param person : nouvelles informations concernant la personne à modifier
	 * @return Person : la personne mise à jour
	 */
	public Person updatePerson(Person person) throws IOException;

	/**
	 * Suppression d'une personne
	 * 
	 * @param firstName : prénom du résident recherché
	 * @param lastName  : nom du résident recherché
	 * @return boolean : True si le dossier médical a été supprimé, sinon False.
	 */
	public boolean deletePerson(String firstName, String lastName) ;
}
