package com.example.task.query;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserQuery {
	private final EntityManager em;
	
	public Query findRefundByUserId(String userId) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT NAME AS name                                                                              ");
		sb.append("     , FLOOR(LIMITATION) AS limitation                                                           ");
		sb.append("     , FLOOR(DEDUCTIBLE_AMOUNT) AS deductibleAmount                                              ");
		sb.append("     , LEAST(FLOOR(LIMITATION), FLOOR(DEDUCTIBLE_AMOUNT)) AS refundAmount                        ");
		sb.append("  FROM (                                                                                         ");
		sb.append("        SELECT NAME                                                                              ");
		sb.append("             , CASE WHEN TOTAL_PAYMENT  <= 33000000                                              ");
		sb.append("                    THEN 740000                                                                  ");
		sb.append("                    WHEN TOTAL_PAYMENT  > 33000000 AND TOTAL_PAYMENT  <= 70000000                ");
		sb.append("                    THEN CASE WHEN (740000 - ((TOTAL_PAYMENT  - 33000000) * 0.008)) < 660000     ");
		sb.append("                              THEN 660000                                                        ");
		sb.append("                              ELSE (740000 - ((TOTAL_PAYMENT  - 33000000) * 0.008))              ");
		sb.append("                         END                                                                     ");
		sb.append("                    ELSE CASE WHEN (660000 - ((TOTAL_PAYMENT  - 70000000) * 0.5)) < 500000       ");
		sb.append("                              THEN 500000                                                        ");
		sb.append("                              ELSE (660000 - ((TOTAL_PAYMENT  - 70000000) * 0.5))                ");
		sb.append("                         END                                                                     ");
		sb.append("               END AS LIMITATION                                                                 ");
		sb.append("             , CASE WHEN TOTAL_AMOUNT <= 1300000                                                 ");
		sb.append("                    THEN TOTAL_AMOUNT * 0.55                                                     ");
		sb.append("                    ELSE 715000 + ((TOTAL_AMOUNT - 1300000) * 0.3)                               ");
		sb.append("               END AS DEDUCTIBLE_AMOUNT                                                          ");
		sb.append("          FROM INCOME A                                                                          ");
		sb.append("         WHERE USER_ID = '"+userId+"'                                                            ");
		sb.append("       ) B                                                                                       ");
		
		String sql = sb.toString();
		Query query = em.createNativeQuery(sql);	
		
		return query;
	}
}
