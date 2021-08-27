package com.example.demo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.example.demo.model.entities.ProductEntity;
import com.example.demo.model.entities.ReviewEntity;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Sql("/sql/products.sql")
@Disabled
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;
	
	@DisplayName("Find All Products")
	@Test
	void testFindAllProducts() {
		ProductEntity product1 = new ProductEntity("Biscuit", 15.00);
		product1.setId(1L);
		
		ProductEntity product2 = new ProductEntity("Chocolate", 25.00);
		product2.setId(2L);
		
		List<ProductEntity> expectedProducts = new LinkedList<>();
		expectedProducts.add(product1);
		expectedProducts.add(product2);
		
		List<ProductEntity> actualProducts = (List<ProductEntity>)productRepository.findAll();
		
		assertAll(
				() -> assertThat(actualProducts.size()).isEqualTo(expectedProducts.size()),
				() -> assertTrue(actualProducts.containsAll(expectedProducts))
				);
	}
	
	@DisplayName("Save Product")
	@Test
	void test_saveProduct() {
		ProductEntity product = new ProductEntity("Shampoo", 5.00);
		
		ProductEntity actual = productRepository.save(product);
		System.out.println("Saved -->"+actual);
		
		assertAll(
				() -> assertThat(actual.getName()).isEqualTo(product.getName()),
				() -> assertThat(actual.getPrice()).isEqualTo(product.getPrice())
				);
	}

	@Nested
	@DisplayName("Product Reviews")
	class ProductReviews{
		
		@DisplayName("Find All Reviews For Product")
		@Test
		void test_FindAllReviews() {
			List<ReviewEntity> expected = new LinkedList<ReviewEntity>();
			ReviewEntity mockReview1 = new ReviewEntity();
			mockReview1.setDescription("Excellent Product");
			mockReview1.setRating(3.5);
			mockReview1.setId(1L);

			ReviewEntity mockReview2 = new ReviewEntity();
			mockReview2.setDescription("Kids Loving");
			mockReview2.setRating(5.0);
			mockReview2.setId(2L);

			expected.add(mockReview1);
			expected.add(mockReview2); 
			
			List<ReviewEntity> actual = productRepository.findProductReviews(1L);
			System.out.println("Reviews "+actual);

			assertAll(() -> assertThat(actual.size()).isEqualTo(expected.size()),
					() -> assertThat(expected.containsAll(actual)));
		}
	}
	
	@DisplayName("Add Product Review")
	@Test
	void test_addReviewToProduct() {
		
		ReviewEntity review = new ReviewEntity();
		review.setDescription("Test");
		review.setRating(3.5);
		
		ProductEntity product = productRepository.findById(2L).get();
		System.out.println("Before ->"+product.getReviews());
		product.addReview(review);
		
		ProductEntity savedProduct = productRepository.save(product);
		System.out.println("After ->"+savedProduct.getReviews());
		assertThat(savedProduct.getReviews().size()).isEqualTo(1);
	}
}
