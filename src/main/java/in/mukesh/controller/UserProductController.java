package in.mukesh.controller;

import in.mukesh.entity.ProductEntity;
import in.mukesh.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.http.*;
=======
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
>>>>>>> 8f6f04d3fbba3fca572fc7a87e375a480f85a90a
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:3001",
        "https://shopverse-oubo.vercel.app"
})
public class UserProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
<<<<<<< HEAD
    public ResponseEntity<List<ProductEntity>> getProductsByCategory(@RequestParam String category) {
        if (category == null || category.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<ProductEntity> products = productService.getProductsByCategory(category.trim());
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);
    }

    @PostMapping("/cart/add/{productId}")
    public ResponseEntity<String> addToCart(
            @PathVariable Long productId,
            @RequestBody Map<String, String> userDetails) {

=======
    public List<ProductEntity> getProductsByCategory(@RequestParam String category) {
        return productService.getProductsByCategory(category);
    }

    @PostMapping("/cart/add/{productId}")
    public ResponseEntity<String> addToCart(@PathVariable Long productId,
            @RequestBody Map<String, String> userDetails) {
>>>>>>> 8f6f04d3fbba3fca572fc7a87e375a480f85a90a
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            String address = userDetails.get("address");
            String phone = userDetails.get("phone");

<<<<<<< HEAD
            if (address == null || address.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Address and phone number are required.");
            }

            String result = productService.addToCart(productId, username, address.trim(), phone.trim());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding to cart: " + e.getMessage());
        }
    }
=======
            String result = productService.addToCart(productId, username, address, phone);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

>>>>>>> 8f6f04d3fbba3fca572fc7a87e375a480f85a90a
}
