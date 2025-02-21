package com.SafetyNet.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends CrudRepository<MedicalRecord, Long>{

}
