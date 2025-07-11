package in.mukesh.service;

import in.mukesh.entity.CartItemEntity;
import in.mukesh.entity.ProductEntity;
import in.mukesh.entity.ProductImage;
import in.mukesh.repository.CartItemRepository;
import in.mukesh.repository.IProductRepo;
import in.mukesh.repository.ProductImageRepo;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;

<<<<<<< HEAD
import java.io.File;
import java.io.FileOutputStream;
=======
>>>>>>> 8f6f04d3fbba3fca572fc7a87e375a480f85a90a
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private IProductRepo pRepo;

    @Autowired
    private ProductImageRepo imageRepo;

    @Autowired
    private CartItemRepository cartRepo;

<<<<<<< HEAD
    @Autowired
    private AmazonS3 s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

=======
>>>>>>> 8f6f04d3fbba3fca572fc7a87e375a480f85a90a
    @Transactional
    public String saveProductWithImage(ProductEntity product, MultipartFile imageFile) {
        try {
            // Save product to generate ID
            ProductEntity savedProduct = pRepo.save(product);

            // Generate unique file name
            String originalName = imageFile.getOriginalFilename();
            String extension = (originalName != null && originalName.contains("."))
                    ? originalName.substring(originalName.lastIndexOf("."))
                    : "";
            String fileName = UUID.randomUUID() + extension;

            // Convert MultipartFile to File
            File file = convertToFile(imageFile);

            // Upload to S3 (no ACL)
            PutObjectRequest putRequest = new PutObjectRequest(bucketName, fileName, file);
            s3Client.putObject(putRequest);

            // Clean up temporary file
            file.delete();

            // Save image path to DB
            ProductImage image = new ProductImage();
            image.setProduct(savedProduct);
            image.setImagePath(fileName); // Only filename
            imageRepo.save(image);

            return "Product with image saved successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to save product or image: " + e.getMessage();
        }
    }

<<<<<<< HEAD
    private File convertToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipartFile.getBytes());
        }
        return convFile;
    }

    public InputStreamResource getImageFromS3(String filename) {
        S3Object s3Object = s3Client.getObject(bucketName, filename);
        return new InputStreamResource(s3Object.getObjectContent());
    }

=======
>>>>>>> 8f6f04d3fbba3fca572fc7a87e375a480f85a90a
    public List<ProductEntity> getProductsByCategory(String category) {
        return pRepo.findByCategoryIgnoreCase(category);
    }

    public List<ProductEntity> getByCreatedBy(String createdBy) {
        List<ProductEntity> products = pRepo.findByCreatedBy(createdBy);
<<<<<<< HEAD
=======

>>>>>>> 8f6f04d3fbba3fca572fc7a87e375a480f85a90a
        for (ProductEntity product : products) {
            if (product.getImage() != null) {
                product.getImage().getImagePath(); // Ensure lazy-loaded image path is initialized
            }
        }
        return products;
    }

    @Transactional
    public String addToCart(Long productId, String username, String address, String phone) {
<<<<<<< HEAD
        ProductEntity product = pRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() <= 0) {
            throw new RuntimeException("Product out of stock");
        }
=======
        ProductEntity product = pRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() <= 0)
            throw new RuntimeException("Product out of stock");
>>>>>>> 8f6f04d3fbba3fca572fc7a87e375a480f85a90a

        product.setQuantity(product.getQuantity() - 1);
        pRepo.save(product);

        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setProduct(product);
        cartItem.setUsername(username);
        cartItem.setAddress(address);
        cartItem.setPhone(phone);
        cartRepo.save(cartItem);

        return "Product added to cart!";
    }

    public List<CartItemEntity> getAllCartItemsByAdmin(String adminUsername) {
<<<<<<< HEAD
        return cartRepo.findAll().stream()
                .filter(cart -> adminUsername.equals(cart.getProduct().getCreatedBy()))
                .toList();
    }
=======
        List<CartItemEntity> allCartItems = cartRepo.findAll();

        return allCartItems.stream()
                .filter(cart -> adminUsername.equals(cart.getProduct().getCreatedBy()))
                .toList();
    }

>>>>>>> 8f6f04d3fbba3fca572fc7a87e375a480f85a90a
}
