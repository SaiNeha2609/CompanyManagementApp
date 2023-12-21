package com.example.cma.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cma.Entity.Stock;
import com.example.cma.exception.CompanyNotFoundException;
import com.example.cma.exception.IncorrectPasswordException;
import com.example.cma.exception.UserNotFoundException;
import com.example.cma.feignClient.UserAuthenticationClient;
import com.example.cma.pojo.StockInputModel;
import com.example.cma.pojo.ValidateResponse;
import com.example.cma.serviceImpl.StockServiceImpl;

@RestController
@RequestMapping("/api/v1.0/market/stock")
public class StockController {

	@Autowired
	private StockServiceImpl stockService;
	
	@Autowired
	private UserAuthenticationClient client;
	
	@PostMapping("/add/{companycode}")
	public ResponseEntity<Stock> addStock(@RequestHeader("Authorization") String token,@RequestBody StockInputModel input) throws CompanyNotFoundException, UserNotFoundException, IncorrectPasswordException{
		ValidateResponse response=client.validateUser(token);
		if(response.getValid()&& response.getRoleName().contains("ADMIN")) {
		Stock s = stockService.addStock(input);
		return new ResponseEntity<>(s,HttpStatus.OK);
	}
		throw new IncorrectPasswordException("Invalid User");
	}
}
