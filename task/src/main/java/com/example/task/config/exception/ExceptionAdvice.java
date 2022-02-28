package com.example.task.config.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.task.config.exception.exception.CExpiredAccessTokenException;
import com.example.task.config.exception.exception.CLoginFailedException;
import com.example.task.config.exception.exception.CSignupFailedException;
import com.example.task.config.exception.exception.CUserExistException;
import com.example.task.config.exception.exception.CUserNotFoundException;
import com.example.task.model.CommonResult;
import com.example.task.service.response.ResponseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
	
	private final ResponseService responseService;
	
	/***
     * -9999
     * default Exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        log.info(String.valueOf(e));
        return responseService.getFailResult(-9999, "알수 없는 오류가 발생하였습니다.");
    }
    
    /***
     * -1000
     * 유저를 찾지 못했을 때 발생시키는 예외
     */
    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(-1000, "존재하지 않는 회원입니다.");
    }

    /***
     * -1001
     * 유저 아이디 로그인 실패 시 발생시키는 예외
     */
    @ExceptionHandler(CLoginFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected CommonResult loginFailedException(HttpServletRequest request, CLoginFailedException e) {
        return responseService.getFailResult(-1001, "가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.");
    }

    /***
     * -1002
     * 회원 가입 시 이미 로그인 된 아이디 경우 발생 시키는 예외
     */
    @ExceptionHandler(CSignupFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult signupFailedException(HttpServletRequest request, CSignupFailedException e) {
        return responseService.getFailResult(-1002, "이미 가입된 아이디입니다");
    }
    
    /***
     * -1003
     * 기 가입자 에러
     */
    @ExceptionHandler(CUserExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected CommonResult existUserException(HttpServletRequest request, CUserExistException e) {
    	return responseService.getFailResult(-1003, "이미 가입된 계정입니다.");
    }
    /**
     * 
     * -1004
     * 액세스 토큰 만료시 발생하는 에러
     */
    @ExceptionHandler(CExpiredAccessTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult expiredAccessTokenException(HttpServletRequest request, CExpiredAccessTokenException e) {
        return responseService.getFailResult(-1004, "액세스 토큰이 만료되었습니다.");
    }
    
    
}
