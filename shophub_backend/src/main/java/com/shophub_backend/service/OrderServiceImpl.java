package com.shophub_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shophub_backend.enums.OrderStatus;
import com.shophub_backend.enums.PaymentMethod;
import com.shophub_backend.enums.PaymentStatus;
import com.shophub_backend.exception.OrderException;
import com.shophub_backend.exception.ProductException;
import com.shophub_backend.exception.UserException;
import com.shophub_backend.model.Address;
import com.shophub_backend.model.Cart;
import com.shophub_backend.model.CartItems;
import com.shophub_backend.model.OrderItems;
import com.shophub_backend.model.Orders;
import com.shophub_backend.model.Product;
import com.shophub_backend.model.User;
import com.shophub_backend.repository.AddressRepository;
import com.shophub_backend.repository.CartItemRepository;
import com.shophub_backend.repository.OrderItemRepository;
import com.shophub_backend.repository.OrderRepository;
import com.shophub_backend.repository.ProductRepository;
import com.shophub_backend.repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Orders order(User user, Address shippingAddress, long productId, String size) {
        Cart cart = cartService.findUserCart(user.getId());
        Optional<Product> opt = productRepository.findById(productId);
        if (opt.isPresent()) {
            Product product = opt.get();
            CartItems isExist = cartItemRepository.isCartItemExist(cart, product, size, user.getId());
            OrderItems item = null;
            if (isExist != null) {
                item = createOrderItem(isExist, size);
                cartItemService.removeItemFromCart(isExist.getId(), user.getId());
            } else {
                item = new OrderItems();
                item.setProduct(product);
                item.setSize(size);
                item.setTotalPrice(product.getQuantity() * product.getPrice());
                item.setDiscountedPrice(product.getQuantity() * product.getDiscountedPrice());
                item.setQuantity(product.getQuantity());
                item.setDiscount(product.getPrice() - product.getDiscountedPrice());
                item.setUserId(user.getId());
            }
            Orders order = createOrder(user, shippingAddress, List.of(item));

            item.setOrder(order);
            shippingAddress.setUser(user);
            user.getOrders().add(order);
            user.getAddresses().add(shippingAddress);
            userRepository.save(user);
            addressRepository.save(shippingAddress);
            orderItemRepository.save(item);
            return orderRepository.save(order);
        } else {
            throw new ProductException("product not found");
        }
    }

    @Override
    public Orders orderFromCart(User user, Address shippingAddress, long cartItemId) {
        CartItems item = cartItemRepository.findById(cartItemId).get();

        OrderItems orderItem = createOrderItem(item, item.getSize());
        Orders order = createOrder(user, shippingAddress, List.of(orderItem));

        orderItem.setOrder(order);
        user.getOrders().add(order);
        user.getAddresses().add(shippingAddress);
        shippingAddress.setUser(user);
        addressRepository.save(shippingAddress);
        userRepository.save(user);
        orderItemRepository.save(orderItem);
        return orderRepository.save(order);
    }

    @Override
    public Orders orderAllCartItems(User user, Address shippingAddress) {
        Cart cart = cartService.findUserCart(user.getId());
        List<OrderItems> orderItems = new ArrayList<>();

        for (CartItems cartItem : cart.getCartItems()) {
            OrderItems item = createOrderItem(cartItem, cartItem.getSize());
            orderItems.add(item);
        }

        Orders order = createOrder(user, shippingAddress, orderItems);

        user.getOrders().add(order);
        user.getAddresses().add(shippingAddress);
        shippingAddress.setUser(user);
        addressRepository.save(shippingAddress);
        userRepository.save(user);
        for (OrderItems item : orderItems) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }
        return order;
    }

    // This is non override method
    private Orders createOrder(User user, Address shippingAddress, List<OrderItems> orderItems) {
        Orders order = new Orders();
        for (OrderItems item : orderItems) {
            order.setUser(user);
            order.setDiscount(item.getDiscount());
            order.setDiscountedPrice((int) item.getDiscountedPrice());
            order.setItems(List.of(item));
            order.setOrderDateTime(LocalDateTime.now());
            order.setOrderId("shophub " + UUID.randomUUID().toString());
            order.setOrderStatus(OrderStatus.PENDING);
            order.getPaymentDetails().setPaymentStatus(PaymentStatus.PENDING);
            order.setShippingAddress(shippingAddress);
            order.setTotalItem(item.getQuantity());
            order.setTotalPrice(item.getTotalPrice());
        }
        return order;
    }

    // This is non override method
    private OrderItems createOrderItem(CartItems cartItem, String size) {
        OrderItems item = new OrderItems();
        item.setProduct(cartItem.getProduct());
        item.setSize(size);
        item.setTotalPrice(cartItem.getQuantity() * cartItem.getTotalPrice());
        item.setDiscountedPrice(cartItem.getQuantity() * cartItem.getDiscountedPrice());
        item.setQuantity(cartItem.getQuantity());
        item.setDiscount(cartItem.getTotalPrice() - cartItem.getDiscountedPrice());
        item.setUserId(cartItem.getUserId());
        return item;
    }

    @Override
    public Orders findOrderById(long orderId) {
        Optional<Orders> opt = orderRepository.findById(orderId);
        if (opt.isPresent()) {
            Orders order = opt.get();
            return order;
        } else {
            throw new OrderException("Order does not exist with this orderId " + orderId);
        }
    }

    @Override
    public List<Orders> userOrderHistory(long userId) {
        Optional<User> opt = userRepository.findById(userId);
        if (opt.isPresent()) {
            User user = opt.get();
            return user.getOrders();
        } else {
            throw new UserException("Invalid userId " + userId);
        }
    }

    @Override
    public Orders placeOrder(long orderId) {
        Optional<Orders> opt = orderRepository.findById(orderId);
        if (opt.isPresent()) {
            Orders order = opt.get();
            if (order.getPaymentDetails().getPaymentMethod().equals(PaymentMethod.COD)) {
                order.getPaymentDetails().setPaymentStatus(PaymentStatus.PENDING);
            } else {
                order.getPaymentDetails().setPaymentStatus(PaymentStatus.COMPLETED);
            }
            order.setOrderStatus(OrderStatus.PLACED);
            return orderRepository.save(order);
        } else {
            throw new OrderException("Order does not exist with this orderId " + orderId);
        }
    }

    @Override
    public Orders confirmOrder(long orderId) {
        Optional<Orders> opt = orderRepository.findById(orderId);
        if (opt.isPresent()) {
            Orders order = opt.get();
            order.setOrderStatus(OrderStatus.CONFIRMED);
            return orderRepository.save(order);
        } else {
            throw new OrderException("Order does not exist with this orderId " + orderId);
        }
    }

    @Override
    public Orders shippedOrder(long orderId) {
        Optional<Orders> opt = orderRepository.findById(orderId);
        if (opt.isPresent()) {
            Orders order = opt.get();
            order.setOrderStatus(OrderStatus.SHIPPED);
            return orderRepository.save(order);
        } else {
            throw new OrderException("Order does not exist with this orderId " + orderId);
        }
    }

    @Override
    public Orders deliverdOrder(long orderId) {
        Optional<Orders> opt = orderRepository.findById(orderId);
        if (opt.isPresent()) {
            Orders order = opt.get();
            if (order.getPaymentDetails().getPaymentMethod().equals(PaymentMethod.COD)) {
                order.getPaymentDetails().setPaymentStatus(PaymentStatus.COMPLETED);
            }
            order.setOrderStatus(OrderStatus.DELIVERED);
            return orderRepository.save(order);
        } else {
            throw new OrderException("Order does not exist with this orderId " + orderId);
        }
    }

    @Override
    public Orders cancleOrder(long orderId) {
        Optional<Orders> opt = orderRepository.findById(orderId);
        if (opt.isPresent()) {
            Orders order = opt.get();
            order.setOrderStatus(OrderStatus.CANCELLED);
            return orderRepository.save(order);
        } else {
            throw new OrderException("Order does not exist with this orderId " + orderId);
        }
    }

    @Override
    public void deleteOrder(long orderId) {
        Optional<Orders> opt = orderRepository.findById(orderId);
        if (opt.isPresent()) {
            Orders order = opt.get();
            orderRepository.delete(order);
        } else {
            throw new OrderException("Order does not exist with this orderId " + orderId);
        }
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateTimeDesc();
    }

}
