package org.example.clientapplication.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.transaction.Transactional;
import org.example.clientapplication.dtos.OrderDTO;
import org.example.clientapplication.dtos.OrderItemDTO;
import org.example.clientapplication.dtos.OrderSummaryDTO;
import org.example.clientapplication.entities.*;
import org.example.clientapplication.enums.OrderStatus;
import org.example.clientapplication.repositories.OrderItemsRepository;
import org.example.clientapplication.repositories.OrderRepository;
import org.example.clientapplication.repositories.ProductRepository;
import org.example.clientapplication.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserAccountRepository UserAccountRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LoyaltyInfoService loyaltyInfoService;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    public void createOrder(OrderDTO orderDTO) {
        UserAccount user = UserAccountRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        List<OrderItemDTO> orderItemsDTO = orderDTO.getItems();
        List<OrderItem> orderItems = orderItemsDTO.stream().map(this::mapToOrderItem).toList();
        double total = calculateTotal(orderItems);
        Order order = buildOrder(orderDTO, user, orderItems, total);
        applyLoyaltyDiscount(order, user);
        Order savedOrder = orderRepository.save(order);
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(savedOrder);
            orderItemsRepository.save(orderItem);
        }
    }

    private OrderItem mapToOrderItem(OrderItemDTO orderItemDTO) {
        Product product = productRepository.findByProductNumber(orderItemDTO.getProductNumber());
        if(!product.getActive().equals(true)) {
            throw new RuntimeException("Product is not active");
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemDTO.getQuantity());
        return orderItem;
    }

    private double calculateTotal(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(orderItem -> orderItem.getProduct().getPrice() * orderItem.getQuantity())
                .sum();
    }

    private Order buildOrder(OrderDTO orderDTO, UserAccount user, List<OrderItem> orderItems, double total) {
        Order order = new Order();
        order.setUser(user);
        order.setItems(orderItems);
        order.setOrderDate(orderDTO.getOrderDate());
        order.setTotal(total);
        order.setStatus(OrderStatus.CREATED);
        return order;
    }

    private void applyLoyaltyDiscount(Order order, UserAccount user) {
        LoyaltyInfo loyaltyInfo = loyaltyInfoService.getLoyaltyInfo(user.getId());
        LoyaltyLevel loyaltyLevel = loyaltyInfo.getLoyaltyLevel();
        double discountPercentage = loyaltyLevel.getDiscountPercentage();
        double discount = order.getTotal() * discountPercentage;
        double priceAfterDiscount = order.getTotal() - discount;
        order.setDiscountPercentage(discountPercentage);
        order.setDiscount(discount);
        order.setPriceAfterDiscount(priceAfterDiscount);
    }

    public void confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.CONFIRMED);

        double total = order.getPriceAfterDiscount();
        int pointEarned = (int) (total / 10);

        int currentPoints = loyaltyInfoService.getLoyaltyInfo(order.getUserId()).getLoyaltyPoints();
        int newPoints = currentPoints + pointEarned;
        loyaltyInfoService.updateLoyaltyPoints(order.getUserId(), newPoints);

        orderRepository.save(order);
    }

    public void cancelOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order order1 = order.get();
            if (order1.getStatus().equals(OrderStatus.CONFIRMED)) {
                throw new RuntimeException("Order is already confirmed");
            }
            orderRepository.deleteById(orderId);
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    public Product getProductByNumber(String productNumber) {
        return productRepository.findByProductNumber(UUID.fromString(productNumber));
    }

    public UserAccount getUserById(long userId) {
        return UserAccountRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order convertToOrder(PurchaseOrder purchaseOrder, OrderService orderService, UserAccount user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(purchaseOrder.getOrderDate(), formatter);

        List<OrderItem> orderItems = purchaseOrder.getItems().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            Product product = orderService.getProductByNumber(item.getProductNumber());
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            return orderItem;
        }).toList();

        double total = orderItems.stream()
                .mapToDouble(orderItem -> orderItem.getProduct().getPrice() * orderItem.getQuantity())
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setItems(orderItems);
        order.setOrderDate(date);
        order.setTotal(total);

        applyLoyaltyDiscount(order, user);

        orderRepository.save(order);

        confirmOrder(order.getId());

        return order;
    }

    public PurchaseOrder parsePurchaseOrder(MultipartFile file) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        return xmlMapper.readValue(file.getInputStream(), PurchaseOrder.class);
    }

    public void copyOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        Order newOrder = new Order();
        newOrder.setUser(order.getUser());
        List<OrderItem > orderItems = order.getItems().stream().map(item -> {
            Product product = item.getProduct();
            if(!product.getActive().equals(true)) {
                throw new RuntimeException("Product is not active");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            return orderItem;
        }).toList();
        newOrder.setItems(orderItems);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setTotal(calculateTotal(orderItems));
        applyLoyaltyDiscount(newOrder, order.getUser());
        orderRepository.save(newOrder);
    }

    public List<OrderSummaryDTO> getOrdersByUserStatusAndDateRange(Long userId, OrderStatus status, LocalDate startDate, LocalDate endDate) {
        UserAccount user = UserAccountRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders ;
        if(status != null && startDate != null &&endDate != null) {
            orders = orderRepository.findByUserAndStatusAndOrderDateBetween(user, status, startDate, endDate);
        } else if (status !=null) {
            orders = orderRepository.findByUserAndStatus(user, status);
        } else if (startDate != null && endDate != null) {
            orders = orderRepository.findByUserAndOrderDateBetween(user, startDate, endDate);
        } else {
            orders = orderRepository.findByUser(user);
        }
        return orders.stream()
                .map(this::mapToOrderSummaryDTO)
                .toList();
    }

    private OrderSummaryDTO mapToOrderSummaryDTO(Order order) {
        OrderSummaryDTO orderSummaryDTO = new OrderSummaryDTO();
        orderSummaryDTO.setOrderId(order.getId());
        orderSummaryDTO.setOrderDate(order.getOrderDate());
        orderSummaryDTO.setStatus(order.getStatus());
        orderSummaryDTO.setTotal(order.getTotal());
        return orderSummaryDTO;
    }
}
