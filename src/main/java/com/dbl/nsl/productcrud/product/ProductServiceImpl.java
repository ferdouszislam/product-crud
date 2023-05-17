package com.dbl.nsl.productcrud.product;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.dbl.nsl.productcrud.common.ApiKeyHelper;
import com.dbl.nsl.productcrud.common.FileUploaderUtil;
import com.dbl.nsl.productcrud.common.FileUploaderUtil.ItemType;
import com.dbl.nsl.productcrud.common.HttpStatusAndMessage;
import com.dbl.nsl.productcrud.common.UpdateFieldsHelper;

@Service
public class ProductServiceImpl implements ProductService {

	private Logger Log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ApiKeyHelper apiKeyHelper;

	@Override
	public Product createProduct(Product product, MultipartFile productPhoto) throws ResponseStatusException {
		Log.info("createProduct called with product: {}, username: {}", product, apiKeyHelper.getUsernameFromHeader());
		HttpStatusAndMessage v = validateOnCreate(product);
		if (Objects.nonNull(v)) {
			Log.info("validation fail: {}", v);
			throw new ResponseStatusException(v.getStatus(), v.getMessage());
		}
		product.setCreatedBy(apiKeyHelper.getUsernameFromHeader());
		product = productRepository.saveAndFlush(product);
		product.setAssetNumber(getAssetNumber(product));
		if (Objects.nonNull(productPhoto) && !productPhoto.isEmpty()) {
			try {
				product.setProductPhoto(FileUploaderUtil
						.savePhoto("productPhoto", ItemType.PRODUCT_PHOTO, product.getId(), productPhoto, true, true));
			} catch (IndexOutOfBoundsException | IOException e) {
				Log.info("failed to save product photo. exception: {}", e);
			}
		}
		return productRepository.saveAndFlush(product);
	}

	@Override
	public List<Product> createProducts(List<Product> products) throws ResponseStatusException {
		Log.info("createProducts called with products: {}, username: {}", 
				products, apiKeyHelper.getUsernameFromHeader());
		for (int i = 0; i < products.size(); i++) {
			HttpStatusAndMessage v = validateOnCreate(products.get(i));
			Product p = products.get(i);
			if (Objects.nonNull(v)) {
				Log.info("validation fail at index: {}, reason: {}", i, v);
				v.setMessage(v.getMessage() + " [at index = " + i + "]");
				throw new ResponseStatusException(v.getStatus(), v.getMessage());
			}
			p.setCreatedBy(apiKeyHelper.getUsernameFromHeader());
			p = productRepository.saveAndFlush(p);
			p.setAssetNumber(getAssetNumber(p));
			products.set(i, productRepository.saveAndFlush(p));
		}
		return products;
	}

	@Override
	public List<Product> uploadProductPhotos(Map<String, MultipartFile> productIdToPhotos) throws ResponseStatusException {
		Log.info("uploadProductPhotos called with productIds: {}, username: {}", 
				productIdToPhotos.keySet(), apiKeyHelper.getUsernameFromHeader());
		List<Product> products = new ArrayList<>();
		for (Map.Entry<String, MultipartFile> p : productIdToPhotos.entrySet()) {
			Long productId = Long.valueOf(p.getKey());
			MultipartFile productPhoto = p.getValue();
			if (Objects.isNull(productId) || Objects.isNull(productPhoto) || productPhoto.isEmpty()) continue;
			Product product = productRepository.findById(productId).orElse(null);
			if (Objects.isNull(product)) continue;
			try {
				product.setProductPhoto(FileUploaderUtil
						.savePhoto("productPhoto", ItemType.PRODUCT_PHOTO, product.getId(), productPhoto, true, true));
			} catch (IndexOutOfBoundsException | IOException e) {
				Log.info("failed to save product photo. exception: {}", e);
			}
			products.add(product);
		}
		return productRepository.saveAll(products);
	}

	@Override
	public Product readProduct(Long productId) {
		Log.info("readProduct called with productId: {}, username: {}", 
				productId, apiKeyHelper.getUsernameFromHeader());
		return getProductById(productId);
	}

	@Override
	public List<Product> readProducts(Map<String, List<Object>> params) {
		Log.info("readProducts called with params: {}, username: {}", params, apiKeyHelper.getUsernameFromHeader());
		if (apiKeyHelper.isAdmin()) {
			return productRepository.findAll();
		}
		if (Objects.isNull(apiKeyHelper.getUsernameFromHeader())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "no apiKey provided in header");
		}
		return productRepository.findAllByCreatedBy(apiKeyHelper.getUsernameFromHeader());
	}
	
	@Override
	public List<Category> readCategoryNameWiseProductNames() {
		Map<String, List<String>> categoryNameWiseProductNames = getCategoryNameWiseProductNames();
		List<Category> categories = new ArrayList<>();
		for (Map.Entry<String, List<String>> cP : categoryNameWiseProductNames.entrySet()) {
			List<ProductName> products = new ArrayList<>();
			for (String p : cP.getValue()) {
				products.add(new ProductName(p));
			}
			categories.add(new Category(cP.getKey(), products));
		}
		return categories;
	}

	@Override
	public Product updateProduct(Long productId, Product product) 
			throws ResponseStatusException {
		Log.info("updateProduct called with productId: {}, product: {}, username: {}", 
				productId, product, apiKeyHelper.getUsernameFromHeader());
		Product existingProduct = getProductById(productId);
		HttpStatusAndMessage v = validateOnUpdate(productId, product);
		if (Objects.nonNull(v)) {
			throw new ResponseStatusException(v.getStatus(), v.getMessage());
		}
		existingProduct = UpdateFieldsHelper
				.updateFields(existingProduct, product, 
						Arrays.asList("categoryName", "productName", "serialNumber", 
								"purchasePrice", "purchaseDate", "warrantyInYears", "warrantyExpireDate"), false);
		existingProduct.setAssetNumber(getAssetNumber(existingProduct));
		return productRepository.saveAndFlush(existingProduct);
	}
	
	@Override
	public Product uploadProductPhoto(Long productId, MultipartFile productPhoto) throws ResponseStatusException {
		Log.info("uploadProductPhoto called with productId: {},  username: {}", 
				productId, apiKeyHelper.getUsernameFromHeader());
		Product product = getProductById(productId);
		try {
			product.setProductPhoto(FileUploaderUtil
					.savePhoto("productPhoto", ItemType.PRODUCT_PHOTO, product.getId(), productPhoto, true, true));
		} catch (IndexOutOfBoundsException | IOException e) {
			Log.info("failed to save product photo. exception: {}", e);
		}
		return productRepository.saveAndFlush(product);
	}

	@Override
	public void deleteProduct(Long productId) throws ResponseStatusException {
		Log.info("deleteProduct called with productId: {}, username: {}", 
				productId, apiKeyHelper.getUsernameFromHeader());
		Product product = getProductById(productId);
		productRepository.delete(product);
	}
	
	private HttpStatusAndMessage validateOnCreate(Product product) {
		HttpStatusAndMessage v = validateCategoryAndProductName(product);
		if (Objects.nonNull(v)) return v;
		v = validatePurchaseDateAndWarrantyExpireDate(product);
		return v;
	}
	
	private HttpStatusAndMessage validateOnUpdate(Long productId, Product product) {
		if (!Objects.equals(productId, product.getId())) {
			return new HttpStatusAndMessage(HttpStatus.UNPROCESSABLE_ENTITY, 
					"product id in request body and url do not match");
		}
		HttpStatusAndMessage v = validateCategoryAndProductName(product);
		if (Objects.nonNull(v)) return v;
		v = validatePurchaseDateAndWarrantyExpireDate(product);
		return v;
	}
	
	private String getAssetNumber(Product product) {
		try {
			return product.getCategoryName().substring(0, 1) + product.getProductName().substring(0, 1)
					+ String.valueOf(product.getId());
		} catch (Exception e) {
			return String.valueOf(product.getId());
		}
	}
	
	private Product getProductById(Long productId) throws ResponseStatusException {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
		if (apiKeyHelper.isAdmin()) {
			return product;
		} else if (Objects.isNull(apiKeyHelper.getUsernameFromHeader())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invalid or, no apiKey provided in header");
		} else if (Objects.nonNull(apiKeyHelper.getUsernameFromHeader()) 
				&& !Objects.equals(apiKeyHelper.getUsernameFromHeader(), product.getCreatedBy())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "access restricted");
		}
		return product;
	}

	private HttpStatusAndMessage validateCategoryAndProductName(Product product) {
		Map<String, List<String>> categoryToProductNames = getCategoryNameWiseProductNames();
		if (!categoryToProductNames.getOrDefault(product.getCategoryName(), new ArrayList<>()).contains(product.getProductName())) {
			return new HttpStatusAndMessage(
					HttpStatus.UNPROCESSABLE_ENTITY, "invalid categoryName, productName combination");
		}
		return null;
	}
	
	private HttpStatusAndMessage validatePurchaseDateAndWarrantyExpireDate(Product product) {
		if (Objects.isNull(product.getWarrantyInYears()) || Objects.isNull(product.getWarrantyExpireDate())) {
			return null;
		}
		Long yearsBetween = getNumberOfYearsBetween(product.getPurchaseDate(), product.getWarrantyExpireDate());
		if (yearsBetween != product.getWarrantyInYears()) {
			return new HttpStatusAndMessage(HttpStatus.UNPROCESSABLE_ENTITY, "invalid warrantyExpireDate");
		}
		return null;
	}
	
	private Long getNumberOfYearsBetween(Date startDate, Date endDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate d1 = LocalDate.parse(dateFormat.format(startDate));
		LocalDate d2 = LocalDate.parse(dateFormat.format(endDate));
		return ChronoUnit.YEARS.between(d1, d2);
	}
	
	private Map<String, List<String>> getCategoryNameWiseProductNames() {
		Map<String, List<String>> categoryToProductNames = new HashMap<>();
		categoryToProductNames.put("Computers", Arrays.asList("Laptop", "Desktop", "Chromebook"));
		categoryToProductNames.put("Smartphones", Arrays.asList("iPhone", "Samsung Galaxy", "Google Pixel", "OnePlus"));
		categoryToProductNames.put("Audio", Arrays.asList("Headphones", "Bluetooth Speakers", "Soundbars", "Earphones"));
		return categoryToProductNames;
	}
}
