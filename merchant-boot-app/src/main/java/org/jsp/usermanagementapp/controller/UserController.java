package org.jsp.usermanagementapp.controller;

import java.util.List;
import java.util.Optional;

import org.jsp.usermanagementapp.dto.User;
import org.jsp.usermanagementapp.dto.ResponseStructure;
import org.jsp.usermanagementapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@PostMapping
	public ResponseStructure<User> saveUser(@RequestBody User merchant) {
		ResponseStructure<User> structure = new ResponseStructure<>();
		structure.setMessage("User Saved");
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setData(userRepository.save(merchant));
		return structure;
	}

	@GetMapping("/{id}")
	public ResponseStructure<User> findById(@PathVariable(name = "id") int id) {
		ResponseStructure<User> structure = new ResponseStructure<>();
		Optional<User> recUser = userRepository.findById(id);
		if (recUser.isPresent()) {
			structure.setMessage("Merchant Found");
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setData(recUser.get());
			return structure;
		}
		structure.setData(null);
		structure.setMessage("User not found as id is invalid");
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		return structure;
	}

	@PutMapping
	public User updateUser(@RequestBody User user) {
		Optional<User> recUser = userRepository.findById(user.getId());
		if (recUser.isPresent()) {
			User dbUser = recUser.get();
			dbUser.setEmail(user.getEmail());
			dbUser.setGst_number(user.getGst_number());
			dbUser.setName(user.getName());
			dbUser.setPassword(user.getPassword());
			dbUser.setPhone(user.getPhone());
			userRepository.save(dbUser);
			return dbUser;
		}
		return null;
	}

	@GetMapping
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable(name = "id") int id) {
		Optional<User> recUser = userRepository.findById(id);
		if (recUser.isPresent()) {
			userRepository.delete(recUser.get());
			return "Merchant deleted";
		}
		return "Cannot delete merchant as id is invalid";
	}

	@GetMapping("/find-by-name/{name}")
	public ResponseStructure<List<User>> findByName(@PathVariable(name = "name") String name) {
		ResponseStructure<List<User>> structure = new ResponseStructure<>();
		List<User> users = userRepository.findByName(name);
		if (users.isEmpty()) {
			structure.setData(null);
			structure.setMessage("User not found as name is invalid");
			structure.setStatusCode(HttpStatus.NOT_FOUND.value());
			return structure;
		}
		structure.setMessage("User Found");
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setData(users);
		return structure;
	}

	@GetMapping("/find-by-phone/{phone}")
	public ResponseStructure<User> findByphone(@PathVariable(name = "phone") long phone) {
		ResponseStructure<User> structure = new ResponseStructure<>();
		Optional<User> recUser = userRepository.findByPhone(phone);
		if (recUser.isPresent()) {
			structure.setMessage("User Found");
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setData(recUser.get());
			return structure;
		}
		structure.setData(null);
		structure.setMessage("User not found as phone number is invalid");
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		return structure;
	}

	@PostMapping("/verify-by-phone")
	public ResponseStructure<User> verifyUser(@RequestParam(name = "phone") long phone,
			@RequestParam(name = "password") String password) {
		ResponseStructure<User> structure = new ResponseStructure<>();
		Optional<User> recUser = userRepository.findByPhoneAndPassword(phone, password);
		if (recUser.isPresent()) {
			structure.setMessage("User Found");
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setData(recUser.get());
			return structure;
		}
		structure.setData(null);
		structure.setMessage("User not found as phone number or password is invalid");
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		return structure;
	}

	@PostMapping("/verify-by-email")
	public ResponseStructure<User> verifyUser(@RequestParam(name = "email") String email,
			@RequestParam(name = "password") String password) {
		ResponseStructure<User> structure = new ResponseStructure<>();
		Optional<User> recUser = userRepository.verifyByEmailAndPassword(email, password);
		if (recUser.isPresent()) {
			structure.setMessage("User Found");
			structure.setStatusCode(HttpStatus.OK.value());
			structure.setData(recUser.get());
			return structure;
		}
		structure.setData(null);
		structure.setMessage("User not found as email id or password is invalid");
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		return structure;
	}
}
