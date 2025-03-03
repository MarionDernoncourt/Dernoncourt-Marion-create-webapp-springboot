package com.SafetyNet.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.service.MedicalRecordService;

@RestController
public class MedicalRecordController {

	@Autowired
	MedicalRecordService medicalService;

	@GetMapping("/medicalrecords")
	public List<MedicalRecord> getAllMedicalRecord() throws IOException {
		return medicalService.getAllMedicalRecord();
	}

	@GetMapping("/medicalrecord")
	public MedicalRecord getMedicalRecord_ByLastNameAndFirstName(@RequestParam String firstName, @RequestParam String lastName) throws IOException {
		return medicalService.getMedicalRecord_ByLastNameAndFirstName(firstName, lastName);
	}
}
