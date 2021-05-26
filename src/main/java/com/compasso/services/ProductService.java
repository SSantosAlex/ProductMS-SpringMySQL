package com.compasso.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compasso.models.Product;
import com.compasso.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository prodRepo;
	
	public List<Product> listAllProducts() {
		return prodRepo.findAll();
	}
	
	public void saveProduct(Product product) {		
			prodRepo.save(product);		
	}
	
	public Product getProductbyId(Integer id) {
		return prodRepo.findById(id).get();
	}
	
	public void deleteProduct(Integer id) {
		prodRepo.deleteById(id);
	}

	public HashSet<Product> searchProduct(String q, BigDecimal min_price, BigDecimal max_price) {

		HashSet<Product> products = new HashSet<Product>();				
		
		if(min_price != null && max_price != null) {
			
			prodRepo.findByPriceBetween(min_price, max_price).forEach(products::add);			

		}
		
		if(min_price !=null && max_price == null) {
			
			prodRepo.findByPriceGreaterThanEqual(min_price).forEach(products::add);
			
		} else if(min_price == null && max_price != null) {
			prodRepo.findByPriceLessThanEqual(max_price).forEach(products::add);
		}
		
		if(q!= null) {
			prodRepo.findByNameOrDescription(q).forEach(products::add);
		}
		
		return products;
		
	}
}
