package com.example.cma.pojo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CompanyOutputModel {
		private Long companyCode;
		private String companyName;
		private String companyCEO;
		private Long companyTurnover;
		private String companyWebsite;
		private Double stockPrice;
		private LocalDate Date;


}
