package com.SafetyNet.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.Data;

/**
 * Ce repository récupère les données du fichier JSON et les instancie dans 3
 * listes distinctes. Lorsqu'une méthode de récupération est appelée, on verifie
 * si la liste correspondante est déjà chargée en mémoire. Si oui on retourne la
 * liste existante, si non on lit le fichier JSON, on parse la section
 * correspondante et l'instancie en mémoire. Ce chargement est déclenché
 * automatiquement au démarrage de l'application via l'implémentaire de
 * CommandLineRunner. Les données sont chargées depuis le fichier situé à :
 * {@code src/main/resources/data.json} .
 */
@Data
@Repository
public class DataLoaderRepository implements CommandLineRunner, IDataLoaderRepository {

	private static final Logger logger = LoggerFactory.getLogger(DataLoaderRepository.class);

	private List<Person> persons = null;

	private List<Firestation> firestations = null;

	private List<MedicalRecord> medicalRecords = null;

	private ObjectMapper objectMapper = new ObjectMapper();
	private File jsonFile = new File("src/main/resources/data.json");

	/**
	 * Récupère la liste des personnes depuis le fichier JSON
	 * Si la liste est déjà chargée en mémoire, elle est directement retournée.
	 * 
	 * @return List<Person> : liste de personnes, ou une liste vide en cas d'erreur
	 *         ou si aucune donnée n'est trouvée
	 */
	public List<Person> getAllPersons() {
		try {
			// Vérification si la liste Persons a déjà été chargée en mémoire
			if (persons != null) {
				logger.debug("Liste de personne déjà chargée en mémoire");
				return persons;
			}

			// Lecture du fichier JSON
			JsonNode root = objectMapper.readTree(jsonFile); // Lire le fichier JSON et le convertir en objet JsonNode<
			JsonNode personsNode = root.get("persons"); // on cherche le noeud "persons"
			
			// Verification si le noeud Persons est valide
			if (personsNode == null || !personsNode.isArray()) {
				logger.error("Aucune données trouvées dans 'persons'. Retour d'une liste vide");
				return List.of();
			}
			// Conversion des données JSON en objet Java
			logger.info("Lecture du fichier ok, nombre d'habitants : " + personsNode.size());
			persons = objectMapper.convertValue(personsNode, new TypeReference<List<Person>>() {
			});
			// La liste est stokée dans persons 
			return persons;
			
		} catch (IOException e) {
			logger.error("Erreur lors de la lecture du fichier JSON", e);
			return List.of();
		}

	};

	
	/**
	 * Récupère la liste des casernes de pompiers depuis le fichier JSON
	 * Si la liste est déjà chargée en mémoire, elle est directement retournée.
	 * 
	 * @return List<Firestation> : liste de casernes, ou une liste vide en cas d'erreur
	 *         ou si aucune donnée n'est trouvée
	 */
	public List<Firestation> getAllFirestation() {
		try {
			if (firestations != null) {
				logger.debug("Liste de casernes déjà chargée en mémoire");
				return firestations;
			}
			JsonNode root = objectMapper.readTree(jsonFile);
			JsonNode firestationNode = root.get("firestations");

			if (firestationNode == null || !firestationNode.isArray()) {
				logger.error("Aucune donnée trouvée");
				return List.of();
			}
			logger.info("Lecture du fichier ok, nombre de firestation : " + firestationNode.size());
			firestations = objectMapper.convertValue(firestationNode, new TypeReference<List<Firestation>>() {
			});
			return firestations;

		} catch (IOException e) {
			logger.error("Erreur lors de la lecture du fichier JSON", e);
			return List.of();
		}
	};

	
	/**
	 * Récupère la liste des dossier médicaux depuis le fichier JSON
	 * Si la liste est déjà chargée en mémoire, elle est directement retournée.
	 * 
	 * @return List<MedicalRecord> : liste des dossiers médicaux, ou une liste vide en cas d'erreur
	 *         ou si aucune donnée n'est trouvée
	 */
	public List<MedicalRecord> getAllMedicalRecord() {

		try {
			if (medicalRecords != null) {
				logger.debug("Liste de dossier médical déjà chargée en mémoire");

				return medicalRecords;
			}

			objectMapper.registerModule(new JavaTimeModule());

			JsonNode root = objectMapper.readTree(jsonFile);
			JsonNode medicalRecordNode = root.get("medicalrecords");

			if (medicalRecordNode == null || !medicalRecordNode.isArray()) {
				logger.error("Aucune donnée trouvées / Retour d'une liste vide");
				return List.of();
			}
			logger.info("Lecture du fichier ok, nombre de dossier medical : " + medicalRecordNode.size());
			medicalRecords = objectMapper.convertValue(medicalRecordNode, new TypeReference<List<MedicalRecord>>() {
			});
			return medicalRecords;

		} catch (IOException e) {
			logger.error("Erreur lors de la lecture du fichier JSON", e);
			return List.of();
		}

	}

	@Override
	public void run(String... args) throws Exception {
		getAllPersons();
		getAllFirestation();
		getAllMedicalRecord();

	}
}
