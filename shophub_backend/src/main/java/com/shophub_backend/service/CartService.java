package com.shophub_backend.service;

import com.shophub_backend.model.Cart;
import com.shophub_backend.model.CartItems;
import com.shophub_backend.model.User;
import com.shophub_backend.request.CartItemRequest;

public interface CartService {
    public void createCart(User user);
    public Cart findUserCart(long userId);
    public CartItems addItemToCart(CartItemRequest req, long userId);
}
