package com.SafetyNet.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Person;

@Repository
public interface IPersonRepository {

	public List<Person> getAllPersons() throws IOException ;
}
