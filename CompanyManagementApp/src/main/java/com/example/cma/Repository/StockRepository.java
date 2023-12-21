package com.example.cma.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cma.Entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock,Long>{

}
