package com.example.cma.Service;

import com.example.cma.Entity.Stock;
import com.example.cma.exception.CompanyNotFoundException;
import com.example.cma.pojo.StockInputModel;
import org.springframework.stereotype.Service;


public interface StockService {
	
	public Stock addStock(StockInputModel input) throws CompanyNotFoundException;

}
