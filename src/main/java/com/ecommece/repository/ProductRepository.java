package com.ecommece.repository;

import com.ecommece.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    List<Product> findByCategoryId(Long categoryId);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
