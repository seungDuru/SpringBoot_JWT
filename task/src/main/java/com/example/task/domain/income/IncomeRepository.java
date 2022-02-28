package com.example.task.domain.income;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Integer> {
	Optional<Income> findByRegNo(String regNo);
}
