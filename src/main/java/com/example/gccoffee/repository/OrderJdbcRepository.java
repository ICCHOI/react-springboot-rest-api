package com.example.gccoffee.repository;

import com.example.gccoffee.model.Order;
import com.example.gccoffee.model.OrderItem;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Order insert(Order order) {
        jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) " +
                        "VALUES(UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)",
                toOrderParamMap(order));
        order.getOrderItems()
                .forEach(item -> jdbcTemplate.update("INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at, updated_at) " +
                        "VALUES(UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity, :createdAt, :updatedAt)",
                toOrderItemParameterMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item)));
        return order;
    }

    private Map<String, Object> toOrderParamMap(Order order) {
        var parameterMap = new HashMap<String, Object>();
        parameterMap.put("orderId", order.getOrderId().toString().getBytes(StandardCharsets.UTF_8));
        parameterMap.put("email", order.getEmail().getAddress());
        parameterMap.put("address", order.getAddress());
        parameterMap.put("postcode", order.getPostcode());
        parameterMap.put("orderStatus", order.getOrderStatus().toString());
        parameterMap.put("createdAt", order.getCreatedAt());
        parameterMap.put("updatedAt", order.getUpdatedAt());
        return parameterMap;
    }

    private Map<String, Object> toOrderItemParameterMap(UUID orderId, LocalDateTime createdAt, LocalDateTime updatedAt, OrderItem item) {
        var parameterMap = new HashMap<String, Object>();
        parameterMap.put("orderId", orderId.toString().getBytes());
        parameterMap.put("productId", item.productId().toString().getBytes());
        parameterMap.put("category", item.category().toString());
        parameterMap.put("price", item.price());
        parameterMap.put("quantity", item.quantity());
        parameterMap.put("createdAt", createdAt);
        parameterMap.put("updatedAt", updatedAt);
        return parameterMap;
    }
}
