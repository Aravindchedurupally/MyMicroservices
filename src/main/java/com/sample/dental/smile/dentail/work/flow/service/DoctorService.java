package com.sample.dental.smile.dentail.work.flow.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.sample.dental.smile.dentail.work.flow.dto.DoctorRequestDTO;
import com.sample.dental.smile.dentail.work.flow.dto.DoctorResponseDTO;
import com.sample.dental.smile.dentail.work.flow.dto.DoctorScheduleRequestDTO;
import com.sample.dental.smile.dentail.work.flow.entity.Doctor;

public interface DoctorService {

	public List<DoctorResponseDTO> getAllDoctors();

	public DoctorResponseDTO getDoctorByUuid(String doctorUuid);

	public DoctorResponseDTO updateDoctor(String doctorUuid, DoctorRequestDTO doctorRequestDTO);

	public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO);

	public List<DoctorResponseDTO> getTopDoctors();

	public List<DoctorResponseDTO> getSpecializedDoctors();

	public List<DoctorResponseDTO> getTopSpecializedDoctors();

	public Map<String, Object> isDoctorAvailable(String doctorUuid, LocalDate appointmentDate, LocalTime appointmentTime);

	public boolean scheduleMeeting(String doctorUuid, String userUuid, LocalDate appointmentDate,
			LocalTime appointmentTime, DoctorScheduleRequestDTO request);
	

}
