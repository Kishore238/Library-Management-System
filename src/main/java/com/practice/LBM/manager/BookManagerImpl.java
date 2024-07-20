package com.practice.LBM.manager;

import com.practice.LBM.entity.Book;
import com.practice.LBM.model.BookDto;
import com.practice.LBM.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookManagerImpl implements BookManager {
    @Autowired
    private BookRepository bookRepository;
    @Override
    public void addBook(BookDto bookReq) {
       Book book = Book.builder().author(bookReq.getAuthor())
               .qty(bookReq.getQty())
               .title(bookReq.getTitle())
               .build();
       bookRepository.save(book);
    }

    @Override
    public BookDto getBooks(String title) throws Exception {
        Book book = bookRepository.findByTitle(title).orElseThrow(
                ()-> new Exception("No such title is present")
        );
        return BookDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .qty(book.getQty())
                .build();
    }
}
