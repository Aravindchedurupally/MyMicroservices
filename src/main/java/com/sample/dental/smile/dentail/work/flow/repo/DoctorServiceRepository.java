package com.sample.dental.smile.dentail.work.flow.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sample.dental.smile.dentail.work.flow.entity.DoctorClientService;

@Repository
public interface DoctorServiceRepository extends JpaRepository<DoctorClientService, Long> {

	@Query(value = "SELECT * FROM public.doctor_service dcs WHERE dcs.doctor_uuid = :doctorUuid AND dcs.appointment_date = :appointmentDate", nativeQuery = true)
	List<DoctorClientService> findBookedSlots(@Param("doctorUuid") String doctorUuid,
			@Param("appointmentDate") LocalDate appointmentDate);

}
