package com.shophub_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shophub_backend.model.CartItems;
import com.shophub_backend.model.User;
import com.shophub_backend.response.ApiResponse;
import com.shophub_backend.security.jwt.JwtConstant;
import com.shophub_backend.service.CartItemService;
import com.shophub_backend.service.UserService;

@RestController
@RequestMapping("api/user/cart_item/")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserService userService;


    @DeleteMapping("{cartItemId}")
    public ResponseEntity<ApiResponse> removeCartItem(@PathVariable long cartItemId,@RequestHeader(JwtConstant.JWT_HEADER) String token){
        User user = userService.findUserByToken(token);
        cartItemService.removeItemFromCart(cartItemId,user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Item removed from cart...",true));
    }

    @PutMapping("{cartItemId}")
    public ResponseEntity<CartItems> updateCartItem(@PathVariable long cartItemId,@RequestHeader(JwtConstant.JWT_HEADER) String token,CartItems item){
        User user = userService.findUserByToken(token);
        CartItems updatedCartItems = cartItemService.updateCartItems(user.getId(), cartItemId, item);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCartItems);
    }
}
