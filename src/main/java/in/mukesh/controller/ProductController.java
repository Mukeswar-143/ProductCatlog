package in.mukesh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.mukesh.entity.ProductEntity;
import in.mukesh.service.ProductService;

//http://localhost:8080/product/getProducts?page=0&size=10&sortBy=price&direction=desc&category=Mobile

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://shopverse-beta.vercel.app"
})
public class ProductController {

    @Autowired
    private ProductService pService;

    @PostMapping("/entry")
    public ResponseEntity<String> saveProduct(@RequestBody ProductEntity pEntity) {
        if (pService.getByPid(pEntity.getPid()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Product with same pid already exists");
        } else {
            pService.saveProduct(pEntity);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Product added successfully");
        }
    }

    @GetMapping("/all")
    public List<ProductEntity> getAll() {
        return pService.getAll();
    }

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<ProductEntity> getByPid(@PathVariable Long id) {
        ProductEntity product = pService.getByPid(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    //
    // @GetMapping("/getProducts")
    // public ResponseEntity<List<ProductEntity>> getProducts(
    // @RequestParam(required = false) String category,
    // @RequestParam(required = false) String sortBy,
    // @RequestParam(required = false) String direction,
    // @RequestParam(required = false) Integer page,
    // @RequestParam(required = false) Integer size) {
    // try {
    // List<ProductEntity> products = pService.getProducts(category, sortBy,
    // direction, page, size);
    // return ResponseEntity.ok(products);
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    // }
    // }

    @GetMapping("/getProducts")
    public ResponseEntity<Page<ProductEntity>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ProductEntity> products = pService.getProducts(category, sortBy, direction, page, size);
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/deleteProductById/{pid}")
    public ResponseEntity<String> deleteProductByPid(@PathVariable Long pid) {
        boolean deleted = pService.deleteProductByPid(pid);
        if (deleted) {
            return ResponseEntity.ok("Product deleted successfully with pid: " + pid);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid Product ID");
        }
    }
}
