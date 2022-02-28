package com.example.task.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.task.domain.user.User;
import com.example.task.domain.user.UserRepository;
import com.example.task.model.Role;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {
	
	@Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private String userId = "hong";
    private String password = "1234";
    private String name = "홍길동";
    private String regNo = "860824-1655068";
    
    @Test
    public void selectUserAfterUserSave() throws Exception {
    	
    	userRepository.save(User.builder()
    			.userId(userId)
    			.password(passwordEncoder.encode(password))
    			.name(name)
    			.regNo(regNo)
    			.role(Role.USER)
    			.build());
    	
    	User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("no data"));
    	
    	assertNotNull(user);
    	assertThat(user.getUserId().equals(userId));
    	assertThat(passwordEncoder.matches(password, user.getPassword()));
    	assertThat(user.getName().equals(name));
    	assertThat(user.getRegNo().equals(regNo));
    }
}
