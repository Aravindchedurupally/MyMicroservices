package com.sample.dental.smile.dentail.work.flow.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DoctorScheduleRequestDTO {

	private String doctorUuid;
	private String userUuid;
	private String appointmentDate;
	private String appointmentStartTime;
    private String symptoms;
    private String description;

}
