package com.shophub_backend.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shophub_backend.exception.ProductException;
import com.shophub_backend.exception.ResourceNotFoundException;
import com.shophub_backend.model.Cart;
import com.shophub_backend.model.CartItems;
import com.shophub_backend.model.Category;
import com.shophub_backend.model.Product;
import com.shophub_backend.model.Size;
import com.shophub_backend.repository.CartItemRepository;
import com.shophub_backend.repository.CartRepository;
import com.shophub_backend.repository.CategoryRepository;
import com.shophub_backend.repository.ProductRepository;
import com.shophub_backend.request.ProductRequest;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public Product createProduct(ProductRequest req) {
        Category topLevel = categoryRepository.findByName(req.getTopLevelCategory());

        if (topLevel == null) {
            Category topLevelCategory = new Category();
            topLevelCategory.setName(req.getTopLevelCategory());
            ;
            topLevelCategory.setLevel(1);

            topLevel = categoryRepository.save(topLevelCategory);
        }

        Category secondLevel = categoryRepository.findByNameAndParent(req.getSecondLevelcategory(), topLevel.getName());

        if (secondLevel == null) {
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(req.getSecondLevelcategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);

            secondLevel = categoryRepository.save(secondLevelCategory);
        }

        Category thirdLevel = categoryRepository.findByNameAndParent(req.getThirdLevelCategory(),
                secondLevel.getName());

        if (thirdLevel == null) {
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(req.getThirdLevelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);

            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setDescription(req.getDescription());
        product.setBrand(req.getBrand());
        product.setColor(req.getColor());
        product.setPrice(req.getPrice());
        product.setDiscountedPrice(req.getDiscountedPrice());
        product.setDiscountPercent(req.getDiscountPercent());
        product.setQuantity(req.getQuantity());
        product.setImgUrl(req.getImgUrl());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        for(Size size : req.getSizes()){
            product.getSizes().add(size);
            size.setProduct(product);
            // sizeRepository.save(size);
        }

        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product deleteProduct(long productId) {
        List<CartItems> items = cartItemRepository.findAllByProductId(productId);
        for (CartItems item : items) {
            Cart cart = item.getCart();

            cart.setDiscountedPrice(cart.getDiscountedPrice() - item.getDiscountedPrice());
            cart.setTotalPrice(cart.getTotalPrice() - item.getTotalPrice());
            int discount = cart.getDiscount() - (item.getTotalPrice() - item.getDiscountedPrice());
            cart.setDiscount(discount);
            cart.setTotalItem(cart.getTotalItem()-item.getQuantity());
            cartRepository.save(cart);
            cartItemRepository.delete(item);
        }

        cartItemRepository.deleteByProductId(productId);
        Product product = productRepository.findById(productId).get();
        product.getSizes().clear();
        productRepository.delete(product);
        return product;
    }

    @Override
    public Product updateProduct(long productId, Product updatedProduct) {
        // Fetch existing product from DB
        Optional<Product> existingProductOpt = productRepository.findById(productId);

        if (!existingProductOpt.isPresent()) {
            throw new ResourceNotFoundException("Product with ID " + productId + " not found.");
        }

        Product existingProduct = existingProductOpt.get();

        // Update all fields that an admin can modify
        existingProduct.setTitle(updatedProduct.getTitle());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setBrand(updatedProduct.getBrand());
        existingProduct.setColor(updatedProduct.getColor());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setDiscountedPrice(updatedProduct.getDiscountedPrice());
        existingProduct.setDiscountPercent(updatedProduct.getDiscountPercent());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setImgUrl(updatedProduct.getImgUrl());
        // Update Sizes (Clear existing sizes and add new ones)
        existingProduct.getSizes().clear();
        existingProduct.getSizes().addAll(updatedProduct.getSizes());

        // // Recalculate average rating if reviews exist
        // if (!existingProduct.getRatings().isEmpty()) {
        // int totalRating =
        // existingProduct.getRatings().stream().mapToInt(Rating::getRating).sum();
        // int avgRating = totalRating / existingProduct.getRatings().size();
        // existingProduct.setAvgRating(avgRating);
        // }

        // Update cart items associated with this product
        List<CartItems> cartItemsList = cartItemRepository.findAllByProductId(productId);
        if (!cartItemsList.isEmpty()) {
            for (CartItems item : cartItemsList) {
                item.setDiscountedPrice(updatedProduct.getDiscountedPrice());
                item.setTotalPrice(updatedProduct.getPrice());
            }
            cartItemRepository.saveAll(cartItemsList);
        }

        // Save updated product
        return productRepository.save(existingProduct);
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductById(long productId) {
        Optional<Product> opt = productRepository.findById(productId);
        if(opt.isPresent()){
            return opt.get();
        }else{
            throw new ProductException("Product is not availble...");
        }
    }

    @Override
    public List<Product> findProductByCategory(String categoryName) {
        return productRepository.findProductByCategory(categoryName);
    }

    @Override
    public List<Product> searchProduct(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    @Override
    public Page<Product> filterProduct(
        String category, List<String> colors, List<String> size,
        int minPrice,int maxPrice, int minDiscount, String sort, 
        String stock, int pageNumber, int pageSize
    ) {
        // write code here...
        Pageable page = PageRequest.of(pageNumber, pageSize);
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount);
        if (sort != null) {
            switch (sort) {
                case "price_low":
                    products.sort(Comparator.comparing(Product::getDiscountedPrice));
                    break;
                case "price_high":
                    products.sort(Comparator.comparing(Product::getDiscountedPrice).reversed());
                    break;
            }
        }  
        if(!colors.isEmpty()){
            products = products.stream().filter(p->{
                return colors.stream().anyMatch(c->{
                    return c.equalsIgnoreCase(p.getColor());
                });
            }).collect(Collectors.toList());
        }  
        
        if(!size.isEmpty()){
            products = products.stream().filter(p->{
                return size.stream().anyMatch(s->{
                    return p.getSizes().stream().anyMatch(productSize->{
                        return productSize.getName().equalsIgnoreCase(s);
                    });
                });
            }).collect(Collectors.toList());
        }
        int startIdx = (int)page.getOffset();
        int endIdx = Math.min(startIdx+page.getPageSize(),products.size());

        List<Product> pageContent = products.subList(startIdx, endIdx);
        Page<Product> filterdProducts = new PageImpl<>(pageContent,page,products.size());
        return filterdProducts;
    }

    @Override
    public List<Product> recentlyAddedProduct() {
        return productRepository.findTop10ByOrderByCreatedAtDesc();
    }
}
