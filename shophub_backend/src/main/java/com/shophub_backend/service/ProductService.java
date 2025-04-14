package com.shophub_backend.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shophub_backend.model.Product;
import com.shophub_backend.request.ProductRequest;

public interface ProductService {

    // only for admin...
    public Product createProduct(ProductRequest req);
    public Product deleteProduct(long productId);
    public Product updateProduct(long productId,Product newProduct);
    public List<Product> getAllProduct();
    public List<Product> recentlyAddedProduct();


    // These all methods are for user and admin as well...
    public Product findProductById(long productId);
    public List<Product> findProductByCategory(String categoryName);
    public List<Product> searchProduct(String query);

    public Page<Product> filterProduct(String category,List<String> colors,List<String> size,int minPrice,int maxPrice,int minDiscount,String sort,String stock,int pageNumber,int pageSize);

}
