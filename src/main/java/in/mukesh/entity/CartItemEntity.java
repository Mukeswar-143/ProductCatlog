package in.mukesh.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_items")
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProductEntity product;

    @Column(nullable = false)
    private String username;

    private String address;
    private String phone;

    @Column(nullable = false)
    private Integer quantity = 1;

    private LocalDate addedAt = LocalDate.now();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDate getAddedAt() {
        return addedAt;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setAddedAt(LocalDate addedAt) {
        this.addedAt = addedAt;
    }

   
}

