package com.example.task.dto.jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenDto {
	private String accessToken;
}
