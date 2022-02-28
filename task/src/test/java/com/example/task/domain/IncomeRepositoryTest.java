package com.example.task.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.task.domain.income.Income;
import com.example.task.domain.income.IncomeRepository;


@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class IncomeRepositoryTest {
	@Autowired
	private IncomeRepository incomeRepository;
	
	private String incomeDetail = "급여";
	private Long totalPayment = 24000000L;
	private String workStartDt = "2020.10.03";	
	private String companyName = "(주)활빈당";
	private String name = "홍길동";
	private String regNo = "860824-1655068";
	private String paymentDt = "2020.11.02";
	private String workEndDt = "2020.11.02";
	private String incomeCategory = "근로소득(연간)";
	private String businessNo = "012-34-56789";
	private Long totalAmount = 2000000L;
	
	@Test
	public void selectIncomeAfterIncomeSave() throws Exception {
		
		incomeRepository.save(Income.builder()
				.incomeDetail(incomeDetail)
				.totalPayment(totalPayment)
				.workStartDt(workStartDt)
				.companyName(companyName)
				.name(name)
				.regNo(regNo)
				.paymentDt(paymentDt)
				.workEndDt(workEndDt)
				.incomeCategory(incomeCategory)
				.businessNo(businessNo)
				.totalAmount(totalAmount)
				.build());
		
		Income income = incomeRepository.findByRegNo(regNo).orElseThrow(() -> new IllegalArgumentException("no data"));
		
		assertNotNull(income);
		assertThat(income.getIncomeDetail().equals(incomeDetail));
		assertThat(income.getTotalPayment().equals(totalPayment));
		assertThat(income.getWorkStartDt().equals(workStartDt));
		assertThat(income.getCompanyName().equals(companyName));
		assertThat(income.getName().equals(name));
		assertThat(income.getRegNo().equals(regNo));
		assertThat(income.getPaymentDt().equals(paymentDt));
		assertThat(income.getWorkEndDt().equals(workEndDt));
		assertThat(income.getIncomeCategory().equals(incomeCategory));
		assertThat(income.getBusinessNo().equals(businessNo));
		assertThat(income.getTotalAmount().equals(totalAmount));
	}
	
}
