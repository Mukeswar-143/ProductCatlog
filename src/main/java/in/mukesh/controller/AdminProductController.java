package in.mukesh.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.mukesh.entity.ProductEntity;
import in.mukesh.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3001")
public class AdminProductController {

    @Autowired
    private ProductService pService;

    @Autowired
    private ObjectMapper objectMapper;

    // Get all products for the logged-in admin
    @GetMapping("/products")
    public ResponseEntity<List<ProductEntity>> getProductsForAdmin() {
        String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ProductEntity> products = pService.getByCreatedBy(adminUsername);
        return products.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(products);
    }

    // Basic dashboard endpoint
    @GetMapping("/dashboard")
    public String getDashboard() {
        return "Welcome to the Admin Dashboard!";
    }

    // Upload product with image (multipart form-data)
    @PostMapping(value = "/product/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProductWithImage(
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile imageFile) {

        try {
            // Convert JSON string to ProductEntity
            ProductEntity product = objectMapper.readValue(productJson, ProductEntity.class);
            String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            product.setCreatedBy(adminUsername);
            String uploadDir = System.getProperty("user.dir") + "/images";
            new File(uploadDir).mkdirs(); // Ensure the directory exists

            // Save product and image
            String result = pService.saveProductWithImage(product, imageFile, uploadDir);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid product upload request. Error: " + e.getMessage());
        }
    }
}
