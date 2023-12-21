package com.example.cma.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.cma.Entity.Company;
import com.example.cma.Repository.CompanyRepository;
import com.example.cma.Service.CompanyService;
import com.example.cma.exception.CompanyNotFoundException;
import com.example.cma.feignClient.UserAuthenticationClient;

@Service
public class CompanyImpl implements CompanyService{
	@Autowired
	private CompanyRepository companyRepo;
	
	

	@Override
	public Company saveCompany(Company com) {
		return companyRepo.save(com);
	}
	
	@Override
	public Optional<Company> getByID(long companyCode){
		return companyRepo.findById(companyCode);
	}

	@Override
	public List<Company> getAllCompanies() {
		List<Company> companyList= companyRepo.findAll();
		return companyList;
	}
	@Override
	public void deleteCompany(long companyCode) throws CompanyNotFoundException{
			Company comp=companyRepo.findById(companyCode).orElse(null);
			if(comp==null) {
				throw new CompanyNotFoundException("Invalid Company Code. Please provide valid company code");
			}
			 companyRepo.deleteById(companyCode);
	}

	@Override
	public Company updateCompany(Long companyCode, Company company) throws CompanyNotFoundException {
		Company com=companyRepo.findById(companyCode).orElse(null);
		if(com==null) {
			throw new CompanyNotFoundException("Invalid Company Code. Please provide valid company code");
		}
		return companyRepo.save(company);
	}
}






