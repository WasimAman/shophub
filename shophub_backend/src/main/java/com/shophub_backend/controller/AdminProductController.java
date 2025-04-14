package com.shophub_backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shophub_backend.model.Product;
import com.shophub_backend.request.ProductRequest;
import com.shophub_backend.security.jwt.JwtConstant;
import com.shophub_backend.service.ProductService;
import com.shophub_backend.service.UserService;

import io.jsonwebtoken.JwtException;

@RestController
@RequestMapping("api/admin/products/")
public class AdminProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @PostMapping("create")
    public ResponseEntity<Product> createProduct(@RequestHeader(JwtConstant.JWT_HEADER) String token,@RequestBody ProductRequest req){
        if(userService.findUserByToken(token) == null){
            throw new JwtException("Invalid Token...");
        }
        Product product = productService.createProduct(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PostMapping("creates")
    public ResponseEntity<List<Product>> createMultipleProduct(@RequestBody List<ProductRequest> reqs,@RequestHeader(JwtConstant.JWT_HEADER) String token){
        if(userService.findUserByToken(token) == null){
            throw new JwtException("Invalid Token...");
        }
        List<Product> listOfProducts = new ArrayList<>();
        for (ProductRequest req : reqs) {
            Product createdProduct = productService.createProduct(req);
            listOfProducts.add(createdProduct);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(listOfProducts);
    }

    @DeleteMapping("{productId}/delete")
    public ResponseEntity<Product> deleteProduct(@PathVariable long productId,@RequestHeader(JwtConstant.JWT_HEADER) String token){
        if(userService.findUserByToken(token) == null){
            throw new JwtException("Invalid Token...");
        }
        Product product = productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PutMapping("{productId}/update")
    public ResponseEntity<Product> updateProduct(@PathVariable long productId,@RequestBody Product product,@RequestHeader(JwtConstant.JWT_HEADER) String token){
        if(userService.findUserByToken(token) == null){
            throw new JwtException("Invalid Token...");
        }
        Product updatedProduct = productService.updateProduct(productId, product);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @GetMapping("all")
    public ResponseEntity<List<Product>> getAllProduct(@RequestHeader(JwtConstant.JWT_HEADER) String token){
        if(userService.findUserByToken(token) == null){
            throw new JwtException("Invalid Token...");
        }
        List<Product> allProducts = productService.getAllProduct();
        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
    }

    @GetMapping("recent")
    public ResponseEntity<List<Product>> recentlyAddedProductHandler(){
        List<Product> recentlyAddedProduct = productService.recentlyAddedProduct();
        return ResponseEntity.status(HttpStatus.OK).body(recentlyAddedProduct);
    }
}
