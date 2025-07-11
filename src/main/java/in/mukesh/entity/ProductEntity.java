package in.mukesh.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;

@Entity
@Table(name = "product")
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String pname;
	private String category;
	private Long price;
	private Integer quantity;
	@Column(length  = 1500, nullable = false)
	private String pdesc;
	private String modelName;
	private String color;

	@Column(name = "created_by")
	private String createdBy;

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private LocalDate createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date", insertable = false)
	private LocalDate updatedDate;

	public String getModelName() {
		return modelName;
	}

	public String getColor() {
		return color;
	}

	@OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonManagedReference
	private ProductImage image;

	public Long getId() {
		return id;
	}

	public String getPname() {
		return pname;
	}

	public String getCategory() {
		return category;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setImage(ProductImage image) {
		this.image = image;
	}

	public Long getPrice() {
		return price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public String getPdesc() {
		return pdesc;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public LocalDate getUpdatedDate() {
		return updatedDate;
	}

	public ProductImage getImage() {
		return image;
	}

}
