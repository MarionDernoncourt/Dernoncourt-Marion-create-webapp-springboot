package com.SafetyNet.Repository;

import org.springframework.data.repository.CrudRepository;

import com.SafetyNet.model.Person;

public interface PersonRepository extends CrudRepository<Person, Long>{

}
