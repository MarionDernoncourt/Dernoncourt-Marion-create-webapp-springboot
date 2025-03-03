package com.SafetyNet.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data

public class MedicalRecord {

	private String firstName;
	private String lastName;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private LocalDate birthdate;
	private List<String> medications;
	private List<String> allergies;

}
