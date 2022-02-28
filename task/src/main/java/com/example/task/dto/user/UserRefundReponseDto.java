package com.example.task.dto.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
public class UserRefundReponseDto {
	private String name;
	private java.math.BigDecimal limitation;
	private java.math.BigDecimal deductibleAmount;
	private java.math.BigDecimal refundAmount;
}
