package com.SafetyNet.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Firestation;

@Repository
public class FirestationRepository implements IFirestationRepository {

	private static final Logger logger = LoggerFactory.getLogger(FirestationRepository.class);

	@Autowired
	InitializationListsRepository initializationListsRepository;
	 private List<Firestation> firestations = null ;

	@Override
	public List<Firestation> getAllFirestation() {
		firestations = initializationListsRepository.getAllFirestation()	;
	return firestations;
	}

	@Override
	public Firestation getFirestation_ByAddress(String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createFirestation(Firestation firestation) {
		// TODO Auto-generated method stub

	}

	@Override
	public Firestation updateFirestation(Firestation firestation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFirestation(Firestation firestation) {
		// TODO Auto-generated method stub

	}

}