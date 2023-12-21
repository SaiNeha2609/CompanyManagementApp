package com.example.cma.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cma.Entity.Company;


@Repository
public interface CompanyRepository extends JpaRepository<Company,Long>{
	

}
