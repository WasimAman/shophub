package com.shophub_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shophub_backend.model.Address;
import com.shophub_backend.model.Orders;
import com.shophub_backend.model.User;
import com.shophub_backend.security.jwt.JwtConstant;
import com.shophub_backend.service.OrderService;
import com.shophub_backend.service.UserService;

@RestController
@RequestMapping("api/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @PostMapping("{productId}/{size}")
    public ResponseEntity<Orders> orderHandler(@PathVariable long productId,@PathVariable String size,@RequestHeader(JwtConstant.JWT_HEADER) String token,@RequestBody Address shippingAddress){

        User user = userService.findUserByToken(token);
        Orders order = orderService.order(user, shippingAddress, productId, size);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @PostMapping("{cartItemId}")
    public ResponseEntity<Orders> orderFromCartHandler(@PathVariable long cartItemId,@RequestBody Address shippingAddress,@RequestHeader(JwtConstant.JWT_HEADER) String token){
        User user = userService.findUserByToken(token);
        Orders order = orderService.orderFromCart(user, shippingAddress, cartItemId);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @PostMapping("")
    public ResponseEntity<Orders> orderAllCartItemsHandler(@RequestBody Address shippingAddress,@RequestHeader(JwtConstant.JWT_HEADER) String token){
        User user = userService.findUserByToken(token);
        Orders order = orderService.orderAllCartItems(user, shippingAddress);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Orders>> userOrderHistoryHandler(@RequestHeader(JwtConstant.JWT_HEADER) String token){
        User user = userService.findUserByToken(token);
        List<Orders> userOrderHistory = orderService.userOrderHistory(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(userOrderHistory);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<Orders> findOrderHandler(@PathVariable long orderId){
        Orders order = orderService.findOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }
}
