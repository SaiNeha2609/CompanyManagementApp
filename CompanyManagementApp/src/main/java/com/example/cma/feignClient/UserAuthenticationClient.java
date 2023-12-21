package com.example.cma.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.cma.exception.IncorrectPasswordException;
import com.example.cma.exception.UserNotFoundException;
import com.example.cma.pojo.ValidateResponse;

@FeignClient(url="localhost:8085/api/v1.0/user", name="LoginApplication")
public interface UserAuthenticationClient {
	
	@PostMapping("/validate")
    public ValidateResponse validateUser(@RequestHeader("Authorization") String token) throws UserNotFoundException, IncorrectPasswordException;

}
