package com.sample.dental.smile.dentail.work.flow.serviceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sample.dental.smile.dentail.work.flow.dto.DoctorRequestDTO;
import com.sample.dental.smile.dentail.work.flow.dto.DoctorResponseDTO;
import com.sample.dental.smile.dentail.work.flow.dto.DoctorScheduleRequestDTO;
import com.sample.dental.smile.dentail.work.flow.entity.Doctor;
import com.sample.dental.smile.dentail.work.flow.entity.DoctorClientService;
import com.sample.dental.smile.dentail.work.flow.entity.User;
import com.sample.dental.smile.dentail.work.flow.exception.ResourceNotFoundException;
import com.sample.dental.smile.dentail.work.flow.repo.DoctorRepository;
import com.sample.dental.smile.dentail.work.flow.repo.DoctorServiceRepository;
import com.sample.dental.smile.dentail.work.flow.repo.userRepository;
import com.sample.dental.smile.dentail.work.flow.service.DoctorService;

@Service
public class DoctorServiceImpl implements DoctorService {

	private static final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

	@Autowired
	DoctorRepository doctorRepository;

	@Autowired
	DoctorServiceRepository doctorServiceRepository;
	
	@Autowired
	userRepository userRepository;

	@Override
	public List<DoctorResponseDTO> getAllDoctors() {
		List<Doctor> doctors = doctorRepository.findAll();
		logger.info("Retrieved {} doctors from the database.", doctors.size());
		return doctors.stream().map(this::mapToDoctorResponseDTO).collect(Collectors.toList());
	}

	@Override
	public DoctorResponseDTO getDoctorByUuid(String doctorUuid) {
		Optional<Doctor> doctorOptional = doctorRepository.findByDoctorUuid(doctorUuid);
		if (doctorOptional.isPresent()) {
			logger.info("Doctor found with UUID: {}", doctorUuid);
			return mapToDoctorResponseDTO(doctorOptional.get());
		} else {
			logger.error("Doctor with UUID {} not found.", doctorUuid);
			throw new ResourceNotFoundException("Doctor not found with UUID: " + doctorUuid);
		}
	}

	@Override
	public DoctorResponseDTO updateDoctor(String doctorUuid, DoctorRequestDTO doctorRequestDTO) {
		Optional<Doctor> doctorOptional = doctorRepository.findByDoctorUuid(doctorUuid);
		if (!doctorOptional.isPresent()) {
			logger.error("Doctor with UUID {} not found.", doctorUuid);
			throw new ResourceNotFoundException("Doctor not found with UUID: " + doctorUuid);
		}
		Doctor doctor = doctorOptional.get();
		doctor.setFirstName(doctorRequestDTO.getFirstName());
		doctor.setLastName(doctorRequestDTO.getLastName());
		doctor.setSpecialization(doctorRequestDTO.getSpecialization());
		doctor.setExperienceYears(doctorRequestDTO.getExperienceYears());
		doctor.setEmail(doctorRequestDTO.getEmail());
		doctor.setPhoneNumber(doctorRequestDTO.getPhoneNumber());
		doctor.setDoctorAvailable(doctorRequestDTO.isDoctorAvailable());
		doctor.setImage(doctorRequestDTO.getImage());
		doctor.setTopDoctor(doctorRequestDTO.isTopDoctor());
		doctor.setSpecialized(doctorRequestDTO.isSpecialized());
		Doctor updatedDoctor = doctorRepository.save(doctor);
		logger.info("Doctor with UUID: {} updated successfully.", doctorUuid);
		return mapToDoctorResponseDTO(updatedDoctor);

	}

	@Override
	public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO) {
		Doctor doctor = new Doctor();
		doctor.setDoctorAvailable(doctorRequestDTO.isDoctorAvailable());
		doctor.setFirstName(doctorRequestDTO.getFirstName());
		doctor.setLastName(doctorRequestDTO.getLastName());
		doctor.setSpecialization(doctorRequestDTO.getSpecialization());
		doctor.setExperienceYears(doctorRequestDTO.getExperienceYears());
		doctor.setEmail(doctorRequestDTO.getEmail());
		doctor.setPhoneNumber(doctorRequestDTO.getPhoneNumber());
		doctor.setDoctorUuid(UUID.randomUUID().toString());
		doctor.setImage(doctorRequestDTO.getImage());
		doctor.setTopDoctor(doctorRequestDTO.isTopDoctor());
		doctor.setSpecialized(doctorRequestDTO.isSpecialized());
		Doctor savedDoctor = doctorRepository.save(doctor);
		logger.info("Doctor created and saved to database with UUID: {}", savedDoctor.getDoctorUuid());
		return mapToDoctorResponseDTO(savedDoctor);
	}

	private DoctorResponseDTO mapToDoctorResponseDTO(Doctor doctor) {
		return DoctorResponseDTO.builder().doctorUuid(doctor.getDoctorUuid()).firstName(doctor.getFirstName())
				.lastName(doctor.getLastName()).specialization(doctor.getSpecialization())
				.experienceYears(doctor.getExperienceYears()).email(doctor.getEmail())
				.phoneNumber(doctor.getPhoneNumber()).doctorAvailable(doctor.isDoctorAvailable())
				.isTopDoctor(doctor.isTopDoctor()).specialized(doctor.isSpecialized()).image(doctor.getImage())
				.workTimmingPerDay(doctor.getWorkTimmingPerDay()).build();
	}

	@Override
	public List<DoctorResponseDTO> getTopDoctors() {
		return doctorRepository.findByTopDoctor(true).stream().map(this::mapToDoctorResponseDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<DoctorResponseDTO> getSpecializedDoctors() {
		return doctorRepository.findBySpecialized(true).stream().map(this::mapToDoctorResponseDTO)
				.collect(Collectors.toList());

	}

	@Override
	public List<DoctorResponseDTO> getTopSpecializedDoctors() {
		return doctorRepository.findByTopDoctorAndSpecialized(true, true).stream().map(this::mapToDoctorResponseDTO)
				.collect(Collectors.toList());

	}

	@Override
	public Map<String, Object> isDoctorAvailable(String doctorUuid, LocalDate appointmentDate,
			LocalTime appointmentTime) {
		Map<String, Object> response = new HashMap<String, Object>();
		// Define default doctor working hours
		LocalTime startWork = LocalTime.of(10, 0); // 10:00 AM
		LocalTime endWork = LocalTime.of(14, 0); // 02:00 PM

		// Check if the appointment is within working hours
		if (appointmentTime.isBefore(startWork) || appointmentTime.plusMinutes(30).isAfter(endWork)) {
			response.put("code", 202);
			response.put("message", "Choosed timming is Not within working hours");
			response.put("isDoctorAvailable", false);
			return response; // Not within working hours
		}

		// Check if the doctor is already booked for that time slot
		List<DoctorClientService> bookedSlots = doctorServiceRepository.findBookedSlots(doctorUuid, appointmentDate);
		if (bookedSlots == null || bookedSlots.isEmpty()) {
			response.put("code", 200);
			response.put("message", "Doctor is available for slot");
			response.put("isDoctorAvailable", true);
			return response;
		}
		for (DoctorClientService slot : bookedSlots) {
			if (appointmentTime.isBefore(slot.getAppointmentEndTime())
					&& appointmentTime.plusMinutes(30).isAfter(slot.getAppointmentStartTime())) {
				response.put("code", 202);
				response.put("message", "Slot overlaps with an existing booking");
				response.put("isDoctorAvailable", false);
				return null; // Slot overlaps with an existing booking
			}
		}
		response.put("code", 200);
		response.put("message", "Choosed timming is Not within working hours");
		response.put("isDoctorAvailable", true);
		return response;
	}

	@Override
	public boolean scheduleMeeting(String doctorUuid, String userUuid, LocalDate appointmentDate,
			LocalTime appointmentTime,DoctorScheduleRequestDTO request) {
		try {
			if (!validUser(userUuid) && !validDoctor(doctorUuid)) return false;
			DoctorClientService newAppointment = new DoctorClientService();
			newAppointment.setDoctorUuid(doctorUuid);
			newAppointment.setUserUuid(userUuid);
			newAppointment.setAppointmentStartTime(appointmentTime); // Already a LocalTime instance
			newAppointment.setAppointmentEndTime(appointmentTime.plusMinutes(30)); // Adds 30 minutes to start time
			newAppointment.setAppointmentDate(appointmentDate); // Already a LocalDate instance
			newAppointment.setStatus("Scheduled");
			newAppointment.setSymptoms(request.getSymptoms());
			newAppointment.setDescription(request.getDescription());
			doctorServiceRepository.save(newAppointment);
			return true;
		} catch (Exception e) {
			logger.error("Error scheduling appointment for doctor UUID: " + doctorUuid, e);
			return false;
		}
	}
	
	public boolean validUser(String uuid) {
		return userRepository.findByUUID(uuid) != null;
	}

	public boolean validDoctor(String uuid) {
		return doctorRepository.findByUUID(uuid) != null;
	}
}
