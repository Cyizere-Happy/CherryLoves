package com.unica.cherryLoves.controller;

import com.unica.cherryLoves.dto.CartDto;
import com.unica.cherryLoves.exceptions.ResourceNotFoundException;
import com.unica.cherryLoves.models.Cart;
import com.unica.cherryLoves.response.ApiResponse;
import com.unica.cherryLoves.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.unica.cherryLoves.security.user.UserDetailsImpl;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    @GetMapping("/my-cart")
    public ResponseEntity<ApiResponse> getCart() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Cart cart = cartService.getCartByUserId(userDetails.getId());
            if (cart == null) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart not found", null));
            }
            CartDto cartDto = cartService.convertToDto(cart);
            return ResponseEntity.ok(new ApiResponse("success", cartDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearCart(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Cart cart = cartService.getCartByUserId(userDetails.getId());
        if (cart != null) {
            cartService.clearCart(cart.getId());
        }
        return ResponseEntity.ok(new ApiResponse("Clear Cart Success!", null));
    }

    @GetMapping("/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Cart cart = cartService.getCartByUserId(userDetails.getId());
            BigDecimal totalPrice = cartService.getTotalPrice(cart.getId());
            return ResponseEntity.ok(new ApiResponse("Total Price", totalPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}

