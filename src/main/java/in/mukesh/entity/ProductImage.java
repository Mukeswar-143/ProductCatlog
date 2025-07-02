package in.mukesh.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "product_image")
public class ProductImage {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private ProductEntity product;

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    public Long getId() {
        return id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public String getImagePath() {
        return imagePath;
    }

}
