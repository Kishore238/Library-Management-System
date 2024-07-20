package com.practice.LBM.controller;

import com.practice.LBM.manager.BookManager;
import com.practice.LBM.manager.FineManager;
import com.practice.LBM.manager.OrderManager;
import com.practice.LBM.manager.UserManager;
import com.practice.LBM.model.BookDto;
import com.practice.LBM.model.FineDto;
import com.practice.LBM.model.OrderDto;
import com.practice.LBM.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class Api {
    @Autowired
    BookManager bookManager;
    @Autowired
    UserManager userManager;
    @Autowired
    OrderManager orderManager;
    @Autowired
    FineManager fineManager;

    @PostMapping("/api/book")
    @PreAuthorize("hasAnyRole('LIB')")
    @Transactional
    ResponseEntity addBook(@RequestBody BookDto bookDto){
        bookManager.addBook(bookDto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/book")
    ResponseEntity getBook(@RequestParam("title") String title){
        try {
            BookDto bookDto=bookManager.getBooks(title);
            return ResponseEntity.ok(bookDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("api/user")
    @PreAuthorize("hasAnyRole('LIB')")
    ResponseEntity createUser(@RequestBody UserDto userDto){
        userManager.createUser(userDto.getUsername(),userDto.getPassword() , userDto.getRole());
        return ResponseEntity.ok().build();
    }

    @PostMapping("api/order")
    @PreAuthorize("hasAnyRole('LIB','MEM')")
    ResponseEntity createOrder(@RequestBody OrderDto orderDto){
        try {
            orderManager.borrowBook(orderDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/api/order/username/{username}/title/{title}")
    @PreAuthorize("hasAnyRole('LIB','MEM')")
    ResponseEntity returnOrder(@PathVariable("username") String username,
                               @PathVariable("title") String title){
        try{
            OrderDto orderDto=new OrderDto();
            orderDto.setTitle(title);
            orderDto.setUsername(username);
            orderManager.returnBook(orderDto);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/api/fine/user/{username}")
    @PreAuthorize("hasAnyRole('LIB','MEM')")
    ResponseEntity getFine(@PathVariable("username") String username){
        try {
            Integer amount=fineManager.getForUser(username);
            return ResponseEntity.ok(amount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/api/fine")
    @PreAuthorize("hasAnyRole('LIB','MEM')")
    ResponseEntity payFine(@RequestBody FineDto fineDto){
        try {
            fineManager.payForUser(fineDto.getUsername(),fineDto.getAmount());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
