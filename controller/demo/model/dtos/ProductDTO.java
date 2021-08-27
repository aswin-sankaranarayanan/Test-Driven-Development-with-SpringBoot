package com.example.demo.model.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProductDTO extends BaseDTO{

	
	@NotBlank(message = "name is required")
	private String name;
	
	@NotNull(message = "price should be a number greater than 0")
	@Positive(message = "price should be a number greater than 0")
	private Double price;

	public ProductDTO() {}
	
	public ProductDTO(Long id, String name, Double price) {
		super(id);
		this.name = name;
		this.price = price;
	}
	
	public ProductDTO(String name, Double price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ProductDTO [name=" + name + ", price=" + price + "]";
	}

}
