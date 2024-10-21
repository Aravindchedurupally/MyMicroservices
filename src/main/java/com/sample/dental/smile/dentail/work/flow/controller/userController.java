package com.sample.dental.smile.dentail.work.flow.controller;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.dental.smile.dentail.work.flow.authToken.JwtTokenUtil;
import com.sample.dental.smile.dentail.work.flow.dto.userRequestDto;
import com.sample.dental.smile.dentail.work.flow.dto.userResponseDTO;
import com.sample.dental.smile.dentail.work.flow.entity.User;
import com.sample.dental.smile.dentail.work.flow.exception.HandleCustomExceptionResponse;
import com.sample.dental.smile.dentail.work.flow.password.dec.PasswordEncryptDecrypt;
import com.sample.dental.smile.dentail.work.flow.repo.userRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.validation.ConstraintViolationException;

@RequestMapping("/api/auth")
@RestController
public class userController {

	private static final Logger logger = LoggerFactory.getLogger(userController.class);

	@Autowired
	userRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	HandleCustomExceptionResponse exceptionResponseTemplate = new HandleCustomExceptionResponse();

	@PostMapping("/register")
	public ResponseEntity<?> userRegistration(@RequestBody userRequestDto userRequestDto) {
		Map<String, Object> response = new HashMap<>();
		try {
			// Log the start of the registration process
			logger.info("Starting user registration for username: {}", userRequestDto.getUserName());

			// Check if the user already exists in the database
			User existingUser = userRepository.findByUserName(userRequestDto.getUserName());
			if (existingUser != null) {
				logger.warn("User with username {} already exists", userRequestDto.getUserName());
				response.put("message", "User already exists with the provided username");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

			// Create a new user
			User newUser = new User();
			newUser.setUserName(userRequestDto.getUserName());
			newUser.setFirstName(userRequestDto.getFirstName());
			newUser.setLastName(userRequestDto.getLastName());
			newUser.setPassword(PasswordEncryptDecrypt.encrypt(userRequestDto.getPassword()));
			newUser.setPhoneNumber(userRequestDto.getPhoneNumber()); // Set additional fields
			newUser.setCreatedDate(LocalDateTime.now()); // Set creation date
			newUser.setStatus("ACTIVE"); // Set initial user status

			// Save the user to the database
			userRepository.save(newUser);

			logger.info("User {} successfully registered", userRequestDto.getUserName());

			// Prepare successful response
			response.put("message", "User successfully registered");
			response.put("userId", newUser.getId());
			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (ConstraintViolationException e) {
			logger.error("Validation error during user registration for username: {}", userRequestDto.getUserName(), e);
			// Handle validation exception and return specific validation messages
			response.put("message", "Validation failed");
			response.put("error", exceptionResponseTemplate.handleValidationExceptionResponse(e));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			logger.error("Error during user registration for username: {}", userRequestDto.getUserName(),
					e.getLocalizedMessage());
			response.put("message", "User registration failed");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody userRequestDto userDetails) {
		logger.info("Login attempt for user: {}", userDetails.getUserName());
		User user = userRepository.findByUserName(userDetails.getUserName());
		String token = null;

		try {
			if (user == null) {
				logger.warn("User not found: {}", userDetails.getUserName());
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
			}

			String rawPassword = PasswordEncryptDecrypt.encrypt(userDetails.getPassword());
			logger.debug("Raw password encrypted for user: {}", userDetails.getUserName());

			if (checkPassword(rawPassword, user.getPassword())) {
				token = generateToken(user);
				Map<String, String> response = new HashMap<>();
				response.put("token", token);
				logger.info("User {} logged in successfully.", userDetails.getUserName());
				return ResponseEntity.ok(response);
			} else {
				logger.warn("Invalid credentials for user: {}", userDetails.getUserName());
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
			}
		} catch (Exception e) {
			logger.error("Error during login for user {}: {}", userDetails.getUserName(), e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}

	public boolean checkPassword(String rawPassword, String encodedPassword) {
		boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
		logger.debug("Password match result for user: {} is {}", rawPassword, matches);
		return matches;
	}

	public String generateToken(User userDetails) {
		Map<String, Object> userDetailsMap = new HashMap<>();
		userDetailsMap.put("email", userDetails.getUserName());
		userDetailsMap.put("patientId", userDetails.getUuid());
		userDetailsMap.put("firstName", userDetails.getFirstName());
		userDetailsMap.put("lastName", userDetails.getLastName());
		String token = jwtTokenUtil.generateToken(userDetails.getUserName(), userDetailsMap);
		logger.debug("Token generated for user: {}", userDetails.getUserName());
		return token;
	}

	@GetMapping("/profile")
	public ResponseEntity<?> fetchProfileByUserId(@RequestParam String userId) {
		Map<String, Object> response = new HashMap<>();

		try {
			logger.info("Fetching profile for userId: {}", userId);
			// Fetch user data from the repository
			User userData = userRepository.findByUserName(userId);
			// If no user is found
			if (userData == null) {
				logger.warn("No user found with userId: {}", userId);
				response.put("code", "404");
				response.put("message", "User not found");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			// If user is found
			logger.info("User found: {}", userData.getUserName());

			userResponseDTO responseBody = userResponseDTO.builder().userName(userData.getUserName())
					.password(userData.getPassword()) // Consider not returning the password for security reasons
					.phoneNumber(userData.getPhoneNumber()).status(userData.getStatus())
					.firstName(userData.getFirstName()).lastName(userData.getLastName()).build();

			response.put("code", "200");
			response.put("message", "Success");
			response.put("data", responseBody);

			logger.info("Profile successfully fetched for userId: {}", userId);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			// Log the error
			logger.error("An error occurred while fetching the profile for userId: {}", userId, e);
			// Build error response
			response.put("code", "500");
			response.put("message", "Internal Server Error");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/profile")
	public ResponseEntity<?> updateProfileById(@RequestBody userRequestDto request) {
		Map<String, Object> response = new HashMap<>();

		try {
			String userId = request.getUserName();
			logger.info("Fetching profile for userId: {}", userId);
			// Fetch user data from the repository
			User userData = userRepository.findByUserName(userId);
			// If no user is found
			if (userData == null) {
				logger.warn("No user found with userId: {}", userId);
				response.put("code", "404");
				response.put("message", "User not found");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			// If user is found
			logger.info("User found: {}", userData.getUserName());
			// Update user details
			userData.setFirstName(request.getFirstName());
			userData.setLastName(request.getLastName());
			userData.setPhoneNumber(request.getPhoneNumber());
			userData.setUpdateDate(LocalDateTime.now());
			// Save the updated user data
			userRepository.save(userData);

			// Prepare response after successful update
			response.put("code", "200");
			response.put("message", "User profile updated successfully");
			response.put("data", userData); // Only send necessary data (exclude sensitive information)

			logger.info("Profile successfully updated for userId: {}", userId);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			// Log the error
			logger.error("An error occurred while updating the profile for userId: {}", request.getUserName(), e);

			// Build error response
			response.put("code", "500");
			response.put("message", "Internal Server Error");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/verification/gate")
	public ResponseEntity<?> verificationGate(@RequestParam String senderMobileNumber) {
		Map<String, Object> response = new HashMap<>();
		String otp = generateOTP(6);
		String messageContent = "Your OTP to verify your account is: " + otp;
		try {
			Message message = Message.creator(new PhoneNumber(senderMobileNumber), // To number
					new PhoneNumber(senderMobileNumber), // From Twilio number
					messageContent) // Message content
					.create();

			System.out.println("OTP sent successfully! Message SID: " + message.getSid());
			response.put("message", "success");
			response.put("code", 201);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			System.out.println("Failed to send OTP: " + e.getMessage());
			response.put("message", "success");
			response.put("code", 400);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	// Function to generate a random OTP of the specified length
	public static String generateOTP(int length) {
		String numbers = "0123456789";
		Random random = new Random();
		StringBuilder otp = new StringBuilder();

		for (int i = 0; i < length; i++) {
			otp.append(numbers.charAt(random.nextInt(numbers.length())));
		}
		return otp.toString();
	}
}