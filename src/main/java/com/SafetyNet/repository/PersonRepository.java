package com.SafetyNet.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.model.Person;
import com.SafetyNet.service.FirestationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class PersonRepository implements IPersonRepository {

	private static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

	@Override
	public List<Person> getAllPersons() throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		File jsonFile = new File("src/main/resources/data.json");

		logger.info("Lecture du fichier json", jsonFile.getPath());

		JsonNode root = objectMapper.readTree(jsonFile);
		JsonNode personsNode = root.get("persons");

		if (personsNode == null || !personsNode.isArray()) {
			logger.warn("Aucune données trouvées dans 'persons'. Retour d'une liste vide");
			return List.of();

		}
		logger.info("Nombre de personnes trouvées : {}", personsNode.size());
		return objectMapper.convertValue(personsNode, new TypeReference<List<Person>>() {
		});
	}


	
}