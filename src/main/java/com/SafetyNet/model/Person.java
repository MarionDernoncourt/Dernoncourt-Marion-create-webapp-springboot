package com.SafetyNet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente une Person Contient le nom (String), prénom(String),
 * adresse(String), ville(String), code postal (int), numéro de
 * téléphone(String) et email(String).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private int zip;
	private String phone;
	private String email;

}
