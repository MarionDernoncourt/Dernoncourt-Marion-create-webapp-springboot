package com.SafetyNet.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Person;

@Repository
public interface IPersonRepository {

	public List<Person> getAllPersons() throws IOException ;
	
	public Person getPersonByFirstNameAndLastName(String firstName, String lastName) throws IOException;
	
	public void createPerson(Person person) throws IOException ;
	
	public Person updatePerson(Person person) throws IOException;
	
	public boolean deletePerson(String firstName, String lastName) throws IOException;
}
