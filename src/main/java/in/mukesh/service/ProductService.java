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

    // ✅ Save product with image
    @Transactional
    public String saveProductWithImage(ProductEntity product, MultipartFile imageFile, String uploadDir) {
        try {
            // Save product first
            ProductEntity savedProduct = pRepo.save(product);

            // Save image to local folder
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir, fileName);
            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            // Create and save image entity
            ProductImage image = new ProductImage();
            image.setProduct(savedProduct);
            image.setImagePath("/images/" + fileName); // Relative URL for browser access
            imageRepo.save(image);

            return "Product with image saved successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error saving product or image.";
        }
    }

    // ✅ Get all products created by this admin (including image)
    public List<ProductEntity> getByCreatedBy(String createdBy) {
        List<ProductEntity> products = pRepo.findByCreatedBy(createdBy);

        // Optional: Trigger lazy loading if needed
        for (ProductEntity product : products) {
            if (product.getImage() != null) {
                product.getImage().getImagePath(); // ensures image is loaded
            }
        }

        return products;
    }

   
}
