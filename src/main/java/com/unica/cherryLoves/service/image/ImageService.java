package com.unica.cherryLoves.service.image;

import com.unica.cherryLoves.dto.ImageDto;
import com.unica.cherryLoves.exceptions.ResourceNotFoundException;
import com.unica.cherryLoves.models.Image;
import com.unica.cherryLoves.models.Product;
import com.unica.cherryLoves.repository.ImageRepository;
import com.unica.cherryLoves.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageReposirtory;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageReposirtory.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No image found with id: " + id));
    }

    @Override
    public void deleteImageById(Long id) {
       imageReposirtory.findById(id).ifPresentOrElse(imageReposirtory::delete, () -> {
           throw new ResourceNotFoundException("No image found with id: " + id);
       });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDtos = new ArrayList<>();

        for (MultipartFile file : files){
            try{
                validateFile(file);
                String sanitizedFileName = sanitizeFileName(file.getOriginalFilename());

                Image image = new Image();
                image.setFileName(sanitizedFileName);
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                Image savedImage = imageReposirtory.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imageReposirtory.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());

                savedImageDtos.add(imageDto);

            }catch(IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDtos;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            validateFile(file);
            String sanitizedFileName = sanitizeFileName(file.getOriginalFilename());
            image.setFileName(sanitizedFileName);
            image.setImage(new SerialBlob(file.getBytes()));
            imageReposirtory.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !isValidImageType(contentType)) {
            throw new IllegalArgumentException("Invalid file type: " + contentType);
        }
    }

    private boolean isValidImageType(String contentType) {
        return List.of("image/jpeg", "image/png", "image/gif", "image/webp").contains(contentType);
    }

    private String sanitizeFileName(String fileName) {
        if (fileName == null) return "image";
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
