package com.example.task.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {
	private String userId;
    private String password;
    private String name;
    private String regNo;
}
