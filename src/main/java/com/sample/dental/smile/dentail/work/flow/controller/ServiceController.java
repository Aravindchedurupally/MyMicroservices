package com.sample.dental.smile.dentail.work.flow.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.dental.smile.dentail.work.flow.dto.DoctorScheduleRequestDTO;
import com.sample.dental.smile.dentail.work.flow.service.DoctorService;

@RestController
@RequestMapping("/api/service")
public class ServiceController {

	private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

	@Autowired
	private DoctorService doctorService;

	@GetMapping("/{doctorUuid}/availability")
	public ResponseEntity<?> checkDoctorAvailability(@PathVariable String doctorUuid, @RequestParam String date,
			@RequestParam String startTime) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			// Parse date and time
			LocalDate appointmentDate = LocalDate.parse(date);
			LocalTime appointmentTime = LocalTime.parse(startTime);
			// Check if the doctor is available in the provided slot
			response = doctorService.isDoctorAvailable(doctorUuid, appointmentDate, appointmentTime);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error checking doctor availability for doctor UUID: " + doctorUuid, e);
			response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put("Error", "An error occurred while checking availability.");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/schedule")
	public ResponseEntity<?> scheduleDoctorMeeting(@RequestBody DoctorScheduleRequestDTO request) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			LocalDate appointmentDate = LocalDate.parse(request.getAppointmentDate());
			LocalTime appointmentTime = LocalTime.parse(request.getAppointmentStartTime());
			response = doctorService.isDoctorAvailable(request.getDoctorUuid(), appointmentDate, appointmentTime);

			if (!Boolean.parseBoolean(response.get("isDoctorAvailable").toString())) {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			// Schedule the meeting (insert into database)
			boolean success = doctorService.scheduleMeeting(request.getDoctorUuid(), request.getUserUuid(),
					appointmentDate, appointmentTime,request);

			if (success) {
				response.put("code", 201);
				response.put("message", "successfully scheduled slot");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("code", 400);
				response.put("Error", "Failed to schedule the meeting.");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			// Log error
			logger.error("Error scheduling meeting for doctor UUID: " + request.getDoctorUuid(), e);
			response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put("Error", "Error scheduling meeting for doctor UUID." + request.getDoctorUuid());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
