package in.mukesh.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.mukesh.entity.CartItemEntity;
import in.mukesh.entity.ProductEntity;
import in.mukesh.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:3001",
        "https://shopverse-oubo.vercel.app"
})
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMappe objectMapper;

    @GetMapping("/products")
    public ResponseEntity<List<ProductEntity>> getProductsForAdmin() {
        String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ProductEntity> products = productService.getByCreatedBy(adminUsername);

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(products);
        }
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
            ProductEntity product = objectMapper.readValue(productJson, ProductEntity.class);
            String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            product.setCreatedBy(adminUsername);

            String result = productService.saveProductWithImage(product, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid product upload request. Error: " + e.getMessage());
        }
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartItemEntity>> getAllCartItems() {
        String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CartItemEntity> carts = productService.getAllCartItemsByAdmin(adminUsername);

        if (carts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(carts);
    }
}
