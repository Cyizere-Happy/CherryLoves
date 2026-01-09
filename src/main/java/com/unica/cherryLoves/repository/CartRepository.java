package com.unica.cherryLoves.repository;

import com.unica.cherryLoves.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
