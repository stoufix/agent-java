package com.aboutme.agent.service;

import com.aboutme.agent.services.CvPdfService;
import com.example.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CvPdfServiceTest {

	@Test
	void generatePdf_withFullCv_returnsPdfBytes() {

		CvRequest cv = new CvRequest();

		PersonalInfo p = new PersonalInfo();
		p.setFirstName("John");
		p.setLastName("Doe");
		p.setEmail("john.doe@example.com");
		p.setPhone("+33123456789");
		cv.setPersonalInfo(p);

		cv.setSummary("Experienced engineer");

		Experience exp = new Experience();
		exp.setPosition("Senior Developer");
		exp.setCompany("ACME");
		exp.setStartDate(LocalDate.of(2018,1,1));
		exp.setEndDate(LocalDate.of(2025,1,1));
		exp.setResponsibilities(Arrays.asList("Develop features", "Write tests"));
		cv.setExperience(List.of(exp));

		Education edu = new Education();
		edu.setDegree("MSc Computer Science");
		edu.setInstitution("University");
		cv.setEducation(List.of(edu));

		cv.setSkills(Arrays.asList("Java", "Spring", "PDF"));

		byte[] pdf = CvPdfService.generatePdf(cv);

		assertNotNull(pdf);
		assertTrue(pdf.length > 10, "PDF should contain content");
		String header = new String(pdf, 0, 4);
		assertEquals("%PDF", header);
	}

	@Test
	void generatePdf_withMinimalCv_returnsPdfBytes() {
		CvRequest cv = new CvRequest();
		// minimal personal info
		PersonalInfo p = new PersonalInfo();
		p.setFirstName("A");
		p.setLastName("B");
		cv.setPersonalInfo(p);

		byte[] pdf = CvPdfService.generatePdf(cv);

		assertNotNull(pdf);
		assertTrue(pdf.length > 10);
		String header = new String(pdf, 0, 4);
		assertEquals("%PDF", header);
	}
}
