package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.entities.ProductEntity;
import com.example.demo.model.entities.ReviewEntity;

public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Long> {

	@Query("Select reviews  from ProductEntity p where p.id=:id")
	List<ReviewEntity> findProductReviews(@Param("id") Long productId);

}
