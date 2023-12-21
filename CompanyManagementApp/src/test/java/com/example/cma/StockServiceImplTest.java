package com.example.cma;

import com.example.cma.Entity.Company;
import com.example.cma.Entity.Stock;
import com.example.cma.Repository.StockRepository;
import com.example.cma.Service.CompanyService;
import com.example.cma.exception.CompanyNotFoundException;
import com.example.cma.pojo.StockInputModel;
import com.example.cma.serviceImpl.StockServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceImplTest {

    @Mock
    private StockRepository stockRepo;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private StockServiceImpl stockService;

    private StockInputModel sampleInput;

    @BeforeEach
    public void setUp() {
        sampleInput = new StockInputModel();
        sampleInput.setStockPrice(100.0);
        sampleInput.setCompanyCode(1L);
    }

    @Test
    public void testAddStock() throws CompanyNotFoundException {
        Company sampleCompany = new Company();
        sampleCompany.setCompanyCode(1L);
        when(companyService.getByID(1L)).thenReturn(Optional.of(sampleCompany));
       when(stockRepo.save(any(Stock.class))).thenReturn(new Stock());
        stockService.addStock(sampleInput);
        verify(companyService, atLeastOnce()).getByID((1L));
        verify(stockRepo, atLeastOnce()).save(any(Stock.class));
    }


    @Test
    public void testAddStockCompanyIdNotFound() throws CompanyNotFoundException{
        when(companyService.getByID(anyLong())).thenReturn(null);
        Assertions.assertThrows(CompanyNotFoundException.class, () -> {
            stockService.addStock(sampleInput);
        });
        verify(companyService, times(1)).getByID(anyLong());
        verify(stockRepo, never()).save(any());

    }}

