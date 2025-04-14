package com.shophub_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shophub_backend.model.Cart;
import com.shophub_backend.model.CartItems;
import com.shophub_backend.model.User;
import com.shophub_backend.request.CartItemRequest;
import com.shophub_backend.security.jwt.JwtConstant;
import com.shophub_backend.service.CartService;
import com.shophub_backend.service.UserService;

@RestController
@RequestMapping("api/user/cart/")
public class CartController {
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    @PostMapping("add")
    public ResponseEntity<CartItems> addItemToCart(@RequestBody CartItemRequest req,@RequestHeader(JwtConstant.JWT_HEADER) String token){
        User user = userService.findUserByToken(token);
        CartItems items = cartService.addItemToCart(req, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(items);
    }

    @GetMapping("")
    public ResponseEntity<Cart> findUsercart(@RequestHeader(JwtConstant.JWT_HEADER) String token){
        User user = userService.findUserByToken(token);
        Cart userCart = cartService.findUserCart(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(userCart);
    }
}
