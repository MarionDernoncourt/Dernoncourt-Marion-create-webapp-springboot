package com.SafetyNet.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Firestation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class FirestationRepository implements IFirestationRepository {

	private static final Logger logger = LoggerFactory.getLogger(FirestationRepository.class);

	private ObjectMapper objectMapper = new ObjectMapper();
	private File jsonFile = new File("src/main/resources/data.json");

	@Override
	public List<Firestation> getAllFirestation() {
		try {
			logger.info("lecture du fichier JSON");
			JsonNode root = objectMapper.readTree(jsonFile);
			JsonNode firestationNode = root.get("firestations");

			if (firestationNode == null || !firestationNode.isArray()) {
				logger.warn("Aucune donnée trouvée");
				return List.of();
			}
			logger.info("Lecture du fichier ok, nombre de firestation : " + firestationNode.size());
			return objectMapper.convertValue(firestationNode, new TypeReference<List<Firestation>>() {
			});
		} catch (IOException e) {
			logger.error("Erreur lors de la lecture du fichier JSON", e);
			return List.of();
		}
	
	}


	
}