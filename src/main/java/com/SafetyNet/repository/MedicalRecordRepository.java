package com.SafetyNet.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.service.AgeCalculatorService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Repository
public class MedicalRecordRepository implements IMedicalRecordRepository {

	private static final Logger logger = LoggerFactory.getLogger(MedicalRecordRepository.class);

	@Autowired
	AgeCalculatorService ageCalculatorService;

	private ObjectMapper objectMapper = new ObjectMapper();
	private File jsonFile = new File("src/main/resources/data.json");

	@Override
	public List<MedicalRecord> getAllMedicalRecord() throws IOException {

		objectMapper.registerModule(new JavaTimeModule());

		JsonNode root = objectMapper.readTree(jsonFile);
		JsonNode medicalRecordNode = root.get("medicalrecords");

		if (medicalRecordNode == null || !medicalRecordNode.isArray()) {
			logger.warn("Aucune donnée trouvées / Retour d'une liste vide");
			return List.of();
		}

		return objectMapper.convertValue(medicalRecordNode, new TypeReference<List<MedicalRecord>>() {
		});
	}

}