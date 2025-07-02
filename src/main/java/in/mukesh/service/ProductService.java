package in.mukesh.service;

import in.mukesh.entity.ProductEntity;
import in.mukesh.entity.ProductImage;
import in.mukesh.repository.IProductRepo;
import in.mukesh.repository.ProductImageRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private IProductRepo pRepo;

    @Autowired
    private ProductImageRepo imageRepo;

    
    @Transactional
    public String saveProductWithImage(ProductEntity product, MultipartFile imageFile, String uploadDir) {
        try {
            // Save product details first
            ProductEntity savedProduct = pRepo.save(product);

            // Generate a unique image filename
            String originalName = imageFile.getOriginalFilename();
            String extension = "";

            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID() + extension;
            Path imagePath = Paths.get(uploadDir, fileName);

            // Create directories if they don't exist
            Files.createDirectories(imagePath.getParent());

            // Save image file to disk
            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            // Save image path in the DB
            ProductImage image = new ProductImage();
            image.setProduct(savedProduct);
            image.setImagePath("/images/" + fileName); // relative path for browser
            imageRepo.save(image);

            return "Product with image saved successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to save product or image: " + e.getMessage();
        }
    }

   
    public List<ProductEntity> getByCreatedBy(String createdBy) {
        List<ProductEntity> products = pRepo.findByCreatedBy(createdBy);

       
        for (ProductEntity product : products) {
            if (product.getImage() != null) {
                product.getImage().getImagePath(); // Ensures image data is fetched
            }
        }

        return products;
    }
}
