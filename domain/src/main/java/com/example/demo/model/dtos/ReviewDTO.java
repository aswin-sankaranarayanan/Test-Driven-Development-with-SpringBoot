package com.example.demo.model.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class ReviewDTO extends BaseDTO {

	@NotBlank(message = "Description is required")
	private String description;
	
	@Positive(message = "Rating is required")
	private Double rating;
	
	@JsonBackReference
	private ProductDTO productDTO;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "ReviewDTO [description=" + description + ", rating=" + rating + ", productDTO=" + productDTO + "]";
	}
	
	
}
