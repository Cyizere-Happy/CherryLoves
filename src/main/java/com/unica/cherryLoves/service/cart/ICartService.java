package com.unica.cherryLoves.service.cart;

import com.unica.cherryLoves.dto.CartDto;
import com.unica.cherryLoves.models.Cart;
import com.unica.cherryLoves.models.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(long id);
    BigDecimal getTotalPrice(Long id);
    Cart initializeNewCart(User user);
    Cart getCartByUserId(Long userId);

    CartDto convertToDto(Cart cart);
}
