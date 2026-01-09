package com.unica.cherryLoves.repository;

import com.unica.cherryLoves.models.Image;
import com.unica.cherryLoves.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long>{
    List<Image> findByProductId(Long id);
}
