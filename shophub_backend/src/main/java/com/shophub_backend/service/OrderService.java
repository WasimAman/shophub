package com.shophub_backend.service;

import java.util.List;

import com.shophub_backend.model.Address;
import com.shophub_backend.model.Orders;
import com.shophub_backend.model.User;

public interface OrderService {

    public Orders order(User user,Address shippingAddress,long productId,String size);
    public Orders orderFromCart(User user,Address shippingAddress,long cartItemId);
    public Orders orderAllCartItems(User user,Address shippingAddress);
    public Orders findOrderById(long orderId);
    public List<Orders> userOrderHistory(long userId);

    // For admin only...
    public Orders placeOrder(long orderId);
    public Orders confirmOrder(long orderId);
    public Orders shippedOrder(long orderId);
    public Orders deliverdOrder(long orderId);
    public Orders cancleOrder(long orderId);
    public void deleteOrder(long orderId);
    public List<Orders> getAllOrders();
}
