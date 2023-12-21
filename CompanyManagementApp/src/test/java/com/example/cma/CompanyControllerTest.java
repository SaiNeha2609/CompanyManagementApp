package com.example.cma;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.example.cma.Controller.CompanyController;
import com.example.cma.Entity.Company;
import com.example.cma.Service.CompanyService;
import com.example.cma.exception.CompanyNotFoundException;
import com.example.cma.exception.IncorrectPasswordException;
import com.example.cma.exception.UserNotFoundException;
import com.example.cma.feignClient.UserAuthenticationClient;
import com.example.cma.pojo.ValidateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {com.example.cma.CompanyManagementAppApplication.class })
@AutoConfigureMockMvc
public class CompanyControllerTest {
 
    @Mock
    private CompanyService companyService;
 
    @Mock
    private UserAuthenticationClient userClient;
 
    @InjectMocks
    private CompanyController companyController;
 
    @Autowired
    private MockMvc mockMvc;
    
    
    @BeforeEach
    public void setUp() {
    	mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
    	
    }
    
    private Company createMockCompany() {
    	Company mockCompany = new Company();
    	mockCompany.setCompanyCode(1L);
    	mockCompany.setCompanyName("Mock Company");
    	mockCompany.setCompanyCEO("mock");
    	mockCompany.setCompanyTurnover(2345678901L);
    	mockCompany.setCompanyWebsite("www.mockcompany.com");
    	mockCompany.setStockExchange(Arrays.asList("NSE"));
    	return mockCompany;
    }
 
    @Test
    public void testAddCompanySuccess() throws Exception {
        ValidateResponse validResponse = new ValidateResponse(true, "ADMIN");
        when(userClient.validateUser(anyString())).thenReturn(validResponse);
        when(companyService.saveCompany(any())).thenReturn(new Company());
     
        Company dummyCompany = createMockCompany();
     
        mockMvc.perform(post("/api/v1.0/market/company/register")
                .header("Authorization", "mockToken")
                .content(asJsonString(dummyCompany))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
     
        //verify(userClient, times(1)).validateUser(anyString());
        verify(companyService, times(1)).saveCompany(any());
    }
    
    @Test
    public void testAddCompanyInvalidPassword() throws UserNotFoundException, IncorrectPasswordException, CompanyNotFoundException {
        ValidateResponse invalidResponse = new ValidateResponse(false, "ADMIN");
        when(userClient.validateUser(anyString())).thenReturn(invalidResponse);
         assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/api/v1.0/market/company/register")
                    .header("Authorization", "mockToken")
                    .content(asJsonString(createMockCompany()))
                    .contentType(MediaType.APPLICATION_JSON));
        });
 
        //verify(userClient, times(1)).validateUser(anyString());
        verify(companyService, never()).saveCompany(any(Company.class));
    }
    
    @Test
    public void testGetAllCompanySuccess() throws Exception {
        when(userClient.validateUser(anyString())).thenReturn(new ValidateResponse(true, "USER"));
 
        List<Company> mockCompanies = Arrays.asList(new Company(), new Company());
        when(companyService.getAllCompanies()).thenReturn(mockCompanies);
 
        mockMvc.perform(get("/api/v1.0/market/company/getAll")
                .header("Authorization", "mockToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
 
        verify(companyService).getAllCompanies();
    }
    
    @Test
    public void testGetAllCompanyInvalidPassword() throws UserNotFoundException, IncorrectPasswordException {
        ValidateResponse invalidResponse = new ValidateResponse(false, "ADMIN");
        when(userClient.validateUser(anyString())).thenReturn(invalidResponse);
         assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/api/v1.0/market/company/getAll")
                    .header("Authorization", "mockToken"));
        });
         verify(userClient,times(1)).validateUser(anyString());
         verify(companyService, never()).getAllCompanies();
    }
 
   
 
    @Test
    public void testGetByIdCompanySuccess() throws Exception {
        // Dummy user validation response
        ValidateResponse validResponse = new ValidateResponse(true, "ADMIN");
        when(userClient.validateUser(anyString())).thenReturn(validResponse);
 
        Company dummyCompany = createMockCompany();
        when(companyService.getByID(anyLong())).thenReturn(Optional.of(dummyCompany));
        mockMvc.perform(get("/api/v1.0/market/company/info/1")
                .header("Authorization", "mockToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Mock Company"));
 
        verify(companyService, times(1)).getByID(anyLong());
    }
 
    @Test
    public void testGetByIdCompanyNotFound() throws Exception {
        ValidateResponse validResponse = new ValidateResponse(true, "ADMIN");
        when(userClient.validateUser(anyString())).thenReturn(validResponse);
         when(companyService.getByID(anyLong())).thenReturn(null);
        
         mockMvc.perform(get("/api/v1.0/market/company/info/1")
                 .header("Authorization", "mockToken"))
                 .andExpect(status().isOk());
  
         verify(companyService, times(1)).getByID(anyLong());
    }
    
    @Test
    public void testGetByIdCompanyInvalidPassword() throws UserNotFoundException, IncorrectPasswordException {
        ValidateResponse invalidResponse = new ValidateResponse(false, "ADMIN");
        when(userClient.validateUser(anyString())).thenReturn(invalidResponse);
         assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/api/v1.0/market/company/info/1")
                    .header("Authorization", "mockToken"));
        });
 
        verify(userClient, times(1)).validateUser(anyString());
        verify(companyService, never()).getByID(anyLong());
    }
 
    
 
    @Test
    public void testDeleteCompanySuccess() throws Exception {
        when(userClient.validateUser(anyString())).thenReturn(new ValidateResponse(true, "ADMIN"));
 
        mockMvc.perform(delete("/api/v1.0/market/company/delete/1")
                .header("Authorization", "mockToken"))
                .andExpect(status().isOk());
 
        verify(companyService, times(1)).deleteCompany(anyLong());
    }
 
    @Test
    public void testDeleteCompanyInvalidPassword() throws UserNotFoundException, IncorrectPasswordException, CompanyNotFoundException {
        ValidateResponse invalidResponse = new ValidateResponse(false, "USER");
        when(userClient.validateUser(anyString())).thenReturn(invalidResponse);
         assertThrows(ServletException.class, () -> {
            mockMvc.perform(delete("/api/v1.0/market/company/delete/1")
                    .header("Authorization", "mockToken"));
        });
 
        verify(userClient, times(1)).validateUser(anyString());
        verify(companyService, never()).deleteCompany(anyLong());
    }
    
 
    @Test
    public void testUpdateCompanySuccess() throws Exception {
    	ValidateResponse validResponse = new ValidateResponse(true, "ADMIN");
        when(userClient.validateUser(anyString())).thenReturn(validResponse);
        Company existingCompany = new Company();
        existingCompany.setCompanyCode(1L);
        existingCompany.setCompanyName("Existing Company");
        existingCompany.setCompanyCEO("mock");
        existingCompany.setCompanyTurnover(2345678901L);
        existingCompany.setCompanyWebsite("www.mockcompany.com");
        existingCompany.setStockExchange(Arrays.asList("NSE"));
        Company dummyCompany = createMockCompany();
       when(companyService.updateCompany(anyLong(), any(Company.class))).thenReturn(dummyCompany);
 
       mockMvc.perform(put("/api/v1.0/market/company/update/1")
                .header("Authorization", "mockToken")
                .content(asJsonString(existingCompany))
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.companyName").value("Mock Company"));

       verify(companyService, times(1)).updateCompany(anyLong(), any(Company.class));
    }
    
    @Test
    public void testUpdateCompanyInvalidPassword() throws UserNotFoundException, IncorrectPasswordException, CompanyNotFoundException {
        ValidateResponse invalidResponse = new ValidateResponse(true, "USER");
        when(userClient.validateUser(anyString())).thenReturn(invalidResponse);
         assertThrows(ServletException.class, () -> {
            mockMvc.perform(put("/api/v1.0/market/company/update/1")
                    .header("Authorization", "mockToken")
                    .content(asJsonString(createMockCompany()))
                    .contentType(MediaType.APPLICATION_JSON));
        });
 
        verify(userClient, times(1)).validateUser(anyString());
        verify(companyService, never()).updateCompany(anyLong(), any(Company.class));
    }
    

    private String asJsonString(final Object obj) {
    	try {
    		return new ObjectMapper().writeValueAsString(obj);
    	}catch(Exception e) {
    		throw new RuntimeException(e);
    	}
    }
  
}