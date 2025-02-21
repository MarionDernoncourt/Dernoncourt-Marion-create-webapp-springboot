package com.SafetyNet.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Firestation;

@Repository
public interface FirestationRepository extends CrudRepository<Firestation, Long> {

}
