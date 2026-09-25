package com.ecommece.controller;

import com.ecommece.dto.AddToCartRequest;
import com.ecommece.dto.CartDto;
import com.ecommece.dto.UpdateCartItemRequest;
import com.ecommece.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        return ResponseEntity.ok(cartService.getCartForCurrentUser());
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addItemToCart(request));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<CartDto> updateItemQuantity(@PathVariable Long id, @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateItemQuantity(id, request));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<CartDto> removeItemFromCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.removeItemFromCart(id));
    }
}
