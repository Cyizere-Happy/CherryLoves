package com.unica.cherryLoves.controller;

import com.unica.cherryLoves.exceptions.ResourceNotFoundException;
import com.unica.cherryLoves.models.Cart;
import com.unica.cherryLoves.models.User;
import com.unica.cherryLoves.response.ApiResponse;
import com.unica.cherryLoves.security.user.UserDetailsImpl;
import com.unica.cherryLoves.service.cart.ICartItemService;
import com.unica.cherryLoves.service.cart.ICartService;
import com.unica.cherryLoves.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {
            User user = getAuthenticatedUser();
            Cart cart = cartService.initializeNewCart(user);
            cartItemService.addItemToCart(cart.getId(), productId, null, quantity);
            return ResponseEntity.ok(new ApiResponse("Add Item Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{productId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        try {
            User user = getAuthenticatedUser();
            Cart cart = cartService.getCartByUserId(user.getId());
            if (!cart.getId().equals(cartId)) {
                return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse("Unauthorized access to cart", null));
            }
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("Remove Item Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/cart/{cartId}/item/{productId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long productId,
                                                          @RequestParam Integer quantity) {
        try {
            User user = getAuthenticatedUser();
            Cart cart = cartService.getCartByUserId(user.getId());
            if (!cart.getId().equals(cartId)) {
                return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse("Unauthorized access to cart", null));
            }
            cartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Update Item Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userService.getUserById(userDetails.getId());
    }
}
