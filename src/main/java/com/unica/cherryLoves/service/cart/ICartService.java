package com.unica.cherryLoves.service.cart;

import com.unica.cherryLoves.models.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(long id);
    BigDecimal getTotalPrice(Long id);
}
