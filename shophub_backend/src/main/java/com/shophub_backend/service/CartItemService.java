package com.shophub_backend.service;

import com.shophub_backend.model.CartItems;

public interface CartItemService {
    public void removeItemFromCart(long cartItemId, long userId);
    public CartItems updateCartItems(long userId, long cartItemId,int quantity);
    public CartItems findCartItemById(long cartItemId);
}
