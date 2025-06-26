package in.mukesh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.mukesh.entity.ProductEntity;
import in.mukesh.repository.IProductRepo;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

    @Autowired
    private IProductRepo pRepo;

    public String saveProduct(ProductEntity productEntity) {
        pRepo.save(productEntity);
        return "success";
    }

    public ProductEntity getByPid(Long pid) {
        return pRepo.findByPid(pid);
    }

    public List<ProductEntity> getAll() {
        return pRepo.findAll();
    }

    // public List<ProductEntity> getProducts(String category, String sortBy, String
    // direction, Integer page, Integer size) {
    // if (page == null || size == null) {
    // if (category != null && !category.trim().isEmpty()) {
    // return pRepo.findByCategoryIgnoreCase(category);
    // } else {
    // return pRepo.findAll();
    // }
    // }
    // Sort sort = (sortBy != null && direction != null)
    // ? Sort.by(Sort.Direction.fromString(direction), sortBy)
    // : Sort.unsorted();
    //
    // Pageable pageable = PageRequest.of(page, size, sort);
    //
    // if (category != null && !category.trim().isEmpty()) {
    // return pRepo.findByCategoryIgnoreCase(category, pageable).getContent();
    // } else {
    // return pRepo.findAll(pageable).getContent();
    // }
    // }

    public Page<ProductEntity> getProducts(String category, String sortBy, String direction, int page, int size) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (category != null && !category.isEmpty()) {
            return pRepo.findByCategoryIgnoreCase(category, pageable);
        } else {
            return pRepo.findAll(pageable);
        }
    }

    public Page<ProductEntity> getAllProducts(Pageable pageable) {
        return pRepo.findAll(pageable);
    }

    @Transactional
    public boolean deleteProductByPid(Long pid) {
        ProductEntity product = pRepo.findByPid(pid);
        if (product != null) {

            pRepo.delete(product);
            return true;
        }
        return false;
    }
}
