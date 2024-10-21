package com.sample.dental.smile.dentail.work.flow.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class userResponseDTO {

	private String userName;
	private String password;
	private String phoneNumber;
	private String status;
	private String firstName;
	private String lastName;

}
