package com.ecommece.service;

import com.ecommece.dto.AddToCartRequest;
import com.ecommece.dto.CartDto;
import com.ecommece.dto.CartItemDto;
import com.ecommece.dto.UpdateCartItemRequest;
import com.ecommece.entity.Cart;
import com.ecommece.entity.CartItem;
import com.ecommece.entity.Product;
import com.ecommece.entity.User;
import com.ecommece.exception.BadRequestException;
import com.ecommece.exception.ResourceNotFoundException;
import com.ecommece.repository.CartItemRepository;
import com.ecommece.repository.CartRepository;
import com.ecommece.repository.ProductRepository;
import com.ecommece.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserService currentUserService;

    public CartDto getCartForCurrentUser() {
        Cart cart = getOrCreateCart();
        return mapToDto(cart);
    }

    public CartDto addItemToCart(AddToCartRequest request) {
        Cart cart = getOrCreateCart();
        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
            cart.getCartItems().add(newItem);
        }

        return mapToDto(cart);
    }

    public CartDto updateItemQuantity(Long itemId, UpdateCartItemRequest request) {
        Cart cart = getOrCreateCart();
        
        if (request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Item does not belong to your cart");
        }

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        return mapToDto(cart);
    }

    public CartDto removeItemFromCart(Long itemId) {
        Cart cart = getOrCreateCart();

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Item does not belong to your cart");
        }

        cartItemRepository.delete(item);
        cart.getCartItems().remove(item);

        return mapToDto(cart);
    }

    private Cart getOrCreateCart() {
        String email = currentUserService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    private CartDto mapToDto(Cart cart) {
        List<CartItemDto> items = cart.getCartItems().stream()
                .map(item -> new CartItemDto(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                )).collect(Collectors.toList());

        Double totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        return new CartDto(cart.getId(), items, totalAmount);
    }
}
