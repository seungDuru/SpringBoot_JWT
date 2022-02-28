package com.example.task.contoller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.example.task.config.exception.exception.CUserNotFoundException;
import com.example.task.domain.income.Income;
import com.example.task.domain.user.User;
import com.example.task.domain.user.UserRepository;
import com.example.task.dto.jwt.JwtRequestDto;
import com.example.task.dto.jwt.JwtTokenDto;
import com.example.task.dto.user.UserScrapRequestDto;
import com.example.task.dto.user.UserSignupRequestDto;
import com.example.task.model.Role;
import com.example.task.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
	@Autowired
	Environment env;
	
	@Before
    public void setUp() {
		userRepository.save(User.builder()
				.userId("hong")
				.password(passwordEncoder.encode("password"))
				.name("홍길동")
				.regNo("860824-1655068")
				.role(Role.USER)
				.build());
    }
	
	/**
	 * 로그인 성공 
	 */
	@Test
	public void loginSuccessTest() throws Exception {
		String object = objectMapper.writeValueAsString(JwtRequestDto.builder()
				.userId("hong")
				.password("password")
				.build());
		
		ResultActions actions = mockMvc.perform(post("/szs/login")
				.content(object)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());
				
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.accessToken").exists());
	}
	
	/**
	 * 로그인 실패 
	 */
	@Test
	public void loginFailTest() throws Exception {
		String object = objectMapper.writeValueAsString(JwtRequestDto.builder()
				.userId("hong")
				.password("wrongPassword")
				.build());
		
		ResultActions actions = mockMvc.perform(post("/szs/login")
				.content(object)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());
				
		actions
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(-1001));
	}
	
	/**
	 * 회원가입 성공
	 */
	@Test
	public void signupSuccessTest() throws Exception {
		
		String userId = "doolRi";
		
		String object = objectMapper.writeValueAsString(UserSignupRequestDto.builder()
				.userId(userId)
				.password(passwordEncoder.encode("password"))
				.name("김둘리")
				.regNo("921108-1582816")
				.build());
		
		ResultActions actions = mockMvc.perform(post("/szs/signup")
				.content(object)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());
				
		actions
				.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data").exists());
	}
	
	/**
	 * 회원가입 실패
	 */
	@Test
	public void signupFailTest() throws Exception {
		
		String userId = "hong";
		
		String object = objectMapper.writeValueAsString(UserSignupRequestDto.builder()
				.userId(userId)
				.password(passwordEncoder.encode("password"))
				.name("홍길동")
				.regNo("860824-1655068")
				.build());
		
		ResultActions actions = mockMvc.perform(post("/szs/signup")
				.content(object)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());
				
		actions
				.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(-1002));
	}
	
	/**
	 * 스크랩
	 */
	@Test
	public void scrapTest() throws Exception {
		String object = objectMapper.writeValueAsString(UserScrapRequestDto.builder()
				.name("홍길동")
				.regNo("860824-1655068")
				.build());
		
		ResultActions actions = mockMvc.perform(post("/szs/scrap")
				.content(object)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());
		
		actions
				.andExpect(jsonPath("$.success").value(true))
		        .andExpect(jsonPath("$.code").value(0))
		        .andExpect(jsonPath("$.msg").exists())
		        .andExpect(jsonPath("$.data").exists());
	}
	
	/**
	 * 자기정보 조회
	 */
	@Test
	public void meTest() throws Exception {
		
		String accessToken = this.getTokenAfterLogin();
		
		ResultActions actions = mockMvc.perform(get("/szs/me")
				.param("accessToken", accessToken));
		
		actions
				.andDo(print())
				.andExpect(jsonPath("$.success").value(true))
		        .andExpect(jsonPath("$.code").value(0))
		        .andExpect(jsonPath("$.msg").exists())
		        .andExpect(jsonPath("$.data").exists());
	}
	
	/**
	 * 환급액 조회
	 */
	@Test
	public void refundTest() throws Exception {
		
		if(saveScrapData()) throw new CUserNotFoundException();
		String accessToken = this.getTokenAfterLogin();
		
		ResultActions actions = mockMvc.perform(get("/szs/refund")
				.param("accessToken", accessToken));
		
		actions
				.andDo(print())
				.andExpect(jsonPath("$.success").value(true))
		        .andExpect(jsonPath("$.code").value(0))
		        .andExpect(jsonPath("$.msg").exists())
		        .andExpect(jsonPath("$.data").exists());
	}
	
	
	public String getTokenAfterLogin() throws Exception {
		
		JwtRequestDto jwtRequestDto = JwtRequestDto.builder()
				.userId("hong")
				.password("password")
				.build();
		
		
		JwtTokenDto jwtTokenDto = userService.login(jwtRequestDto);
		
		return jwtTokenDto.getAccessToken();
	}
	
	public boolean saveScrapData() throws Exception {
		
		UserScrapRequestDto userScrapRequestDto = UserScrapRequestDto.builder()
				.name("홍길동")
				.regNo("860824-1655068")
				.build();
		
		Income income = userService.scrap(userScrapRequestDto);
		
		return ObjectUtils.isEmpty(income);
	}
}
