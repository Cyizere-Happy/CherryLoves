package com.unica.cherryLoves.service.cart;

import com.unica.cherryLoves.models.Cart;
import com.unica.cherryLoves.models.CartItem;

import java.math.BigDecimal;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId,CartItem cartItem, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);
}
