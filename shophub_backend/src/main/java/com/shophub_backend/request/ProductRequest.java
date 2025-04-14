package com.shophub_backend.request;

import java.util.HashSet;

import com.shophub_backend.model.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String title;
    private String description;
    private String brand;
    private String color;
    private int price;
    private int discountedPrice;
    private int discountPercent;
    private int quantity;
    private String imgUrl;
    private HashSet<Size> sizes;
    private String topLevelCategory;
    private String secondLevelcategory;
    private String thirdLevelCategory;
}
