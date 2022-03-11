package com.example.task.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.task.domain.income.Income;
import com.example.task.domain.user.User;
import com.example.task.dto.jwt.JwtRequestDto;
import com.example.task.dto.jwt.JwtTokenDto;
import com.example.task.dto.user.UserScrapRequestDto;
import com.example.task.dto.user.UserSignupRequestDto;
import com.example.task.model.SingleResult;
import com.example.task.service.UserService;
import com.example.task.service.response.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = {"USER"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/szs")
public class UserContoller {
	
    private final UserService userService;
    private final ResponseService responseService;
	
    /**
     * 회원가입
     */
	@ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public SingleResult<String> signUp(
    		@ApiParam(value = "회원가입 요청 DTO", required = true)
    		@RequestBody UserSignupRequestDto request) throws Exception {
		
		String userId = userService.signup(request);
    	return responseService.getSingleResult(userId);
    }
	
    /**
     * 로그인
     */
	@ApiOperation(value = "로그인")
    @PostMapping("/login")
    public SingleResult<JwtTokenDto> login(
    		@ApiParam(value = "로그인 요청 DTO", required = true)
    		@RequestBody JwtRequestDto request) throws Exception {
		
		JwtTokenDto jwtTokenDto = userService.login(request);
        return responseService.getSingleResult(jwtTokenDto);
    }
	
	/**
	 * 자기정보 확인
	 */
	@ApiOperation(value = "자기정보 확인")
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public SingleResult<User> showUser(
    		@ApiParam(value = "X-AUTH_TOKEN", required = true) 
    		@RequestParam String accessToken) throws Exception {
		
		JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
				.accessToken(accessToken)
				.build();
		
		return responseService.getSingleResult(userService.showUser(jwtTokenDto));
    }
	
	/**
	 * 정보 스크랩
	 */
	@ApiOperation(value = "정보 스크랩")
	@PostMapping("/scrap")
	public SingleResult<Income> scrap(
			@ApiParam(value = "스크랩 요청 DTO", required = true)
			@RequestBody UserScrapRequestDto request) throws Exception {
		
		return responseService.getSingleResult(userService.scrap(request));
	}
	
	/**
	 * 환급액 조회
	 */
	@ApiOperation(value = "환급액 조회")
	@GetMapping("/refund")
    @PreAuthorize("hasRole('USER')")
    public SingleResult<String> refund(
    		@ApiParam(value = "X-AUTH_TOKEN", required = true)
    		@RequestParam String accessToken) throws Exception {
		
		JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
				.accessToken(accessToken)
				.build();
		
		return responseService.getSingleResult(userService.refund(jwtTokenDto));
    }
}
