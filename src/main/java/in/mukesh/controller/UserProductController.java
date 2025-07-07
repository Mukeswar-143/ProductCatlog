package in.mukesh.controller;

import in.mukesh.entity.ProductEntity;
import in.mukesh.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:3001",
        "https://shopverse-beta.vercel.app"
})
public class UserProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<ProductEntity> getProductsByCategory(@RequestParam String category) {
        return productService.getProductsByCategory(category);
    }

    @PostMapping("/cart/add/{productId}")
    public ResponseEntity<String> addToCart(@PathVariable Long productId,
            @RequestBody Map<String, String> userDetails) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            String address = userDetails.get("address");
            String phone = userDetails.get("phone");

            String result = productService.addToCart(productId, username, address, phone);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
