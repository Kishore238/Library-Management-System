package com.practice.LBM.manager;

import com.practice.LBM.model.OrderDto;

public interface OrderManager {
    void borrowBook(OrderDto order) throws Exception;
    void returnBook(OrderDto order) throws Exception;
}
