package com.sample.dental.smile.dentail.work.flow.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "doctor_service", schema = "public")
public class DoctorClientService {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "doctor_uuid", nullable = false)
	private String doctorUuid;

	@Column(name = "user_uuid", nullable = false)
	private String userUuid;

	@Column(name = "appointment_date", nullable = false)
	private LocalDate appointmentDate;

	@Column(name = "appointment_start_time")
	private LocalTime appointmentStartTime;

	@Column(name = "appointment_end_time")
	private LocalTime appointmentEndTime;

	@Column(name = "status", nullable = false)
	private String status; // e.g., "Scheduled", "Completed", "Cancelled"
	
	@Column(name = "symptoms")
    private String symptoms;
    
	@Column(name = "description")
    private String description;
	

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDoctorUuid() {
		return doctorUuid;
	}

	public void setDoctorUuid(String doctorUuid) {
		this.doctorUuid = doctorUuid;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	
	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public LocalTime getAppointmentStartTime() {
		return appointmentStartTime;
	}

	public void setAppointmentStartTime(LocalTime appointmentStartTime) {
		this.appointmentStartTime = appointmentStartTime;
	}

	public LocalTime getAppointmentEndTime() {
		return appointmentEndTime;
	}

	public void setAppointmentEndTime(LocalTime appointmentEndTime) {
		this.appointmentEndTime = appointmentEndTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
