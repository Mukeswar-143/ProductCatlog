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
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:3001",
        "https://shopverse-beta.vercel.app"
})
public class AdminProductController {

    @Autowired
    private ProductService pService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String IMAGE_UPLOAD_DIR = System.getProperty("user.dir") + "/images";

    @GetMapping("/products")
    public ResponseEntity<List<ProductEntity>> getProductsForAdmin() {
        String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ProductEntity> products = pService.getByCreatedBy(adminUsername);

        return products.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(products);
    }

    @GetMapping("/dashboard")
    public String getDashboard() {
        return "Welcome to the Admin Dashboard!";
    }

    @PostMapping(value = "/product/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProductWithImage(
            @RequestPart("product") String productJson,
            @RequestPart("image") MultipartFile imageFile) {

        try {
            // Deserialize product JSON to entity
            ProductEntity product = objectMapper.readValue(productJson, ProductEntity.class);

            // Set createdBy to currently authenticated user
            String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            product.setCreatedBy(adminUsername);

            // Ensure image directory exists
            File uploadDir = new File(IMAGE_UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            // Save the product and image
            String result = pService.saveProductWithImage(product, imageFile, IMAGE_UPLOAD_DIR);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid product upload request. Error: " + e.getMessage());
        }
    }
}
