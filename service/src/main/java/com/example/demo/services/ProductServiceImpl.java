package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.dtos.ProductDTO;
import com.example.demo.model.dtos.ReviewDTO;
import com.example.demo.model.entities.ProductEntity;
import com.example.demo.model.entities.ReviewEntity;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.utils.ApplicationException;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<ProductDTO> findAll() {
		List<ProductEntity> products = (List<ProductEntity>) productRepository.findAll();
		return  products.stream().map(this::convertToDTO).collect(Collectors.toList());
	}


	@Override
	public ProductDTO saveProduct(ProductDTO product) {
		ProductEntity entity = convertToEntity(product);
		return convertToDTO(productRepository.save(entity));
	}
	
	@Override
	public List<ReviewDTO> findProductReviews(Long productId) {
		ProductEntity product = findProduct(productId);
		List<ReviewEntity> reviews = productRepository.findProductReviews(product.getId());
		return reviews.stream().map(this:: convertToReviewDTO).collect(Collectors.toList());
	}


	
	@Override
	public ReviewDTO addReview(Long productId,ReviewDTO reviewDTO) {
		ProductEntity product = findProduct(productId);
		ReviewEntity review = convertToReviewEntity(reviewDTO);
		product.addReview(review);
		return convertToReviewDTO(review);
	}
	
	private ProductDTO convertToDTO(ProductEntity entity) {
		return new ProductDTO(entity.getId(), entity.getName(),entity.getPrice());
	}
	
	private ProductEntity convertToEntity(ProductDTO dto) {
		return new ProductEntity(dto.getName(), dto.getPrice());
	}
	
	private ReviewDTO convertToReviewDTO(ReviewEntity entity) {
		ReviewDTO dto = new ReviewDTO();
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		dto.setRating(entity.getRating());
		return dto;
	}
	
	private ReviewEntity convertToReviewEntity(ReviewDTO dto){
		ReviewEntity entity = new ReviewEntity();
		entity.setDescription(dto.getDescription());
		entity.setRating(dto.getRating());
		return entity;
	}
	
	private ProductEntity findProduct(Long productId) {
		return productRepository.findById(productId).orElseThrow(() -> new ApplicationException("Invalid Product"));
	}
	
	
}
