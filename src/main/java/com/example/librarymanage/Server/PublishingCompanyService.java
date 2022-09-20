package com.example.librarymanage.Server;

import com.example.librarymanage.DTO.BookDTO;
import com.example.librarymanage.DTO.PublishingCompanyDTO;
import com.example.librarymanage.DTO.StaffDTO;
import com.example.librarymanage.Entity.Author;
import com.example.librarymanage.Entity.Book;
import com.example.librarymanage.Entity.BookAuthor;
import com.example.librarymanage.Entity.PublishingCompany;
import com.example.librarymanage.Repository.PublishingCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class PublishingCompanyService {
    @Autowired
    PublishingCompanyRepository publishingCompanyRepository;
    @Autowired
    @Lazy
    BookService bookService;
    @Autowired
    @Lazy
    BookAuthorService bookAuthorService;
    @Autowired
    @Lazy
    BorrowPayService borrowPayService;
    public List<PublishingCompany> findAll()
    {
        return publishingCompanyRepository.findAll();
    }
    public PublishingCompany findPublishingCompanyByID(String publishingCompanyID)
    {
        return publishingCompanyRepository.findByPublishingcompanyid(publishingCompanyID);
    }
    public PublishingCompanyDTO findAll(Integer page, Integer itemPerPage)
    {
        //System.out.println(page + itemPerPage);
        if (page != null && itemPerPage != null)
        {
            System.out.println(page + itemPerPage);
            PublishingCompanyDTO publishingCompanyDTO =  new PublishingCompanyDTO();
            //System.out.println(Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            publishingCompanyDTO.setMaxPage((int)Math.ceil((double)publishingCompanyRepository.findAll().size() / itemPerPage));
            publishingCompanyDTO.setPublishingCompanyList(publishingCompanyRepository.findAll(PageRequest.of(page - 1, itemPerPage, Sort.by("publishingcompanyid").ascending())).getContent());
            System.out.println(publishingCompanyDTO.getMaxPage());
            return publishingCompanyDTO;
        }
        else
        {
            PublishingCompanyDTO publishingCompanyDTO =  new PublishingCompanyDTO();
            List<PublishingCompany> publishingCompanyList = publishingCompanyRepository.findAll();
            publishingCompanyDTO.setMaxPage(publishingCompanyList.size());
            publishingCompanyDTO.setPublishingCompanyList(publishingCompanyList);
            return publishingCompanyDTO;
        }

    }
    public ResponseEntity<PublishingCompanyDTO> update(PublishingCompany publishingCompany, String publishingCompanyIDOld)
    {
        if (!Objects.equals(publishingCompany.getPublishingcompanyid(), publishingCompanyIDOld))
        {

            if (publishingCompanyRepository.findByPublishingcompanyid(publishingCompany.getPublishingcompanyid()) == null)
            {
                /*System.out.println("yes");*/
                List<Book> bookList = bookService.findAllByPublishingcompanyid(publishingCompanyIDOld);
                bookService.deleteAll(bookList);
                delete(publishingCompanyRepository.findByPublishingcompanyid(publishingCompanyIDOld));
                save(publishingCompany);
                for (Book book : bookList)
                {
                    book.setPublishingcompanyid(publishingCompany.getPublishingcompanyid());
                }
                bookService.saveAll(bookList);
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            else
            {
                System.out.println("yes1");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        else
        {
            save(publishingCompany);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }
    public ResponseEntity<PublishingCompanyDTO> addPublishingCompany(PublishingCompany publishingCompany)
    {
        if (publishingCompanyRepository.findByPublishingcompanyid(publishingCompany.getPublishingcompanyid()) == null)
        {

            save(publishingCompany);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        else
        {
            save(publishingCompany);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<PublishingCompany> deletePublishingCompany(String publishingCompanyID)
    {
        try
        {
            PublishingCompany publishingCompany = publishingCompanyRepository.findByPublishingcompanyid(publishingCompanyID);
            if (publishingCompany != null)
            {
                List<Book> bookList = bookService.findAllByPublishingcompanyid(publishingCompanyID);
                for (Book book : bookList)
                {
                    bookAuthorService.delete(bookAuthorService.findAllByBookid(book.getBookid()));
                    borrowPayService.delete(borrowPayService.findAllByBookid(book.getBookid()));
                }
                bookService.deleteAll(bookList);
                delete(publishingCompany);
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<PublishingCompanyDTO> findAllByPublishingcompanyid(String publishingCompanyID, Integer page, Integer itemPerPage)
    {
        try {
            PublishingCompanyDTO publishingCompanyDTO = new PublishingCompanyDTO();
            publishingCompanyDTO.setMaxPage(publishingCompanyRepository.findAllByPublishingcompanyidContainingIgnoreCase(publishingCompanyID, Pageable.unpaged()).size());
            publishingCompanyDTO.setPublishingCompanyList(publishingCompanyRepository.findAllByPublishingcompanyidContainingIgnoreCase(publishingCompanyID, PageRequest.of(page - 1, itemPerPage, Sort.by("publishingcompanyid").ascending())));
            System.out.println(publishingCompanyDTO);
            return new ResponseEntity<>(publishingCompanyDTO, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }
    public ResponseEntity<PublishingCompanyDTO> findAllByPublishingcompanyname(String publishingCompanyID, Integer page, Integer itemPerPage)
    {
        try {
            PublishingCompanyDTO publishingCompanyDTO = new PublishingCompanyDTO();
            publishingCompanyDTO.setMaxPage(publishingCompanyRepository.findAllByPublishingcompanynameContainingIgnoreCase(publishingCompanyID, Pageable.unpaged()).size());
            publishingCompanyDTO.setPublishingCompanyList(publishingCompanyRepository.findAllByPublishingcompanynameContainingIgnoreCase(publishingCompanyID, PageRequest.of(page - 1, itemPerPage, Sort.by("publishingcompanyid").ascending())));
            return new ResponseEntity<>(publishingCompanyDTO, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }
    public ResponseEntity<PublishingCompanyDTO> findAllByPublishingcompanyidAndPublishingcompanyname(String publishingCompanyID, String publishingCompanyName, Integer page, Integer itemPerPage)
    {
        try {
            PublishingCompanyDTO publishingCompanyDTO = new PublishingCompanyDTO();
            publishingCompanyDTO.setMaxPage(publishingCompanyRepository.findAllByPublishingcompanyidContainingIgnoreCaseAndPublishingcompanynameContainingIgnoreCase(publishingCompanyID, publishingCompanyName, Pageable.unpaged()).size());
            publishingCompanyDTO.setPublishingCompanyList(publishingCompanyRepository.findAllByPublishingcompanyidContainingIgnoreCaseAndPublishingcompanynameContainingIgnoreCase(publishingCompanyID, publishingCompanyName, PageRequest.of(page - 1, itemPerPage, Sort.by("publishingcompanyid").ascending())));
            return new ResponseEntity<>(publishingCompanyDTO, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }
    @Transactional
    public void delete(PublishingCompany publishingCompany)
    {
        publishingCompanyRepository.delete(publishingCompany);
    }
    @Transactional
    public void save(PublishingCompany publishingCompany)
    {
        publishingCompanyRepository.save(publishingCompany);
    }
}
