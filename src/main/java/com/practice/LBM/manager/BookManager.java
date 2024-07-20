package com.practice.LBM.manager;

import com.practice.LBM.model.BookDto;

public interface BookManager {
    void addBook(BookDto book);
    BookDto getBooks(String title) throws Exception;
}
