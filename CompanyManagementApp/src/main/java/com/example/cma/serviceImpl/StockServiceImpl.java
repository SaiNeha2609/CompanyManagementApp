package com.example.cma.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cma.Entity.Company;
import com.example.cma.Entity.Stock;
import com.example.cma.Repository.CompanyRepository;
import com.example.cma.Repository.StockRepository;
import com.example.cma.Service.StockService;
import com.example.cma.exception.CompanyNotFoundException;
import com.example.cma.exception.StockNotFoundException;
import com.example.cma.pojo.StockInputModel;

@Service
public class StockServiceImpl implements StockService{
	@Autowired
	private CompanyRepository companyRepo;
	
	@Autowired
	private StockRepository stockRepo; 
	
	@Override
	public Stock addStock(StockInputModel input) throws CompanyNotFoundException {
		// TODO Auto-generated method stub
		Company c = companyRepo.findById(input.getCompanyCode()).orElse(null);
		if(c==null) {
			throw new CompanyNotFoundException();
		}
		Stock stock = new Stock();
		stock.setStockPrice(input.getStockPrice());
		stock.setCompany_id_fk(input.getCompanyCode());
		stock.setDate(LocalDateTime.now());
		return stockRepo.save(stock);
		
	}
	
	public Stock updateStock(Stock stock) throws StockNotFoundException {
		Stock s = stockRepo.findById(stock.getStockId()).orElse(null);
		if(s!=null) {
			return stockRepo.save(s);
		}
		throw new StockNotFoundException("Stock Not Found.");
	}

}
