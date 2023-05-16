package com.dbl.nsl.productcrud.product;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dbl.nsl.productcrud.common.MessageResponse;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createProduct(
			@Valid @RequestPart(name = "product", required = true) Product product, 
			@RequestPart(name = "productPhoto", required = false) MultipartFile productPhoto) {
		return ResponseEntity.ok(productService.createProduct(product, productPhoto));
	}
	
	@RequestMapping(value = "/multiple", method = RequestMethod.POST)
	public ResponseEntity<?> createProducts(@Valid @RequestBody List<Product> products) {
		return ResponseEntity.ok(productService.createProducts(products));
	}
	
	@RequestMapping(value = "/mutiple-product-photos", method = RequestMethod.POST)
	public ResponseEntity<?> uploadProductPhotos(@RequestParam Map<Long, MultipartFile> productIdToPhotos) {
		return ResponseEntity.ok(productService.uploadProductPhotos(productIdToPhotos));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> readProducts(Map<String, List<Object>> params) {
		return ResponseEntity.ok(productService.readProducts(params));
	}
	
	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public ResponseEntity<?> readProductById(@PathVariable("productId") Long productId) {
		return ResponseEntity.ok(productService.readProduct(productId));
	}
	
	@RequestMapping(value = "/category-name-wise-product-names", method = RequestMethod.GET)
	public ResponseEntity<?> readCategoryNameWiseProductNames() {
		return ResponseEntity.ok(productService.readCategoryNameWiseProductNames());
	}
	
	@RequestMapping(value = "/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProduct(@PathVariable("productId") Long productId, 
			@Valid @RequestBody Product product) {
		return ResponseEntity.ok(productService.updateProduct(productId, product));
	}
	
	@RequestMapping(value = "/{productId}/upload-product-photo", method = RequestMethod.PUT)
	public ResponseEntity<?> uploadProductPhoto(@PathVariable("productId") Long productId, 
			@RequestPart(name = "productPhoto", required = true) MultipartFile productPhoto) {
		return ResponseEntity.ok(productService.uploadProductPhoto(productId, productPhoto));
	}
	
	@RequestMapping(value = "/{productId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteProduct(@PathVariable("productId") Long productId) {
		productService.deleteProduct(productId);
		return ResponseEntity.ok(new MessageResponse("product deleted!"));
	}
	
}
