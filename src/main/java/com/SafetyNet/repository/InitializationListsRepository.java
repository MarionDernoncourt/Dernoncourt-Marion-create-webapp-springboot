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

@Data
@Repository
public class InitializationListsRepository implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(InitializationListsRepository.class);

	private List<Person> persons = new ArrayList<Person>();

	private List<Firestation> firestations = new ArrayList<Firestation>();

	private List<MedicalRecord> medicalRecord = new ArrayList<MedicalRecord>();

	private ObjectMapper objectMapper = new ObjectMapper();
	private File jsonFile = new File("src/main/resources/data.json");

	public List<Person> getAllPersons() {
		try {
			if (!persons.isEmpty()) {
				return persons;
			}
			JsonNode root = objectMapper.readTree(jsonFile);
			JsonNode personsNode = root.get("persons");

			if (personsNode == null || !personsNode.isArray()) {
				logger.warn("Aucune données trouvées dans 'persons'. Retour d'une liste vide");
				return List.of();
			}
			logger.info("Lecture du fichier ok, nombre d'habitants : " + personsNode.size());

			persons = objectMapper.convertValue(personsNode, new TypeReference<List<Person>>() {
			});
			logger.info("Taille de la liste avant retour" + persons.size());
			return persons;
		} catch (IOException e) {
			logger.error("Erreur lors de la lecture du fichier JSON", e);
			return List.of();
		}

	};

	public List<Firestation> getAllFirestation() {
		try {
			if (!firestations.isEmpty()) {
				return firestations;
			}
			JsonNode root = objectMapper.readTree(jsonFile);
			JsonNode firestationNode = root.get("firestations");

			if (firestationNode == null || !firestationNode.isArray()) {
				logger.warn("Aucune donnée trouvée");
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

	public List<MedicalRecord> getAllMedicalRecord() {
		List<MedicalRecord> medicalrecords = null;
		try {

			objectMapper.registerModule(new JavaTimeModule());

			JsonNode root = objectMapper.readTree(jsonFile);
			JsonNode medicalRecordNode = root.get("medicalrecords");

			if (medicalRecordNode == null || !medicalRecordNode.isArray()) {
				logger.warn("Aucune donnée trouvées / Retour d'une liste vide");
				return List.of();
			}
			logger.info("Lecture du fichier ok, nombre de dossier medical : " + medicalRecordNode.size());
			medicalrecords = objectMapper.convertValue(medicalRecordNode, new TypeReference<List<MedicalRecord>>() {
			});
			return medicalrecords;

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
