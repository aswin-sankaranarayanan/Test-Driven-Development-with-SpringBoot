package com.example.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.model.dtos.ProductDTO;
import com.example.demo.model.dtos.ReviewDTO;
import com.example.demo.model.entities.ProductEntity;
import com.example.demo.model.entities.ReviewEntity;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.utils.ApplicationException;

@SpringBootTest(classes = { ProductService.class, ProductServiceImpl.class })
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@MockBean
	private ProductRepository productRepository;

	@Test
	@DisplayName("Find All Products")
	void testFindAll() {
		ProductEntity productEntity1 = new ProductEntity();
		productEntity1.setId(1L);
		productEntity1.setName("Biscuit");
		productEntity1.setPrice(15.00);

		ProductEntity productEntity2 = new ProductEntity();
		productEntity2.setId(2L);
		productEntity2.setName("Choclate");
		productEntity2.setPrice(25.00);

		ProductDTO productDTO1 = new ProductDTO(1L, "Biscuit", 15.00);

		ProductDTO productDTO2 = new ProductDTO(2L, "Choclate", 25.00);

		List<ProductEntity> mockProducts = new LinkedList<ProductEntity>();
		mockProducts.add(productEntity1);
		mockProducts.add(productEntity2);

		when(productRepository.findAll()).thenReturn(mockProducts);

		List<ProductDTO> products = productService.findAll();
		assertAll(() -> assertThat(products).hasSize(2), () -> assertThat(products.contains(productDTO1)),
				() -> assertThat(products.contains(productDTO2)));
	}

	@Test
	@DisplayName("Save Product")
	void testSaveProduct() {
		ProductDTO expected = new ProductDTO(1L, "Biscuit", 15.00);
		ProductEntity mockEntity = new ProductEntity("Biscuit", 15.00);
		mockEntity.setId(1L);

		when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(mockEntity);

		ProductDTO actual = productService.saveProduct(expected);

		assertThat(expected).isEqualTo(actual);
	}

	@Nested
	@DisplayName("Product Reviews")
	class ProductReviews {

		@Nested
		@DisplayName("When Valid Product")
		class ValidProductReviewsTest {
			
			@DisplayName("Find All Product Reviews")
			@Test
			void test_findAllReviewsForProduct() {

				ProductEntity productEntity = new ProductEntity();
				productEntity.setId(1L);
				productEntity.setName("Biscuit");
				productEntity.setPrice(15.00);

				List<ReviewEntity> mockReviewEntity = new LinkedList<ReviewEntity>();
				ReviewEntity mockReview1 = new ReviewEntity();
				mockReview1.setDescription("Excellent Product");
				mockReview1.setRating(3.5);

				ReviewEntity mockReview2 = new ReviewEntity();
				mockReview2.setDescription("Kids Loving");
				mockReview2.setRating(5.0);

				mockReviewEntity.add(mockReview1);
				mockReviewEntity.add(mockReview2);

				List<ReviewDTO> expected = new LinkedList<ReviewDTO>();
				ReviewDTO expectedReviewDTO1 = new ReviewDTO();
				expectedReviewDTO1.setDescription("Excellent Product");
				expectedReviewDTO1.setRating(3.5);

				ReviewDTO expectedReviewDTO2 = new ReviewDTO();
				expectedReviewDTO2.setDescription("Kids Loving");
				expectedReviewDTO2.setRating(5.0);

				expected.add(expectedReviewDTO1);
				expected.add(expectedReviewDTO2);

				when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(productEntity));
				when(productRepository.findProductReviews(Mockito.anyLong())).thenReturn(mockReviewEntity);

				List<ReviewDTO> actual = productService.findProductReviews(1L);

				assertAll(() -> assertThat(actual.size()).isEqualTo(expected.size()),
						() -> assertThat(expected.containsAll(actual)));
			}
			
			@Test
			@DisplayName("Add Product Review")
			void test_addReviewToProduct() {
				ProductEntity product = new ProductEntity();
				product.setId(1L);
				
				ProductDTO productDTO = new ProductDTO();
				productDTO.setId(1L);
				
				ReviewDTO expected = new ReviewDTO();
				expected.setDescription("Test");
				expected.setRating(5.0);
				
				expected.setProductDTO(productDTO);
				
				when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
				
				ReviewDTO actual = productService.addReview(productDTO.getId(),expected);
				System.out.println("Expected -> "+expected);
				System.out.println("Actual -> "+ actual);
				assertAll(
						() -> assertThat(actual.getRating()).isEqualTo(expected.getRating()),
						() -> assertThat(actual.getDescription()).isEqualTo(expected.getDescription())
						);
			}
		}
		
		@Nested
		@DisplayName("When Invalid Product")
		class InvalidProduct{
			
			@DisplayName("Then Application Exception will be thrown")
			@Test
			void test_findReviewsForInvalidProduct(){
				when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
				
				ApplicationException applicationException = Assertions.assertThrows(ApplicationException.class, ()->productService.findProductReviews(Mockito.anyLong()));
				
				assertThat(applicationException.getMessage()).isEqualTo("Invalid Product");
			}
		}
		
	}
	
}
