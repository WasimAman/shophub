package com.shophub_backend.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shophub_backend.exception.CartItemException;
import com.shophub_backend.exception.UserException;
import com.shophub_backend.model.Cart;
import com.shophub_backend.model.CartItems;
import com.shophub_backend.repository.CartItemRepository;
import com.shophub_backend.repository.CartRepository;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public void removeItemFromCart(long cartItemId, long userId) {
        CartItems item = findCartItemById(cartItemId);
        Cart userCart = cartService.findUserCart(userId);
        if (item.getUserId() == userId) {
            userCart.setTotalItem(userCart.getTotalItem() - item.getQuantity());
            userCart.setDiscountedPrice(userCart.getDiscountedPrice() - item.getDiscountedPrice());
            userCart.setTotalPrice(userCart.getTotalPrice() - item.getTotalPrice());
            userCart.setDiscount(userCart.getTotalPrice() - userCart.getDiscountedPrice());
            cartItemRepository.deleteById(cartItemId);
        } else {
            throw new UserException("You can't remove another user's item");
        }
    }

    @Override
    public CartItems updateCartItems(long userId, long cartItemId,int quantity) {
        CartItems item = findCartItemById(cartItemId);

        if (!Objects.equals(item.getUserId(), userId)) {
            throw new CartItemException("You can't update another user's cart items");
        }

        // Update item quantity
        item.setQuantity(quantity);

        // Calculate prices (assuming int-based, replace with BigDecimal if needed)
        int productPrice = item.getProduct().getPrice(); // if it's int
        int discountedPrice = item.getProduct().getDiscountedPrice();

        item.setTotalPrice(quantity * productPrice);
        item.setDiscountedPrice(quantity * discountedPrice);

        // Recalculate cart totals
        Cart userCart = item.getCart();
        int totalPrice = 0;
        int totalDiscountedPrice = 0;

        for (CartItems cartItem : userCart.getCartItems()) {
            totalPrice += cartItem.getTotalPrice();
            totalDiscountedPrice += cartItem.getDiscountedPrice();
        }

        userCart.setTotalPrice(totalPrice);
        userCart.setDiscountedPrice(totalDiscountedPrice);
        userCart.setDiscount(totalPrice - totalDiscountedPrice);

        // Save changes
        cartRepository.save(userCart);
        return cartItemRepository.save(item);
    }

    @Override
    public CartItems findCartItemById(long cartItemId) {
        Optional<CartItems> opt = cartItemRepository.findById(cartItemId);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new UserException("CartItem not found with this id " + cartItemId);
        }
    }
}
