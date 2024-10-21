package com.sample.dental.smile.dentail.work.flow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "doctor", schema = "public")
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "doctor_uuid", unique = true, nullable = false)
	private String doctorUuid;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "specialization", nullable = false)
	private String specialization;

	@Column(name = "experience_years")
	private int experienceYears;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "doctor_available", nullable = false)
	private boolean doctorAvailable;

	@Column(name = "total_working_hours_per_day", nullable = false)
	private int totalWorkingHoursPerDay = 4; // Doctors work 4 hours per day
	
	@Column(name = "work_timming_per_day", nullable = false)
	private String workTimmingPerDay = "10AM - 2PM"; 

	@Column(name = "is_top_doctor", nullable = false)
	private boolean isTopDoctor;

	@Column(name = "is_specialized", nullable = false)
	private boolean specialized;

	@Column(name = "image")
	private byte[] image;
	
	public String getWorkTimmingPerDay() {
		return workTimmingPerDay;
	}

	public void setWorkTimmingPerDay(String workTimmingPerDay) {
		this.workTimmingPerDay = workTimmingPerDay;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Long getId() {
		return id;
	}

	public boolean isTopDoctor() {
		return isTopDoctor;
	}

	public void setTopDoctor(boolean isTopDoctor) {
		this.isTopDoctor = isTopDoctor;
	}

	public boolean isSpecialized() {
		return specialized;
	}

	public void setSpecialized(boolean specialized) {
		this.specialized = specialized;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public int getExperienceYears() {
		return experienceYears;
	}

	public void setExperienceYears(int experienceYears) {
		this.experienceYears = experienceYears;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isDoctorAvailable() {
		return doctorAvailable;
	}

	public void setDoctorAvailable(boolean doctorAvailable) {
		this.doctorAvailable = doctorAvailable;
	}

	public int getTotalWorkingHoursPerDay() {
		return totalWorkingHoursPerDay;
	}

	public void setTotalWorkingHoursPerDay(int totalWorkingHoursPerDay) {
		this.totalWorkingHoursPerDay = totalWorkingHoursPerDay;
	}

}
