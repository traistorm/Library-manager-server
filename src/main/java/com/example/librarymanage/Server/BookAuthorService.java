package com.example.librarymanage.Server;

import com.example.librarymanage.Entity.Author;
import com.example.librarymanage.Entity.Book;
import com.example.librarymanage.Entity.BookAuthor;
import com.example.librarymanage.Entity.BookAuthorID;
import com.example.librarymanage.Repository.BookAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookAuthorService {
    @Autowired
    BookAuthorRepository bookAuthorRepository;
    public List<BookAuthor> findAll()
    {
        return bookAuthorRepository.findAll();
    }
    public void save(BookAuthor bookAuthor)
    {
        if (bookAuthorRepository.findByBookidContainingAndAuthoridContaining(bookAuthor.getBookid(), bookAuthor.getAuthorid()) == null)
        {
            bookAuthorRepository.save(bookAuthor);
        }

    }
    public void save(List<BookAuthor> bookAuthorList)
    {
        bookAuthorRepository.saveAll(bookAuthorList);
    }
    public void delete(String bookID, String authorID)
    {
        BookAuthor bookAuthor = new BookAuthor(bookID, authorID);
        //System.out.println(bookAuthor.getBookid());
        bookAuthorRepository.delete(bookAuthor);
    }
    public List<BookAuthor> findAllByBookidContaining(String bookid)
    {
        return bookAuthorRepository.findAllByBookidContaining(bookid);
    }
    public List<BookAuthor> findAllByBookid(String bookid)
    {
        return bookAuthorRepository.findAllByBookid(bookid);
    }
    public void delete(List<BookAuthor> bookAuthorList)
    {
        bookAuthorRepository.deleteAll(bookAuthorList);

    }
}
