package com.dbl.nsl.productcrud.product;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.dbl.nsl.productcrud.common.Photo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String assetNumber;
	
	@JsonIgnore
	private String createdBy;
	
	@NotBlank
	private String categoryName;
	
	@NotBlank
	private String productName;
	
	private String serialNumber;
	
	@NotNull
	private Double purchasePrice;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date purchaseDate;
	
	private Long warrantyInYears;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date warrantyExpireDate;

	@Embedded
	private Photo productPhoto;

	public Product() {
		super();
	}

	public Product(String assetNumber, String createdBy,
			@NotBlank String categoryName, @NotBlank String productName,
			String serialNumber, @NotNull Double purchasePrice,
			@NotNull Date purchaseDate, Long warrantyInYears,
			@NotNull Date warrantyExpireDate, Photo productPhoto) {
		super();
		this.assetNumber = assetNumber;
		this.createdBy = createdBy;
		this.categoryName = categoryName;
		this.productName = productName;
		this.serialNumber = serialNumber;
		this.purchasePrice = purchasePrice;
		this.purchaseDate = purchaseDate;
		this.warrantyInYears = warrantyInYears;
		this.warrantyExpireDate = warrantyExpireDate;
		this.productPhoto = productPhoto;
	}

	public Long getId() {
		return id;
	}

	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Long getWarrantyInYears() {
		return warrantyInYears;
	}

	public void setWarrantyInYears(Long warrantyInYears) {
		this.warrantyInYears = warrantyInYears;
	}

	public Date getWarrantyExpireDate() {
		return warrantyExpireDate;
	}

	public void setWarrantyExpireDate(Date warrantyExpireDate) {
		this.warrantyExpireDate = warrantyExpireDate;
	}

	public Photo getProductPhoto() {
		return productPhoto;
	}

	public void setProductPhoto(Photo productPhoto) {
		this.productPhoto = productPhoto;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", createdBy=" + createdBy
				+ ", categoryName=" + categoryName + ", productName="
				+ productName + ", serialNumber=" + serialNumber
				+ ", purchasePrice=" + purchasePrice + ", purchaseDate="
				+ purchaseDate + ", warrantyInYears=" + warrantyInYears
				+ ", warrantyExpireDate=" + warrantyExpireDate
				+ ", productPhoto=" + productPhoto + "]";
	}
	
}
