package com.compasso.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.compasso.models.ErrorMsgs;
import com.compasso.models.Product;
import com.compasso.services.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="API Catalogo de Produtos")
@CrossOrigin(origins="*")
public class ProductController {
	
	@Autowired
	private ProductService prodService;
	
	@GetMapping("/products")
	@ApiOperation(value="Lista de produtos")
	public List<Product> list(){
		return prodService.listAllProducts();
	}
	
	@GetMapping("/products/search")
	@ApiOperation(value="Lista de produtos filtrados")
	@ResponseBody
	public List<Product> searchProduct(@RequestParam(required = false) String q, BigDecimal min_price, BigDecimal max_price){
				
		List<Product> products = new ArrayList<Product>(prodService.searchProduct(q, min_price, max_price));
		
		return products;
		}

	
	@GetMapping("/products/{id}")
	@ApiOperation(value="Busca de um produto por ID")
	public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
		try {
			Product product = prodService.getProductbyId(id);
			return new ResponseEntity<Product>(product, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@PostMapping("/products")
	@ApiOperation(value="Criação de um produto")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		
		try {
			prodService.saveProduct(product);				
			
			return new ResponseEntity<Product>(product, HttpStatus.CREATED);
		} catch(Exception e) {			
			ErrorMsgs error = new ErrorMsgs(400, "Verifique os campos e valores digitados.");
			
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/products/{id}")
	@ApiOperation(value="Atualização de um produto")
	public ResponseEntity<?> updateProduct(@RequestBody Product product, @PathVariable Integer id) {
		
		try {
			Product checkProduct = prodService.getProductbyId(id);
			product.setId(id);
			prodService.saveProduct(product);
			
			return new ResponseEntity<Product>(product, HttpStatus.OK);
		} catch (Exception e) {
			ErrorMsgs error = new ErrorMsgs(400, "Campos inválidos ou vazios, favor verique e tente novamente");
			
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("products/{id}")
	@ApiOperation(value="Deleção de um produto")
	public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
		try {
			prodService.deleteProduct(id);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(EmptyResultDataAccessException e) {
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}