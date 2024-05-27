package org.jsp.usermanagementapp.repository;

import java.util.List;
import java.util.Optional;

import org.jsp.usermanagementapp.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
	List<User> findByName(String name);

	Optional<User> findByPhone(long phone);

	Optional<User> findByPhoneAndPassword(long phone, String password);

	@Query("select u from User u where u.email=?1 and u.password=?2")
	Optional<User> verifyByEmailAndPassword(String email, String password);
}
