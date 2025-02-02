package com.practice.LBM.manager;

import com.practice.LBM.entity.Book;
import com.practice.LBM.entity.Fine;
import com.practice.LBM.entity.Order;
import com.practice.LBM.entity.User;
import com.practice.LBM.model.OrderDto;
import com.practice.LBM.repository.BookRepository;
import com.practice.LBM.repository.OrderRepository;
import com.practice.LBM.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;


@Service
public class OrderManagerImpl implements OrderManager{
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    OrderRepository orderRepository;
    @Override
    public void borrowBook(OrderDto orderReq) throws Exception {
        String title = orderReq.getTitle();
        String username = orderReq.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new Exception("No such user exits")
        );
        if(user.getOrder()!=null){
            throw new Exception("User is having a book already");
        }
        if(user.getFine().getAmount()>0){
            throw new Exception("User has to clear fine");
        }
        Book book = bookRepository.findByTitle(title).orElseThrow(
                ()-> new Exception("No such title")
        );
        if(book.getQty()<1){
            throw new Exception("Book is not available");
        }
        Integer qty = book.getQty();
        book.setQty(qty-1);

        Order order = Order.builder()
                .book(book)
                .user(user)
                .createdAt(Date.valueOf(LocalDate.now().minusDays(10)))
                .build();
        user.setOrder(order);
        userRepository.save(user);
        bookRepository.save(book);

    }

    @Override
    public void returnBook(OrderDto orderReq) throws Exception {
        String title = orderReq.getTitle();
        String username = orderReq.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new Exception("No such user exits")
        );

        if(user.getOrder()==null){
            throw new Exception("User doesn't have any order");
        }
        Book book=user.getOrder().getBook();
        Integer qty = book.getQty();
        book.setQty(qty+1);

        Order order = user.getOrder();
        if(!order.getBook().getTitle().equals(title)){
            throw new Exception("user is returning wrong name");
        }
        LocalDate borrowedDate = order.getCreatedAt().toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(borrowedDate, LocalDate.now());
        Integer fineAmount=0;
        if(daysBetween>7) {
            fineAmount = ((int) daysBetween - 7) * 5;
        }
        Fine fine= user.getFine();
        fine.setAmount(fineAmount);
        user.setFine(fine);
        order.setUser(null);
        order.setBook(null);
        user.setOrder(null);
        orderRepository.delete(order);

        userRepository.save(user);
        bookRepository.save(book);

    }
}
