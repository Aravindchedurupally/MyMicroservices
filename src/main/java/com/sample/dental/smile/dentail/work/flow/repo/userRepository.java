package com.sample.dental.smile.dentail.work.flow.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sample.dental.smile.dentail.work.flow.entity.User;

@Repository
public interface userRepository extends JpaRepository<User, Long> {

	@Query(value = "select * from public.users where username = ?1", nativeQuery = true)
	User findByUserName(String userName);

	@Query(value = "select * from public.users where uuid = ?1", nativeQuery = true)
	User findByUUID(String uuid);
}
