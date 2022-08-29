package com.example.librarymanage.Repository;

import com.example.librarymanage.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findByBookid(String id);
    List<Book> findAllByBookidAndBooktitleContaining(String bookID, String bookTitle);
    List<Book> findAllByBooktitleContaining(String bookTitle);
    List<Book> findAllByOrderByViewDesc();
    List<Book> findAllByBookidContaining(String bookID);



}
