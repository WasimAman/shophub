package com.shophub_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.shophub_backend.model.Product;
import com.shophub_backend.service.ProductService;

@RestController
@RequestMapping("api/user/product/")
public class UserProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("filter")
    public ResponseEntity<Page<Product>> filterProduct(@RequestParam String category,@RequestParam List<String> colors,@RequestParam List<String> size,@RequestParam int minPrice,@RequestParam int maxPrice,@RequestParam int minDiscount,@RequestParam String sort,@RequestParam String stock,@RequestParam int pageNumber,@RequestParam int pageSize){

        Page<Product> filteredProduct = productService.filterProduct(category, colors, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(filteredProduct);
    }

    @GetMapping("id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable long productId){
        Product productById = productService.findProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productById);
    }

    @GetMapping("/category{categoryName}")
    public ResponseEntity<List<Product>> findProductByCategoryHandler(@PathVariable String categoryName){
        List<Product> categorisedProduct = productService.findProductByCategory(categoryName);
        return ResponseEntity.status(HttpStatus.OK).body(categorisedProduct);
    }


    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String query){
        List<Product> searchedProduct = productService.searchProduct(query);
        return ResponseEntity.status(HttpStatus.OK).body(searchedProduct);
    }
}
