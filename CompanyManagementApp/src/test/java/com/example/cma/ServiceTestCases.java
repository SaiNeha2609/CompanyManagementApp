package com.example.cma;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.cma.Entity.Company;
import com.example.cma.Repository.CompanyRepository;
import com.example.cma.Repository.StockRepository;
import com.example.cma.Service.CompanyService;
import com.example.cma.exception.CompanyNotFoundException;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {com.example.cma.CompanyManagementAppApplication.class })
@AutoConfigureMockMvc
class ServiceTestCases {
	@MockBean
    private StockRepository stockRepo;
	@MockBean
	private CompanyRepository cr;
 
    @Autowired
    private CompanyService companyService;
    
    @Test
void register() {
    	Company mockCompany = new Company();
    	mockCompany.setCompanyCode(1L);
    	mockCompany.setCompanyName("Mock Company");
    	mockCompany.setCompanyCEO("mock");
    	mockCompany.setCompanyTurnover(2345678901L);
    	mockCompany.setCompanyWebsite("www.mockcompany.com");
    	mockCompany.setStockExchange(Arrays.asList("NSE"));
 
	when(cr.save(mockCompany)).thenReturn(mockCompany);
 
	Assertions.assertEquals(mockCompany, companyService.saveCompany(mockCompany));
}
 
	@Test
	void registerusingservice() {
		Company mockCompany = new Company();
    	mockCompany.setCompanyCode(1L);
    	mockCompany.setCompanyName("Mock Company");
    	mockCompany.setCompanyCEO("mock");
    	mockCompany.setCompanyTurnover(2345678901L);
    	mockCompany.setCompanyWebsite("www.mockcompany.com");
    	mockCompany.setStockExchange(Arrays.asList("NSE")); 
		when(companyService.saveCompany(mockCompany)).thenReturn(mockCompany);
 
		Assertions.assertEquals(mockCompany, companyService.saveCompany(mockCompany));
	}
	@Test
	void getuserbyid() {
		Company mockCompany = new Company();
    	mockCompany.setCompanyCode(1L);
    	mockCompany.setCompanyName("Mock Company");
    	mockCompany.setCompanyCEO("mock");
    	mockCompany.setCompanyTurnover(2345678901L);
    	mockCompany.setCompanyWebsite("www.mockcompany.com");
    	mockCompany.setStockExchange(Arrays.asList("NSE"));
		cr.save(mockCompany);
		when(cr.findById(1L)).thenReturn(Optional.of(mockCompany));
		Assertions.assertEquals(Optional.of(mockCompany),companyService.getByID(1L));
	}
	@Test
	void testDeleteCompanyById_ThrowsException() {
		long companyIdToBeDeleted = 1244;
		Mockito.when(cr.findById(companyIdToBeDeleted)).thenReturn(Optional.empty());
		Assertions.assertThrows(CompanyNotFoundException.class,
				() -> companyService.deleteCompany(companyIdToBeDeleted));
 
	}
	@Test
	void testDeleteCompanyById_Success() {
		long companyIdToBeDeleted = 12;
		Company c = new Company();
		Mockito.when(cr.findById(companyIdToBeDeleted)).thenReturn(Optional.of(c));
		Mockito.doNothing().when(cr).deleteById(companyIdToBeDeleted);
		assertDoesNotThrow(() -> companyService.deleteCompany(companyIdToBeDeleted));
		Mockito.verify(cr).findById(companyIdToBeDeleted);
		Mockito.verify(cr).deleteById(companyIdToBeDeleted);
 
	}
	@Test
	void testUpdateCompany() throws CompanyNotFoundException {
		// Create a sample existing restaurant and updated restaurant
 
		// long existingRestaurantId = 1;
 
		Company existingCompany = new Company();
 
		existingCompany.setCompanyCode(1L);
 
		existingCompany.setCompanyName("sony");
		existingCompany.setCompanyCEO("sonycare");
 
		existingCompany.setCompanyWebsite("sony.com");
 
		existingCompany.setCompanyTurnover(1000000000000L);
 
    	existingCompany.setStockExchange(Arrays.asList("NSE"));
 
		
		when(cr.findById(1L)).thenReturn(Optional.of(existingCompany));
 
		when(cr.save(existingCompany)).thenReturn(existingCompany);
 
		Company updatedcompany = new Company();
 
		updatedcompany.setCompanyName("sony");
		updatedcompany.setCompanyCEO("sonycover");
 
		updatedcompany.setCompanyWebsite("sony.com");
 
		updatedcompany.setCompanyTurnover(1000000000000L);
 
    	updatedcompany.setStockExchange(Arrays.asList("NSE"));
		// Mock the behavior of the repository's findById method
 
		Mockito.when(cr.findById(1L))
 
				.thenReturn(Optional.of(existingCompany));
 
		// Call the updateRestaurant method
 
		Company result = companyService.updateCompany(1L, updatedcompany);
		System.out.println(result);
 
		Assertions.assertEquals("sony", updatedcompany.getCompanyName());
 
	}
    
    
}