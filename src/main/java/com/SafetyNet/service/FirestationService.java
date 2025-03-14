package com.SafetyNet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.repository.FirestationRepository;
import com.SafetyNet.repository.IFirestationRepository;
import com.SafetyNet.repository.PersonRepository;

import lombok.Data;

@Service
public class FirestationService {
	
	private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

	@Autowired
	private FirestationRepository firestationRepository;

	
	public List<Firestation> getAllFirestation() throws IOException {
		return firestationRepository.getAllFirestation();
	}
	
	public Firestation getFirestation_ByAddress(String address) {
		return firestationRepository.getFirestation_ByAddress(address);
	}
	
	public Firestation createFirestation (Firestation firestation) {
		return firestationRepository.createFirestation(firestation);
	}
	
	public Firestation updateFirestation(Firestation firestation) {
		return firestationRepository.updateFirestation(firestation);
	}
	
	public boolean deleteFirestation(Firestation firestation) {
		return firestationRepository.deleteFirestation(firestation);
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
