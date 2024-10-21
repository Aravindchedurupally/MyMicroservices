package com.sample.dental.smile.dentail.work.flow.authToken;

public class JwtDetails {
	private String firstName;
	private String lastName;
	private String patientId;
	private String email;

	// Constructor, Getters, and Setters
	public JwtDetails(String firstName, String lastName, String patientId, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.patientId = patientId;
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPatientId() {
		return patientId;
	}

	public String getEmail() {
		return email;
	}
}
