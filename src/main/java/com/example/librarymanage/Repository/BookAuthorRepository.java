package com.example.librarymanage.Repository;

import com.example.librarymanage.Entity.Book;
import com.example.librarymanage.Entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Integer> {
    BookAuthor findByBookidContainingAndAuthoridContaining(String bookID, String authorID);
    List<BookAuthor> findAllByBookidContaining(String bookid);
    List<BookAuthor> findAllByBookid(String bookID);
    List<BookAuthor> findAllByAuthorid(String authorID);
}
