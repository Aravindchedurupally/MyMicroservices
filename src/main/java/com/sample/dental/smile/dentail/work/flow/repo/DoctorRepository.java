package com.sample.dental.smile.dentail.work.flow.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sample.dental.smile.dentail.work.flow.entity.Doctor;
import com.sample.dental.smile.dentail.work.flow.entity.User;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

	Optional<Doctor> findByDoctorUuid(String doctorUuid);
	
	@Query(value = "select * from public.doctor where email = ?1", nativeQuery = true)
	Doctor findByEmail(String email);
	// Method to find top doctors
	@Query(value = "select * from public.doctor where is_top_doctor = ?1", nativeQuery = true)
	List<Doctor> findByTopDoctor(boolean topDoctor);

	// Method to find specialized doctors
	@Query(value = "select * from public.doctor where is_specialized = ?1", nativeQuery = true)
	List<Doctor> findBySpecialized(boolean specialized);

	@Query(value = "select * from public.doctor where is_top_doctor = ?1 AND is_specialized = ?2", nativeQuery = true)
	List<Doctor> findByTopDoctorAndSpecialized(boolean topDoctor, boolean specialized);

	@Query(value = "select * from public.doctor where doctor_uuid= ?1", nativeQuery = true)
	Doctor findByUUID(String uuid);

}
