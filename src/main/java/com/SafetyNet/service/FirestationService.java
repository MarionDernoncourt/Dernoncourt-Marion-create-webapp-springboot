package com.SafetyNet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.repository.IFirestationRepository;
import com.SafetyNet.repository.PersonRepository;

import lombok.Data;

@Data
@Service
public class FirestationService {
	
	private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

	@Autowired
	private IFirestationRepository firestationRepository;
	@Autowired
	private PersonRepository personRepository;
	
	public List<Firestation> getAllFirestation() throws IOException {
		return firestationRepository.getAllFirestation();
	}

	
	public List<Firestation> getStation_ByStationNumber(int stationNumber) throws IOException {

		List<Firestation> stations = new ArrayList<Firestation>();

		List<Firestation> firestations = getAllFirestation();

		logger.info("Lecture des firestations et recherche par stationNumber");
		for (Firestation firestation : firestations) {
			if (firestation.getStation() == stationNumber) {
				stations.add(firestation);

			}

		}
		logger.info("Nombre de firestations ayant " + stationNumber +" comme station : " + stations.size());
		return stations;
	}
	
	
}
