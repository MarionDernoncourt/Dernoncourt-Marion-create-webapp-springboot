package com.SafetyNet.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long>{

}
