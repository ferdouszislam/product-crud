package com.dbl.nsl.productcrud.product;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

public interface ProductService {

	public Product createProduct(Product product, MultipartFile productPhoto) throws ResponseStatusException;
	
	public List<Product> createProducts(List<Product> products) throws ResponseStatusException;
	
	public List<Product> uploadProductPhotos(Map<String, MultipartFile> productIdToPhotos) throws ResponseStatusException;
	
	public Product readProduct(Long productId);
	
	public List<Product> readProducts(Map<String, List<Object>> params);
	
	public List<Category> readCategoryNameWiseProductNames();
	
	public Product updateProduct(Long productId, Product product) throws ResponseStatusException;
	
	public Product uploadProductPhoto(Long productId, MultipartFile productPhoto) throws ResponseStatusException;
	
	public void deleteProduct(Long productId) throws ResponseStatusException;
	
}
