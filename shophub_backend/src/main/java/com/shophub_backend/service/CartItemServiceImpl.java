package com.shophub_backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shophub_backend.exception.CartItemException;
import com.shophub_backend.exception.UserException;
import com.shophub_backend.model.CartItems;
import com.shophub_backend.repository.CartItemRepository;

@Service
public class CartItemServiceImpl implements CartItemService{
      
    @Autowired
    private CartItemRepository cartItemRepository;
    @Override
    public void removeItemFromCart(long cartItemId, long userId) {
        CartItems item = findCartItemById(cartItemId);
        if(item.getUserId() == userId){
            cartItemRepository.deleteById(cartItemId);
        }else{
            throw new UserException("You can't remove another user's item");
        }
    }

    @Override
    public CartItems updateCartItems(long userId, long cartItemId, CartItems newItem) {
        CartItems item = findCartItemById(cartItemId);
        if(item.getUserId() == userId){
            item.setQuantity(newItem.getQuantity());
            item.setDiscountedPrice(item.getQuantity() * item.getProduct().getDiscountedPrice());
            item.setTotalPrice(item.getQuantity() * item.getProduct().getPrice());

            return cartItemRepository.save(item);
        }else{
            throw new CartItemException("You can't update another user's cart items");
        }
    }

    @Override
    public CartItems findCartItemById(long cartItemId) {
        Optional<CartItems> opt = cartItemRepository.findById(cartItemId);
        if(opt.isPresent()){
            return opt.get();
        }else{
            throw new UserException("CartItem not found with this id "+cartItemId);
        }
    }
}
