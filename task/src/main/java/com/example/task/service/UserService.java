package com.example.task.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.example.task.config.exception.exception.CExpiredAccessTokenException;
import com.example.task.config.exception.exception.CLoginFailedException;
import com.example.task.config.exception.exception.CSignupFailedException;
import com.example.task.config.exception.exception.CUserNotFoundException;
import com.example.task.config.security.JwtTokenProvider;
import com.example.task.config.security.UserDetailsImpl;
import com.example.task.domain.income.Income;
import com.example.task.domain.income.IncomeRepository;
import com.example.task.domain.user.User;
import com.example.task.domain.user.UserRepository;
import com.example.task.dto.jwt.JwtRequestDto;
import com.example.task.dto.jwt.JwtTokenDto;
import com.example.task.dto.user.UserRefundReponseDto;
import com.example.task.dto.user.UserScrapRequestDto;
import com.example.task.dto.user.UserSignupRequestDto;
import com.example.task.model.Role;
import com.example.task.query.UserQuery;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	@Value("${scrap.3o3.url}")
    private String url;
	
	private final UserRepository userRepository;
	private final IncomeRepository incomeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserQuery userQuery;
	
	private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    
    /**
     * ?????????
     */
    @Transactional
    public JwtTokenDto login(JwtRequestDto request) throws Exception {
    	User user = userRepository.findById(request.getUserId())
    			.orElseThrow(CLoginFailedException::new);
    	
    	if(passwordEncoder.matches(request.getPassword(), user.getPassword()))
    		return createJwtToken(request);
    	else 
    		throw new CLoginFailedException();
    }
    
    /**
     * ????????????
     */
    private JwtTokenDto createJwtToken(JwtRequestDto request) {
    	Authentication authentication = authenticationManager.authenticate
    			(new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword()));
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        
        String token = jwtTokenProvider.generateToken(principal);
        
        JwtTokenDto jwtTokenDto = JwtTokenDto.builder().accessToken(token).build();
        
        return jwtTokenDto;
    }
    
    /**
     * ????????????
     */
    @Transactional
    public String signup(UserSignupRequestDto request) throws Exception {
    	boolean existMember = userRepository.existsById(request.getUserId());

        if (existMember) throw new CSignupFailedException();
        
        userRepository.save(User.builder()
        		.userId(request.getUserId())
        		.password(passwordEncoder.encode(request.getPassword()))
        		.name(request.getName())
        		.regNo(request.getRegNo())
        		.role(Role.USER)
        		.build());
        
        return request.getUserId();
    }
    
    /**
     * ???????????? ??????
     */
    @Transactional(readOnly = true)
    public User showUser(JwtTokenDto jwtTokenDto) throws Exception {
    	if(ObjectUtils.isEmpty(jwtTokenDto)) throw new CExpiredAccessTokenException();
    	
    	//???????????? PK ??????
    	String userId = jwtTokenProvider.getUserPk(jwtTokenDto.getAccessToken());
    	
    	/*
    	User user = userRepository.findById(userId)
    			.orElseThrow(CUserNotFoundException::new);
    	
    	UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.builder()
    			.userId(user.getUserId())
    			.name(user.getName())
    			.regNo(user.getRegNo())
    			.build();
    	*/
    	
    	return userRepository.findById(userId)
    			.orElseThrow(CUserNotFoundException::new);
    }
    
    /**
     * ????????? ??????
     */
    @Transactional(readOnly = true)
    public String refund(JwtTokenDto jwtTokenDto) throws Exception {
    	if(ObjectUtils.isEmpty(jwtTokenDto)) throw new CExpiredAccessTokenException();
    	
    	//???????????? PK ??????
    	String userId = jwtTokenProvider.getUserPk(jwtTokenDto.getAccessToken());
    	
    	//Native Query ???????????? QLRM?????? ??????
    	JpaResultMapper jpaResultMapper = new JpaResultMapper();
    	List<UserRefundReponseDto> returnList = jpaResultMapper.list(userQuery.findRefundByUserId(userId), UserRefundReponseDto.class);
    	
    	return refundDataSet(returnList);
    }

	/**
	 * ?????? ????????? ??? income ????????? ??????
	 */
    @Transactional
	public Income scrap(UserScrapRequestDto request) throws Exception {
		//????????? ??? json?????? ????????????
		JSONObject json = new JSONObject(getScrapData(request));
		//?????? ????????? ??????
		Income income = dataSet(json);		
		
		if(!ObjectUtils.isEmpty(income)) {
			User user = userRepository.findByRegNo(income.getRegNo())
	    			.orElseThrow(CUserNotFoundException::new);
			//??????????????? ??????
			income.setUser(user);
			incomeRepository.save(income);
		}
		/*
		else {
			//null??? ?????? exception
		}
		*/
		return incomeRepository.findByRegNo(income.getRegNo())
				.orElseThrow(CUserNotFoundException::new);
	}
	
	/**
	 * url json ????????? ????????????
	 */
	private String getScrapData(UserScrapRequestDto request) throws Exception {
		//????????? url ??????
		URL object = new URL(url);
		
		//HttpURLConnection ??????
		HttpURLConnection con = (HttpURLConnection) object.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");

		JSONObject cred   = new JSONObject();
		
		//????????? ?????? ??????
		cred.put("name", request.getName());
		cred.put("regNo", request.getRegNo());

		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(cred.toString());
		wr.flush();
		//????????? ?????? StringBuilder??? ?????? ??????
		StringBuilder sb = new StringBuilder();
		int HttpResult = con.getResponseCode();
		if (HttpResult == HttpURLConnection.HTTP_OK) {
		    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
		    String line = null;
		    while ((line = br.readLine()) != null) {
		        sb.append(line + "\n");
		    }
		    br.close();
		    return sb.toString();
		} else {
		    throw new Exception(con.getResponseMessage());  
		}  
	}
	
	/**
	 * ???????????? ????????? ??????
	 */
	@SuppressWarnings("unchecked")
	private Income dataSet(JSONObject json) throws Exception {
		
		Income income1 = new Income();
		Income income2 = new Income();
		
		String[] arr = {"scrap001", "scrap002"};
		
		JSONObject jsonList = (JSONObject) json.get("jsonList");
		JSONObject joScrap = new JSONObject();
		
		for(String scrap : arr) {
			joScrap = (JSONObject) jsonList.getJSONArray(scrap).get(0);
		
			if(!ObjectUtils.isEmpty(joScrap) ) {
				Iterator<String> i = joScrap.keys();
				
				if("scrap001".equals(scrap)) {
					while(i.hasNext()){
						String key = i.next().toString();
						if("????????????".equals(key)) income1.setIncomeDetail(String.valueOf(joScrap.get(key)));
						if("????????????".equals(key)) income1.setTotalPayment(Long.parseLong(String.valueOf(joScrap.get(key))));
						if("???????????????".equals(key)) income1.setWorkStartDt(String.valueOf(joScrap.get(key)));
						if("?????????".equals(key)) income1.setCompanyName(String.valueOf(joScrap.get(key)));
						if("??????".equals(key)) income1.setName(String.valueOf(joScrap.get(key)));
						if("?????????".equals(key)) income1.setPaymentDt(String.valueOf(joScrap.get(key)));
						if("???????????????".equals(key)) income1.setWorkEndDt(String.valueOf(joScrap.get(key)));
						if("??????????????????".equals(key)) income1.setRegNo(String.valueOf(joScrap.get(key)));
						if("????????????".equals(key)) income1.setIncomeCategory(String.valueOf(joScrap.get(key)));
						if("?????????????????????".equals(key)) income1.setBusinessNo(String.valueOf(joScrap.get(key)));
					}
				}
				
				if("scrap002".equals(scrap)) {
					income2 = income1;
					while(i.hasNext()){
						String key = i.next().toString();
						if("???????????????".equals(key)) income2.setTotalAmount(Long.parseLong(String.valueOf(joScrap.get(key))));
					}
				}
			}
		}
		
		return income2;
	}
	
	/**
	 * ???????????? ????????? ??????
	 */
	private String refundDataSet(List<UserRefundReponseDto> list) throws Exception{
		
		UserRefundReponseDto userRefund = list.get(0);
		
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("??????", userRefund.getName());
    	data.put("??????", numberToKoean(userRefund.getLimitation().intValue()));
    	data.put("?????????", numberToKoean(userRefund.getDeductibleAmount().intValue()));
    	data.put("?????????", numberToKoean(userRefund.getRefundAmount().intValue()));
    	
    	String json = new ObjectMapper().writeValueAsString(data);
    	
    	return json;
	}
	
	/**
	 * ?????? ????????? convert
	 */
	private String numberToKoean(int number) {
		
		String[] unitWords  = {"???", "???"};
		int splitCount      = unitWords.length;
		int splitUnit       = 10000;
		String[] resultArr  = new String[splitCount];
		String resultStr    = "";
		
		for(int i = 0; i < splitCount; i++) {
			int unitResult = (int) ((number % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i));
			if(unitResult > 0) {
				resultArr[i] = String.valueOf(unitResult);
			}
		}
		
		for(int i = 0; i < splitCount; i++) {
			if(resultArr[i] == null || resultArr[i].isEmpty()) continue;
			resultStr = resultArr[i] + unitWords[i] + resultStr;
		}
		
		return resultStr;
	}
	

}
