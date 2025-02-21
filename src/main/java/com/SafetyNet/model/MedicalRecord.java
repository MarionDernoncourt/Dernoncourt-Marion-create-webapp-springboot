package com.SafetyNet.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="medicalrecords")
public class MedicalRecord {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		private String firstName;
		private String lastName;
		@JsonFormat(pattern = "MM/dd/yyyy")
		private LocalDate birthdate;
		private List<String> medications;
		private List<String> allergies;
		
}
