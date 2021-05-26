package com.compasso.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.compasso.models.Product;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer>{

	@Query("SELECT p FROM Product p WHERE CONCAT(p.name, ' ', p.description, ' ') LIKE %?1%")
	public List<Product> findByNameOrDescription(String q);
	
	public List<Product> findByPriceLessThanEqual(BigDecimal maxprice);
	
	public List<Product> findByPriceGreaterThanEqual(BigDecimal minprice);
	
	public List<Product> findByPriceBetween(BigDecimal min_price, BigDecimal max_price);
	
	public Product findByPrice(BigDecimal price);
}
