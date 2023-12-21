package com.example.cma;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.cma.Entity.Company;

@ExtendWith(MockitoExtension.class)
public class CompanyTest {
 
    @Test
    public void testCompanyProperties() {
        Company company = new Company();
        company.setCompanyCode(1L);
        company.setCompanyName("Sample Company");
        company.setCompanyCEO("CEO Name");
        company.setCompanyTurnover(1000000001L);
company.setCompanyWebsite("www.samplecompany.com");
        company.setStockExchange(Arrays.asList("NSE", "BSE"));
 
        assertEquals(1L, company.getCompanyCode().longValue());
        assertEquals("Sample Company", company.getCompanyName());
        assertEquals("CEO Name", company.getClass());
        assertEquals(1000000001L, company.getCturnover().longValue());
assertEquals("www.samplecompany.com", company.getCwebsite());
        assertEquals(Arrays.asList("NSE", "BSE"), company.getSexchange());
 
        Stock stock1 = new Stock();
        stock1.setSid(1L);
        Stock stock2 = new Stock();
        stock2.setSid(2L);
        
        company.setStocks(Arrays.asList(stock1, stock2));
 
        assertNotNull(company.getStocks());
        assertEquals(2, company.getStocks().size());
        assertEquals(1, company.getStocks().get(0).getSid());
        assertEquals(2, company.getStocks().get(1).getSid());
    }
    
    // Add more test cases for service or controller methods that use the Company class
}