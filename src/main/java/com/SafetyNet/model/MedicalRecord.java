package com.SafetyNet.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {

	private String firstName;
	private String lastName;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private LocalDate birthdate;
	private List<String> medications;
	private List<String> allergies;
	

}
