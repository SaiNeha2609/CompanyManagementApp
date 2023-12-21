package com.example.cma;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cma.Controller.StockController;
import com.example.cma.Entity.Stock;
import com.example.cma.Service.StockService;
import com.example.cma.exception.CompanyNotFoundException;
import com.example.cma.exception.IncorrectPasswordException;
import com.example.cma.exception.UserNotFoundException;
import com.example.cma.feignClient.UserAuthenticationClient;
import com.example.cma.pojo.StockInputModel;
import com.example.cma.pojo.ValidateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;

@ExtendWith(MockitoExtension.class)
public class StockControllerTest {

    @Mock
    private StockService stockService;

    @Mock
    private UserAuthenticationClient userClient;

    @InjectMocks
    private StockController stockController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(stockController).build();
    }

    private StockInputModel createMockStock() {
        StockInputModel mockInput = new StockInputModel();
        mockInput.setStockPrice(5490900d);
        mockInput.setCompanyCode(1L);
        return mockInput;
    }
    @Test
    public void testAddCompanySuccess() throws Exception {
        ValidateResponse validResponse = new ValidateResponse(true, "ADMIN");
        when(userClient.validateUser(anyString())).thenReturn(validResponse);
        when(stockService.addStock(any())).thenReturn(new Stock());

        StockInputModel dummyStockInputModel = createMockStock();

        mockMvc.perform(post("/api/v1.0/market/stock/add/1")
                        .header("Authorization", "mockToken")
                        .content(asJsonString(dummyStockInputModel))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userClient, times(1)).validateUser(anyString());
        verify(stockService, times(1)).addStock(any(StockInputModel.class));
    }

    @Test
    public void testAddCompanyInvalidPassword() throws UserNotFoundException, IncorrectPasswordException, CompanyNotFoundException {
        ValidateResponse invalidResponse = new ValidateResponse(false, "ADMIN");
        when(userClient.validateUser(anyString())).thenReturn(invalidResponse);
        assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/api/v1.0/market/stock/add/1")
                    .header("Authorization", "mockToken")
                    .content(asJsonString(createMockStock()))
                    .contentType(MediaType.APPLICATION_JSON));
        });

        verify(userClient, times(1)).validateUser(anyString());
        verify(stockService, never()).addStock(any(StockInputModel.class));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
