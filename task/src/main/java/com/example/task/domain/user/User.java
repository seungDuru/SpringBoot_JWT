package com.example.task.domain.user;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.task.config.converter.AttributeEncryptor;
import com.example.task.dto.user.UserSignupRequestDto;
import com.example.task.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="USER")
public class User {
	
	@Id
    @Column(name = "user_id")
    private String userId;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;
    
    @Column(name = "name")
    private String name;

    @Column(name = "reg_no")
    @Convert(converter = AttributeEncryptor.class)
    private String regNo;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    public User(UserSignupRequestDto request) {
        userId = request.getUserId();
        password = request.getPassword();
        name = request.getName();
        regNo = request.getRegNo();
        role = Role.USER;
    }

    public void encryptPassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

}
