package com.example.task.domain.income;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.task.config.converter.AttributeEncryptor;
import com.example.task.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="INCOME")
public class Income {
	
	@Id
	@Column(name = "seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int seq;
	
    @Column(name = "income_detail")
	private String incomeDetail;	//소득내역
	
    @Column(name = "total_payment")
	private Long totalPayment;		//총지급액
	
    @Column(name = "work_start_dt")
	private String workStartDt;		//업무시작일
	
    @Column(name = "company_name")
	private String companyName;		//기업명
	
    @Column(name = "name")
	private String name;			//이름
	
    @Column(name = "reg_no")
    @Convert(converter = AttributeEncryptor.class)
	private String regNo;			//주민등록번호
	
    @Column(name = "payment_dt")
	private String paymentDt;		//지급일
	
    @Column(name = "work_end_dt")
	private String workEndDt;		//업무종료일
	
    @Column(name = "income_category")
	private String incomeCategory;	//소득구분
	
    @Column(name = "business_no")
    @Convert(converter = AttributeEncryptor.class)
	private String businessNo;		//사업자등록번호
	
    @Column(name = "total_amount")
	private Long totalAmount;		//총사용금액
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
}
