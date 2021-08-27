package com.example.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.dtos.ProductDTO;
import com.example.demo.model.dtos.ReviewDTO;
import com.example.demo.services.ProductService;
import com.example.demo.utils.ApplicationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
class ProductControllerTest {

	private static final String URL = "/products";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private ProductService productService;

	@DisplayName("List all Products")
	@Test
	void test_findAllProducts() throws Exception {
		List<ProductDTO> products = new LinkedList<ProductDTO>();
		products.add(new ProductDTO(1L, "Shampoo", 5.00));
		products.add(new ProductDTO(2L, "Dairy Milk Silk", 55.00));

		when(productService.findAll()).thenReturn(products);

		MockHttpServletResponse response = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn()
				.getResponse();

		assertThat(mapper.readValue(response.getContentAsString(), new TypeReference<List<ProductDTO>>() {
		})).isEqualTo(products);
	}

	@Nested
	@DisplayName("Saving Product")
	class SaveProduct {

		@Nested
		@DisplayName("When Valid Product")
		class saveValidProduct {
			@DisplayName("Then Product is saved")
			@Test
			void test_saveProduct() throws Exception {
				ProductDTO product = new ProductDTO(1L, "Biscuit", 15.00);

				when(productService.saveProduct(Mockito.any(ProductDTO.class))).thenReturn(product);

				MockHttpServletResponse response = mockMvc
						.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
								.content(mapper.writeValueAsString(product)))
						.andExpect(status().is(201)).andReturn().getResponse();

				assertThat(mapper.readValue(response.getContentAsString(), ProductDTO.class)).isEqualTo(product);
			}
		}

		@Nested
		@DisplayName("When Invalid Product")
		class InvalidProduct {

			private static final String NAME_EMPTY_MESSAGE = "name is required";
			private static final String PRICE_EMPTY_MESSAGE = "price should be a number greater than 0";

			@Test
			@DisplayName("The Exception will be thrown when empty name")
			void testSaveProductWithInvalidName() throws Exception {
				ProductDTO product = new ProductDTO(1L, "", 15.00);
				MockHttpServletResponse response = mockMvc
						.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
								.content(mapper.writeValueAsString(product)))
						.andExpect(status().is(400)).andReturn().getResponse();
				
				List<ApplicationException> exceptions = mapper.readValue(response.getContentAsString(), new TypeReference<List<ApplicationException>>(){});
				assertAll(
						() -> assertThat(exceptions.size()).isEqualTo(1),
						() -> assertThat(exceptions.get(0).getMessage()).isEqualTo(NAME_EMPTY_MESSAGE)
						);
			}

			@Test
			@DisplayName("The Exception will be thrown when null price")
			void testSaveProductWithInvalidPrice() throws Exception {
				ProductDTO product = new ProductDTO(1L, "Biscuit", null);
				MockHttpServletResponse response = mockMvc
						.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
								.content(mapper.writeValueAsString(product)))
						.andExpect(status().is(400)).andReturn().getResponse();
				List<ApplicationException> exceptions = mapper.readValue(response.getContentAsString(), new TypeReference<List<ApplicationException>>(){});

				assertAll(
						() -> assertThat(exceptions.size()).isEqualTo(1),
						() -> assertThat(exceptions.get(0).getMessage()).isEqualTo(PRICE_EMPTY_MESSAGE)
						);
			}
		}

	}
	
	@DisplayName("Product Reviews")
	@Nested
	class ProductReviews{
		private final String REVIEW_URL = URL+"/1/reviews";
		
		@Nested
		@DisplayName("When valid product")
		class Review{
			
			@Test
			@DisplayName("List all the reviews for the product")
			void test_findAllReviewsForProduct() throws Exception{
				List<ReviewDTO> expected = new LinkedList<ReviewDTO>();
				ReviewDTO mockReview1 = new ReviewDTO();
				mockReview1.setDescription("Excellent Product");
				mockReview1.setRating(3.5);
				mockReview1.setProductDTO(new ProductDTO(1L,"Biscuit",15.00));
				
				ReviewDTO mockReview2 = new ReviewDTO();
				mockReview2.setDescription("Kids Loving");
				mockReview2.setRating(5.0);
				mockReview2.setProductDTO(new ProductDTO(1L,"Biscuit",15.00));
				
				expected.add(mockReview1);
				expected.add(mockReview2);
				
				when(productService.findProductReviews(Mockito.anyLong())).thenReturn(expected);
				MockHttpServletResponse response = mockMvc.perform(get(REVIEW_URL)).andExpect(status().isOk()).andReturn().getResponse();
				List<ReviewDTO> actual = mapper.readValue(response.getContentAsString(), new TypeReference<List<ReviewDTO>>() {});
				assertAll(
						() -> assertThat(actual.size()).isEqualTo(expected.size()),
						() -> assertThat(expected.containsAll(actual))
						);
			}
			
			@DisplayName("Save Product Review")
			@Test
			void test_addReviewToProduct() throws Exception, Exception {
				ProductDTO product = new ProductDTO();
				product.setId(2L);
				
				ReviewDTO review = new ReviewDTO();
				review.setDescription("Test Description");
				review.setRating(3.3);
				review.setProductDTO(product);
				
				when(productService.addReview(Mockito.anyLong(),Mockito.any())).thenReturn(review);
				
				MockHttpServletResponse response = mockMvc.perform(post(REVIEW_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(review)))
				.andExpect(status().isCreated()).andReturn().getResponse();
				
				ReviewDTO actual = mapper.readValue(response.getContentAsString(), ReviewDTO.class);
				
				assertAll(
						() -> assertThat(actual.getDescription()).isEqualTo(review.getDescription()),
						() -> assertThat(actual.getRating()).isEqualTo(review.getRating())
						);
				
				
			}
		}
		
		@Nested
		@DisplayName("When Invalid Product")
		class InvalidProduct{
			
			@DisplayName("Then Throw Application Exception when Finding Reviews")
			@Test
			void test_ReviewForInvalidProduct() throws Exception{
				when(productService.findProductReviews(Mockito.anyLong())).thenThrow(new ApplicationException("Invalid Product"));
				mockMvc.perform(get(REVIEW_URL)).andExpect(status().is(400));
			}
			
			@DisplayName("Then Throw Application Exception when Saving Review For Invalid Product")
			@Test
			void test_addReviewForInvalidProduct() throws Exception, Exception {
				ReviewDTO dto = new ReviewDTO();
				when(productService.addReview(1L,dto)).thenThrow(new ApplicationException("Invalid Product"));
				MockHttpServletResponse response = mockMvc.perform(post(REVIEW_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new ReviewDTO()))).andExpect(status().is(400)).andReturn().getResponse();
				
				List<ApplicationException> exceptions = mapper.readValue(response.getContentAsString(), new TypeReference<List<ApplicationException>>() {});
				assertAll(
						() -> assertThat(exceptions.size()).isEqualTo(1),
						() ->assertThat(exceptions.contains(new ApplicationException("Invalid Product")))
						);
			}
			
			@Nested
			@DisplayName("Invalid Reviews")
			class InvalidReviews{
				
				@DisplayName("Then Throw Application Exception When Saving With No Rating")
				@Test
				void test_addInvalidReview() throws Exception, Exception {
					ReviewDTO dto = new ReviewDTO();
					dto.setDescription("Test Description");
					
					MockHttpServletResponse response = mockMvc.perform(post(REVIEW_URL).contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(new ReviewDTO()))).andExpect(status().is(400)).andReturn().getResponse();
					
					List<ApplicationException> exceptions = mapper.readValue(response.getContentAsString(), new TypeReference<List<ApplicationException>>() {});
					assertAll(
							() -> assertThat(exceptions.size()).isEqualTo(1),
							() ->assertThat(exceptions.contains(new ApplicationException("Rating is required")))
							);
				
				}
				
				@DisplayName("Then Throw Application Exception When Saving With No Description")
				@Test
				void test_addInvalidReviewWithNoDescription() throws Exception {
					ReviewDTO dto = new ReviewDTO();
					dto.setRating(2.5);
					
					MockHttpServletResponse response = mockMvc.perform(post(REVIEW_URL).contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(new ReviewDTO()))).andExpect(status().is(400)).andReturn().getResponse();
					
					List<ApplicationException> exceptions = mapper.readValue(response.getContentAsString(), new TypeReference<List<ApplicationException>>() {});
					assertAll(
							() -> assertThat(exceptions.size()).isEqualTo(1),
							() ->assertThat(exceptions.contains(new ApplicationException("Description is required")))
							);
				
				}
				
				
			}
			
		}
		
	}
}
