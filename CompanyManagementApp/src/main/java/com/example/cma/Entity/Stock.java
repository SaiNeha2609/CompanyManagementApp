package com.example.cma.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Stock {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long stockId;
	@Column(name="stockPrice",nullable = false)
	private Double stockPrice;
	@Column(name="date",nullable = false)
	private LocalDateTime date;
	private Long company_id_fk;
	

}
