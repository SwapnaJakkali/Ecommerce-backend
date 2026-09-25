package com.ecommece.controller;

import com.ecommece.dto.ProductDto;
import com.ecommece.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ecommece.service.CloudinaryService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String sortDir) {
        return ResponseEntity.ok(productService.getAllProducts(categoryId, keyword, page, size, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDto> createProduct(
            @RequestPart("product") ProductDto productDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        
        if (image != null && !image.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(image);
                productDto.setImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image to Cloudinary", e);
            }
        }
        
        return ResponseEntity.ok(productService.createProduct(productDto));
    }
}
