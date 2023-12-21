package com.example.cma.Controller;

import java.util.List;
import java.util.Optional;

import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.example.cma.Entity.Company;
import com.example.cma.Repository.CompanyRepository;
import com.example.cma.Service.CompanyService;
import com.example.cma.exception.CompanyNotFoundException;
import com.example.cma.exception.IncorrectPasswordException;
import com.example.cma.exception.UserNotFoundException;
import com.example.cma.feignClient.UserAuthenticationClient;
import com.example.cma.pojo.ValidateResponse;

import lombok.Builder;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/v1.0/market/company")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private UserAuthenticationClient client;
	
	
	@PostMapping("/register")
	public ResponseEntity<Company> addCompany(@RequestHeader("Authorization") String token ,@RequestBody Company company) throws UserNotFoundException, IncorrectPasswordException {
		ValidateResponse entity=client.validateUser(token);
		ValidateResponse response=client.validateUser(token);
		if(response.getValid()&& response.getRoleName().contains("ADMIN")) {
			System.out.println("inside register1");
			Company nc = companyService.saveCompany(company);
			return new ResponseEntity<>(nc, HttpStatus.OK);
		}
        	throw new IncorrectPasswordException("Invalid User");
	}
	
	@GetMapping("/info/{companyCode}")
	public ResponseEntity<?> getCompanyById(@RequestHeader("Authorization") String token,@PathVariable  long companyCode ) throws CompanyNotFoundException, UserNotFoundException, IncorrectPasswordException{
		ValidateResponse response=client.validateUser(token);
		if(response.getValid()){
		Optional<Company> company=companyService.getByID(companyCode);
		return new ResponseEntity<>(company,HttpStatus.OK);
	}
		throw new IncorrectPasswordException("Invalid User");
	}
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllCompanies(@RequestHeader("Authorization") String token) throws UserNotFoundException, IncorrectPasswordException{
		ValidateResponse response=client.validateUser(token);
		if(response.getValid()) {
		List<Company> companyList=companyService.getAllCompanies();
		return new ResponseEntity<List<Company>>(companyList,HttpStatus.OK);
	}
		throw new IncorrectPasswordException("Invalid User");
	}

	@DeleteMapping("/delete/{companyCode}")
	public void deleteCompany(@RequestHeader("Authorization") String token,@PathVariable Long companyCode ) throws CompanyNotFoundException, UserNotFoundException, IncorrectPasswordException {
		ValidateResponse response=client.validateUser(token);
		if(response.getValid()&& response.getRoleName().contains("ADMIN")) {
		companyService.deleteCompany(companyCode);
	}
		throw new IncorrectPasswordException("Invalid User");
	}

	@PutMapping("/update/{companyCode}")
	public ResponseEntity<Company> updateCompany(@RequestHeader("Authorization") String token,@PathVariable Long companyCode, @RequestBody Company company) throws CompanyNotFoundException, UserNotFoundException, IncorrectPasswordException{
		ValidateResponse response=client.validateUser(token);
		if(response.getValid()&& response.getRoleName().contains("ADMIN")) {
		Company coo=companyService.updateCompany(companyCode,company);
		return new ResponseEntity<>(coo,HttpStatus.OK);
	}
		throw new IncorrectPasswordException("Invalid User");
	}
}

