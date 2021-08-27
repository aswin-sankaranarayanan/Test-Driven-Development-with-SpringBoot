package com.example.demo.services;

import java.util.List;


import com.example.demo.model.dtos.ProductDTO;
import com.example.demo.model.dtos.ReviewDTO;

public interface ProductService {

	List<ProductDTO> findAll();
	ProductDTO saveProduct(ProductDTO product);
	List<ReviewDTO> findProductReviews(Long productId);
	ReviewDTO addReview(Long productId,ReviewDTO review);
}
