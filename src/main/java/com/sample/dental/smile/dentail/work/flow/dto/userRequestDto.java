package com.sample.dental.smile.dentail.work.flow.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class userRequestDto {

	private String userName;
	private String password;
	private String phoneNumber;
	private String firstName;
	private String lastName;

}
