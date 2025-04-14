package com.shophub_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shophub_backend.model.Orders;
import com.shophub_backend.response.ApiResponse;
import com.shophub_backend.service.OrderService;

@RestController
@RequestMapping("api/admin/order/")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("all")
    public ResponseEntity<List<Orders>> getAllOrder(){
        List<Orders> orders = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(orders);
    }

    @PutMapping("{orderId}/confirmed")
    public  ResponseEntity<Orders> confirmedOrderHandler(@PathVariable long orderId){
        Orders confirmdOrder = orderService.confirmOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(confirmdOrder);
    }

    @PutMapping("{orderId}/shipped")
    public  ResponseEntity<Orders> shippedOrderHandler(@PathVariable long orderId){
        Orders shippedOrder = orderService.shippedOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(shippedOrder);
    }

    @PutMapping("{orderId}/delivered")
    public  ResponseEntity<Orders> deliveredOrderHandler(@PathVariable long orderId){
        Orders deliverdOrder = orderService.deliverdOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(deliverdOrder);
    }

    @PutMapping("{orderId}/canceled")
    public  ResponseEntity<Orders> canceldOrderHandler(@PathVariable long orderId){
        Orders cancledOrder = orderService.cancleOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(cancledOrder);
    }

    @DeleteMapping("{orderId}/delete")
    public  ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order deleted successfully...",true));
    }
}
