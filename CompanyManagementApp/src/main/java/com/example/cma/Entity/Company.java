package com.example.cma.Entity;

import java.util.List;
import java.util.Set;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Company {
	
	@Id
	@NotNull(message="Mandatory")
	@Column(name="companyCode",unique=true)
	private Long companyCode;
	@NotEmpty(message="Mandatory")
	private String companyName;
	@NotEmpty(message="Mandatory")
	private String companyCEO;
	@NotNull(message="Mandatory")
	@Min(value=100000000, message="Company Turnover should be greater than 10Cr.")
	private Long companyTurnover;
	@NotEmpty(message="Mandatory")
	private String companyWebsite;
	@Column(name="stockExchange",nullable = false)
	private List<String> stockExchange;
	@OneToMany(targetEntity= Stock.class)
	private Set<Stock> stock;
	
}

//@OneToMany(mappedBy ="company", cascade = CascadeType.ALL, orphanRemoval = true)
//private List<Stock> stocks;
