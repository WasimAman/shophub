package com.shophub_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shophub_backend.model.Cart;
import com.shophub_backend.model.CartItems;
import com.shophub_backend.model.Product;
import com.shophub_backend.model.User;
import com.shophub_backend.repository.CartItemRepository;
import com.shophub_backend.repository.CartRepository;
import com.shophub_backend.repository.ProductRepository;
import com.shophub_backend.request.CartItemRequest;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public void createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    @Override
    public Cart findUserCart(long userId) {
        return cartRepository.findUserCart(userId).get();
    }   
    
    @Override
    public CartItems addItemToCart(CartItemRequest req,long userId) {
        Product product = productRepository.findById(req.getProductId()).get();
        Cart cart = cartRepository.findByUserId(userId);

        CartItems isPresent = cartItemRepository.isCartItemExist(cart, product, req.getSize(), userId);

        if(isPresent == null){
            CartItems items = new CartItems();
            items.setCart(cart);
            items.setProduct(product);
            items.setQuantity(req.getQuantity());
            items.setUserId(userId);

            items.setTotalPrice(req.getQuantity() * product.getPrice());
            items.setDiscountedPrice(req.getQuantity()*product.getDiscountedPrice());
            items.setSize(req.getSize());

            cart.getCartItems().add(items);
            cart.setTotalPrice(cart.getTotalPrice() + items.getTotalPrice());
            cart.setDiscountedPrice(cart.getDiscountedPrice() + items.getDiscountedPrice());
            cart.setDiscount(cart.getTotalPrice() - cart.getDiscountedPrice());
            cart.setTotalItem(cart.getTotalItem() + items.getQuantity());
            cartRepository.save(cart);
            return items;
        }
        return isPresent;
    }
}
