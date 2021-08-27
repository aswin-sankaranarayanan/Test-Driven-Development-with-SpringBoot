package com.example.demo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dtos.ProductDTO;
import com.example.demo.model.dtos.ReviewDTO;
import com.example.demo.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;

	@GetMapping
	public ResponseEntity<List<ProductDTO>> findAll(){
		return ResponseEntity.status(HttpStatus.OK).body( productService.findAll());
	}
	
	@GetMapping("/{id}/reviews")
	public ResponseEntity<List<ReviewDTO>> findAllReviewsForProduct(@PathVariable("id") Long productId){
		return ResponseEntity.status(HttpStatus.OK).body(productService.findProductReviews(productId));
	}
	
	@PostMapping
	public ResponseEntity<ProductDTO> saveProduct(@Valid @RequestBody ProductDTO product) {
		ProductDTO savedProduct = productService.saveProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
	}
	
	@PostMapping("/{id}/reviews")
	public ResponseEntity<ReviewDTO> addReviewToProduct(@PathVariable("id") Long productId, @Valid @RequestBody ReviewDTO review){
		System.out.println("Request ->"+review);
		ReviewDTO savedReview = productService.addReview(productId,review);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
	}
}
