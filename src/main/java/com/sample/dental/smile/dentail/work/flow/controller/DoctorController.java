package com.sample.dental.smile.dentail.work.flow.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sample.dental.smile.dentail.work.flow.dto.DoctorRequestDTO;
import com.sample.dental.smile.dentail.work.flow.dto.DoctorResponseDTO;
import com.sample.dental.smile.dentail.work.flow.entity.Doctor;
import com.sample.dental.smile.dentail.work.flow.exception.ResourceNotFoundException;
import com.sample.dental.smile.dentail.work.flow.repo.DoctorRepository;
import com.sample.dental.smile.dentail.work.flow.service.DoctorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

	private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

	@Autowired
	DoctorService doctorService;

	@Autowired
	DoctorRepository doctorRepository;

	@PostMapping("/create")
	public ResponseEntity<?> createDoctor(@RequestBody DoctorRequestDTO doctorRequestDTO) {
		logger.info("Request received to create a new doctor.");
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String docterEmail = doctorRequestDTO.getEmail();
			Doctor docterData = doctorRepository.findByEmail(docterEmail);
			if (docterData != null) {
				response.put("code", 202);
				response.put("message", "docter with email already exists!!");
				response.put("data", docterData);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			DoctorResponseDTO createdDoctor = doctorService.createDoctor(doctorRequestDTO);
			logger.info("Doctor created successfully with UUID: {}", createdDoctor.getDoctorUuid());
			response.put("code", 201);
			response.put("message", "docter created successfully!!");
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			logger.error("Error occurred while creating doctor: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/list")
	public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
		logger.info("Request received to list all doctors.");
		try {
			List<DoctorResponseDTO> doctors = doctorService.getAllDoctors();
			logger.info("Fetched list of doctors. Total doctors: {}", doctors.size());
			return ResponseEntity.ok(doctors);
		} catch (Exception e) {
			logger.error("Error occurred while fetching doctors: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/{uuid}")
	public ResponseEntity<DoctorResponseDTO> getDoctorByUuid(@PathVariable("uuid") String doctorUuid) {
		logger.info("Request received to fetch doctor with UUID: {}", doctorUuid);
		try {
			DoctorResponseDTO doctor = doctorService.getDoctorByUuid(doctorUuid);
			logger.info("Fetched doctor with UUID: {}", doctorUuid);
			return ResponseEntity.ok(doctor);
		} catch (ResourceNotFoundException e) {
			logger.error("Doctor with UUID {} not found: {}", doctorUuid, e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			logger.error("Error occurred while fetching doctor with UUID: {}", doctorUuid);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/update/{uuid}")
	public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable("uuid") String doctorUuid,
			@RequestBody DoctorRequestDTO doctorRequestDTO) {
		logger.info("Request received to update doctor with UUID: {}", doctorUuid);
		try {
			DoctorResponseDTO updatedDoctor = doctorService.updateDoctor(doctorUuid, doctorRequestDTO);
			logger.info("Doctor with UUID: {} updated successfully.", doctorUuid);
			return ResponseEntity.ok(updatedDoctor);
		} catch (ResourceNotFoundException e) {
			logger.error("Doctor with UUID {} not found: {}", doctorUuid);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			logger.error("Error occurred while updating doctor with UUID: {}", doctorUuid);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/top")
	public ResponseEntity<List<DoctorResponseDTO>> getTopDoctors() {
		logger.info("Fetching top doctors");
		try {
			List<DoctorResponseDTO> topDoctors = doctorService.getTopDoctors();
			logger.info("Successfully retrieved top doctors");
			return new ResponseEntity<>(topDoctors, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching top doctors: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/specialized")
	public ResponseEntity<List<DoctorResponseDTO>> getSpecializedDoctors() {
		logger.info("Fetching specialized doctors");
		try {
			List<DoctorResponseDTO> specializedDoctors = doctorService.getSpecializedDoctors();
			logger.info("Successfully retrieved specialized doctors");
			return new ResponseEntity<>(specializedDoctors, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching specialized doctors: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/top-specialized")
	public ResponseEntity<List<DoctorResponseDTO>> getTopSpecializedDoctors() {
		logger.info("Fetching top specialized doctors");
		try {
			List<DoctorResponseDTO> topSpecializedDoctors = doctorService.getTopSpecializedDoctors();
			logger.info("Successfully retrieved top specialized doctors");
			return new ResponseEntity<>(topSpecializedDoctors, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching top specialized doctors: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
