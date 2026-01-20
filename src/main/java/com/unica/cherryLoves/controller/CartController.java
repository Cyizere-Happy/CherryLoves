package com.unica.cherryLoves.controller;

import com.unica.cherryLoves.exceptions.ResourceNotFoundException;
import com.unica.cherryLoves.models.Cart;
import com.unica.cherryLoves.response.ApiResponse;
import com.unica.cherryLoves.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("{api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    @GetMapping("{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable long cartId) {
        try {
            Cart cart = cartService.getCart(cartId);
            return ResponseEntity.ok(new ApiResponse("success", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId){
        cartService.clearCart(cartId);
        return ResponseEntity.ok(new ApiResponse("Clear Cart Success!", null));
    }

    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId){
        try {
            BigDecimal totalPrice = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("Total Price", totalPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    }

