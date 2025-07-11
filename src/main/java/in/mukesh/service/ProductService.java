package in.mukesh.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import in.mukesh.entity.CartItemEntity;
import in.mukesh.entity.ProductEntity;
import in.mukesh.entity.ProductImage;
import in.mukesh.repository.CartItemRepository;
import in.mukesh.repository.IProductRepo;
import in.mukesh.repository.ProductImageRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
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

    @Autowired
    private AmazonS3 s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

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

            // Upload to S3
            PutObjectRequest putRequest = new PutObjectRequest(bucketName, fileName, file);
            s3Client.putObject(putRequest);

            // Clean up temporary file
            file.delete();

            // Save image path to DB
            ProductImage image = new ProductImage();
            image.setProduct(savedProduct);
            image.setImagePath(fileName);
            imageRepo.save(image);

            return "Product with image saved successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to save product or image: " + e.getMessage();
        }
    }

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

    public List<ProductEntity> getProductsByCategory(String category) {
        return pRepo.findByCategoryIgnoreCase(category);
    }

    public List<ProductEntity> getByCreatedBy(String createdBy) {
        List<ProductEntity> products = pRepo.findByCreatedBy(createdBy);

        // Ensure image path is initialized (in case of lazy loading)
        for (ProductEntity product : products) {
            if (product.getImage() != null) {
                product.getImage().getImagePath();
            }
        }
        return products;
    }

    @Transactional
    public String addToCart(Long productId, String username, String address, String phone) {
        ProductEntity product = pRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() <= 0) {
            throw new RuntimeException("Product out of stock");
        }

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
        return cartRepo.findAll().stream()
                .filter(cart -> adminUsername.equals(cart.getProduct().getCreatedBy()))
                .toList();
    }
}
