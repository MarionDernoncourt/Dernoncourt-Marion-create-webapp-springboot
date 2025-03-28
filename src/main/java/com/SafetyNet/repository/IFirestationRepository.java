package com.SafetyNet.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Firestation;

@Repository
public interface IFirestationRepository {

	public List<Firestation> getAllFirestation() throws IOException;

	public Firestation createFirestation(Firestation firestation);

	public Firestation updateFirestation(Firestation firestation);

	public boolean deleteFirestation(String address,Integer stationNumber);

	
}
