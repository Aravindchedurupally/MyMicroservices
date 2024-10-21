package com.sample.dental.smile.dentail.work.flow.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
public class DoctorRequestDTO {

	private String firstName;
	private String lastName;
	private String specialization;
	private int experienceYears;
	private String email;
	private String phoneNumber;
	private boolean doctorAvailable;
	private boolean isTopDoctor;
	private boolean specialized;
	private byte[] image;
	private String workTimmingPerDay;
}
