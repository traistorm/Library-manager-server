package com.example.librarymanage.Server;

import com.example.librarymanage.DTO.BookAuthorDTO;
import com.example.librarymanage.DTO.BookDTO;
import com.example.librarymanage.Entity.Author;
import com.example.librarymanage.Entity.Book;
import com.example.librarymanage.Entity.BookAuthor;
import com.example.librarymanage.Entity.BookAuthorID;
import com.example.librarymanage.Repository.BookAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setBookid(bookID);
        bookAuthor.setAuthorid(authorID);
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
    public List<BookAuthor> findAllByAuthorid(String authorID)
    {
        return bookAuthorRepository.findAllByAuthorid(authorID);
    }
    @Transactional
    public void delete(List<BookAuthor> bookAuthorList)
    {
        bookAuthorRepository.deleteAll(bookAuthorList);

    }
    public BookAuthorDTO findAll(Integer page, Integer itemPerPage)
    {
        //System.out.println(page + itemPerPage);
        if (page != null && itemPerPage != null)
        {
            System.out.println(page + itemPerPage);
            BookAuthorDTO bookAuthorDTO =  new BookAuthorDTO();
            //System.out.println(Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            bookAuthorDTO.setMaxPage((int)Math.ceil((double)bookAuthorRepository.findAll().size() / itemPerPage));
            bookAuthorDTO.setBookAuthorList(bookAuthorRepository.findAll(PageRequest.of(page - 1, itemPerPage, Sort.by("booktitle").ascending())).getContent());
            System.out.println(bookAuthorDTO.getMaxPage());
            return bookAuthorDTO;
        }
        else
        {
            BookAuthorDTO bookAuthorDTO =  new BookAuthorDTO();
            List<BookAuthor> bookAuthorList = bookAuthorRepository.findAll();
            bookAuthorDTO.setMaxPage(bookAuthorList.size());
            bookAuthorDTO.setBookAuthorList(bookAuthorList);
            return bookAuthorDTO;
        }

    }
}
