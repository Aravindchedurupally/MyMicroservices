package com.sample.dental.smile.dentail.work.flow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Long ID, auto-generated
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private String uuid = UUID.randomUUID().toString(); // UUID generated as String, unique, and immutable

    @Column(name = "username", nullable = false, unique = true)
    @Email(message = "Username should be a valid email address")
    @NotNull(message = "Username cannot be null")
    private String userName;
    
    @Column(name = "first_name", nullable = false, unique = true)
    @NotNull(message = "firstName cannot be null")
    private String firstName;
    
    @Column(name = "last_name", nullable = false, unique = true)
    @NotNull(message = "lastName cannot be null")
    private String lastName;

    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "password", nullable = false)
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;

    @Column(name = "phone_number", nullable = false, unique = true)
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "status", nullable = false)
    private String status;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

