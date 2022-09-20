package com.example.librarymanage.Server;

import com.example.librarymanage.DTO.BookDTO;
import com.example.librarymanage.Entity.Book;
import com.example.librarymanage.Entity.BookAuthor;
import com.example.librarymanage.Entity.BorrowPay;
import com.example.librarymanage.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Objects;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    @Lazy
    BorrowPayService borrowPayService;
    @Autowired
    @Lazy
    BookAuthorService bookAuthorService;
    public BookDTO findAll(Integer page, Integer itemPerPage)
    {
        //System.out.println(page + itemPerPage);
        if (page != null && itemPerPage != null)
        {
            System.out.println(page + itemPerPage);
            BookDTO bookDTO =  new BookDTO();
            //System.out.println(Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            bookDTO.setMaxPage((int)Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            bookDTO.setBookList(bookRepository.findAll(PageRequest.of(page - 1, itemPerPage, Sort.by("booktitle").ascending())).getContent());
            System.out.println(bookDTO.getMaxPage());
            return bookDTO;
        }
        else
        {
            BookDTO bookDTO =  new BookDTO();
            List<Book> bookList = bookRepository.findAll();
            bookDTO.setMaxPage(bookList.size());
            bookDTO.setBookList(bookList);
            return bookDTO;
        }

    }

    public Book findByBookid(String bookID)
    {
        return bookRepository.findByBookid(bookID);
    }
    public BookDTO findAllByBookid(String bookID, Integer page, Integer itemPerPage)
    {
        if (page != null && itemPerPage != null)
        {
            //System.out.println(page + itemPerPage);
            BookDTO bookDTO =  new BookDTO();
            List<Book> bookList = bookRepository.findAllByBookidContaining(bookID);
            //System.out.println(Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            bookDTO.setMaxPage((int)Math.ceil((double)bookList.size() / itemPerPage));
            if ((page - 1) * itemPerPage + itemPerPage > bookList.size())
            {
                bookList = bookList.subList((page - 1) * itemPerPage, bookList.size());
            }
            else
            {
                bookList = bookList.subList((page - 1) * itemPerPage, (page - 1) * itemPerPage + itemPerPage);
            }
            bookDTO.setBookList(bookList);
            //System.out.println(bookDTO.getMaxPage());
            return bookDTO;
        }
        else
        {

            return null;
        }
    }
    public BookDTO findAllByBookidAndBooktitle(String bookID, String bookTitle, Integer page, Integer itemPerPage)
    {
        if (page != null && itemPerPage != null)
        {
            //System.out.println(page + itemPerPage);
            BookDTO bookDTO =  new BookDTO();
            List<Book> bookList = bookRepository.findAllByBookidAndBooktitleContaining(bookID, bookTitle);
            //System.out.println(Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            bookDTO.setMaxPage((int)Math.ceil((double)bookList.size() / itemPerPage));
            if ((page - 1) * itemPerPage + itemPerPage > bookList.size())
            {
                bookList = bookList.subList((page - 1) * itemPerPage, bookList.size());
            }
            else
            {
                bookList = bookList.subList((page - 1) * itemPerPage, (page - 1) * itemPerPage + itemPerPage);
            }
            bookDTO.setBookList(bookList);
            //System.out.println(bookDTO.getMaxPage());
            return bookDTO;
        }
        else
        {

            return null;
        }
        //return bookRepository.findAllByBookidAndBooktitleContaining(bookID, bookTitle);
    }
    public BookDTO findAllBooktitle(String bookTitle, Integer page, Integer itemPerPage)
    {
        if (page != null && itemPerPage != null)
        {
            BookDTO bookDTO =  new BookDTO();
            List<Book> bookList = bookRepository.findAllByBooktitleContaining(bookTitle);
            bookDTO.setMaxPage((int)Math.ceil((double)bookList.size() / itemPerPage));
            //System.out.println(bookList.size());
            if ((page - 1) * itemPerPage + itemPerPage > bookList.size())
            {
                bookList = bookList.subList((page - 1) * itemPerPage, bookList.size());
            }
            else
            {
                bookList = bookList.subList((page - 1) * itemPerPage, (page - 1) * itemPerPage + itemPerPage);
            }


            bookDTO.setBookList(bookList);
            System.out.println(bookList.size());
            return bookDTO;
        }
        else
        {
            return null;
        }
        //return bookRepository.findAllByBooktitleContaining(bookTitle);
    }
    public boolean add(Book book)
    {
        try
        {
            if (bookRepository.findByBookid(book.getBookid()) != null)
            {
                return false;
            }
            bookRepository.save(book);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }
    @Transactional
    public void save(Book book)
    {
        bookRepository.save(book);
    }
    @Transactional
    public void saveAll(List<Book> bookList)
    {
        bookRepository.saveAll(bookList);
    }
    @Transactional
    public void deleteAll(List<Book> bookList)
    {
        bookRepository.deleteAll(bookList);
    }
    public void delete(Book book)
    {
        bookAuthorService.delete(bookAuthorService.findAllByBookid(book.getBookid()));
        borrowPayService.delete(borrowPayService.findAllByBookid(book.getBookid()));
        bookRepository.delete(book);
    }
    public List<Book> getTopViewList(Integer number)
    {
        List<Book> bookList = bookRepository.findAllByOrderByViewDesc();
        if (bookList.size() <= number)
        {
            return bookList;
        }
        else
        {
            return bookList.subList(0, number);
        }
    }
    public boolean save(Book book, String bookIDOld)
    {
        try {
            if (!Objects.equals(book.getBookid(), bookIDOld))
            {
                if (bookRepository.findByBookid(book.getBookid()) != null)
                {
                    return false; // Bookid is exist
                }
                else
                {
                    // Delete borrow-pay with bookid old
                    List<BorrowPay> borrowPayList = borrowPayService.findAllByBookid(bookIDOld);
                    borrowPayService.delete(borrowPayList);
                    // Delete bookauthor with bookid old
                    List<BookAuthor> bookAuthorList = bookAuthorService.findAllByBookid(bookIDOld);
                    bookAuthorService.delete(bookAuthorList);
                    // Delete book with bookid old
                    bookRepository.delete(bookRepository.findByBookid(bookIDOld));

                    // Save book with bookid new
                    bookRepository.save(book);
                    // Save borrow pay with book id new
                    for (BorrowPay borrowPay : borrowPayList)
                    {
                        borrowPay.setBookid(book.getBookid());
                    }
                    borrowPayService.save(borrowPayList);
                    // Save book author with book id new
                    for (BookAuthor bookAuthor : bookAuthorList)
                    {
                        bookAuthor.setBookid(book.getBookid());
                    }
                    bookAuthorService.save(bookAuthorList);
                    return true;
                }
            }
            else
            {
                bookRepository.save(book);
                List<BookAuthor> bookAuthorList = bookAuthorService.findAllByBookid(bookIDOld);
                bookAuthorService.delete(bookAuthorList);
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    public List<Book> findAllByPublishingcompanyid(String publishingCompanyID)
    {
        return bookRepository.findAllByPublishingcompanyid(publishingCompanyID);
    }
}
