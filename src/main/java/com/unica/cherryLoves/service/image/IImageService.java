package com.unica.cherryLoves.service.image;

import com.unica.cherryLoves.dto.ImageDto;
import com.unica.cherryLoves.models.Image;
import com.unica.cherryLoves.models.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> file, Long productId);
    void updateImage(MultipartFile file, Long productId);
}
