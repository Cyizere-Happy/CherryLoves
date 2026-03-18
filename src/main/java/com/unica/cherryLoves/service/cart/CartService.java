package com.unica.cherryLoves.service.cart;

import com.unica.cherryLoves.dto.CartDto;
import com.unica.cherryLoves.exceptions.ResourceNotFoundException;
import com.unica.cherryLoves.models.Cart;
import com.unica.cherryLoves.models.CartItem;
import com.unica.cherryLoves.repository.CartItemRepository;
import com.unica.cherryLoves.repository.CartRepository;
import com.unica.cherryLoves.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cart;
    }

    @Override
    public void clearCart(long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeNewCart(com.unica.cherryLoves.models.User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public CartDto convertToDto(Cart cart) {
        return modelMapper.map(cart, CartDto.class);
    }
}
