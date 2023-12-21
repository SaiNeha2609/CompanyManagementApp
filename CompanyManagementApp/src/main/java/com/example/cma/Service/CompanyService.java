package com.example.cma.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.cma.Entity.Company;
import com.example.cma.exception.CompanyNotFoundException;

public interface CompanyService {

	abstract Company saveCompany(Company company);

	abstract Optional<Company> getByID(long companyCode);

	abstract List<Company> getAllCompanies();

	abstract  void deleteCompany(long companyCode) throws CompanyNotFoundException;
	
	public Company updateCompany(Long companyCode, Company company) throws CompanyNotFoundException;


}
