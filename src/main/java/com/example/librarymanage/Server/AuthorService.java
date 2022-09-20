package com.example.librarymanage.Server;

import com.example.librarymanage.DTO.AuthorDTO;
import com.example.librarymanage.DTO.BookDTO;
import com.example.librarymanage.Entity.*;
import com.example.librarymanage.Repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    @Lazy
    BookAuthorService bookAuthorService;
    public Author findByAuthorid(String authorID)
    {
        return authorRepository.findByAuthorid(authorID);
    }
    public List<Author> findAll()
    {
        return authorRepository.findAll();
    }
    public void save(Author author)
    {
        authorRepository.save(author);
    }
    public AuthorDTO findAll(Integer page, Integer itemPerPage)
    {
        //System.out.println(page + itemPerPage);
        AuthorDTO authorDTO =  new AuthorDTO();
        if (page != null && itemPerPage != null)
        {
            System.out.println("get");
            //System.out.println(page + itemPerPage);
            //System.out.println(Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            authorDTO.setMaxPage((int)Math.ceil((double)authorRepository.findAll().size() / itemPerPage));
            authorDTO.setAuthorList(authorRepository.findAll(PageRequest.of(page - 1, itemPerPage, Sort.by("authorid").ascending())).getContent());
            System.out.println(authorDTO.getMaxPage());
        }
        else
        {
            //System.out.println("get");
            List<Author> authorList = authorRepository.findAll();
            authorDTO.setMaxPage(authorList.size());
            authorDTO.setAuthorList(authorList);
        }
        return authorDTO;

    }
    public AuthorDTO findAllByAuthorid(String authorid, Integer page, Integer itemPerPage)
    {
        //System.out.println(page + itemPerPage);
        AuthorDTO authorDTO =  new AuthorDTO();
        if (page != null && itemPerPage != null)
        {
            System.out.println("get");
            //System.out.println(page + itemPerPage);
            //System.out.println(Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            authorDTO.setMaxPage((int)Math.ceil((double)authorRepository.findAllByAuthoridContaining(authorid, PageRequest.of(page - 1, itemPerPage, Sort.by("authorid").ascending())).size() / itemPerPage));
            authorDTO.setAuthorList(authorRepository.findAllByAuthoridContaining(authorid, PageRequest.of(page - 1, itemPerPage, Sort.by("authorid").ascending())));
            System.out.println(authorDTO.getMaxPage());
        }
        else
        {
            //System.out.println("get");
            List<Author> authorList = authorRepository.findAll();
            authorDTO.setMaxPage(authorList.size());
            authorDTO.setAuthorList(authorList);
        }
        return authorDTO;

    }
    public AuthorDTO findAllByAuthorname(String authorid, Integer page, Integer itemPerPage)
    {
        //System.out.println(page + itemPerPage);
        AuthorDTO authorDTO =  new AuthorDTO();
        if (page != null && itemPerPage != null)
        {
            System.out.println("get");
            //System.out.println(page + itemPerPage);
            //System.out.println(Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            authorDTO.setMaxPage((int)Math.ceil((double)authorRepository.findAllByAuthornameContaining(authorid, PageRequest.of(page - 1, itemPerPage, Sort.by("authorname").ascending())).size() / itemPerPage));
            authorDTO.setAuthorList(authorRepository.findAllByAuthornameContaining(authorid, PageRequest.of(page - 1, itemPerPage, Sort.by("authorname").ascending())));
            System.out.println(authorDTO.getMaxPage());
        }
        else
        {
            //System.out.println("get");
            List<Author> authorList = authorRepository.findAll();
            authorDTO.setMaxPage(authorList.size());
            authorDTO.setAuthorList(authorList);
        }
        return authorDTO;

    }
    public AuthorDTO findAllByAuthoridAndAuthorname(String authorid, String authorname, Integer page, Integer itemPerPage)
    {
        //System.out.println(page + itemPerPage);
        AuthorDTO authorDTO =  new AuthorDTO();
        if (page != null && itemPerPage != null)
        {
            System.out.println("get");
            //System.out.println(page + itemPerPage);
            //System.out.println(Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            authorDTO.setMaxPage((int)Math.ceil((double)authorRepository.findAllByAuthoridContainingAndAuthornameContaining(authorid, authorname, PageRequest.of(page - 1, itemPerPage, Sort.by("authorname").ascending())).size() / itemPerPage));
            authorDTO.setAuthorList(authorRepository.findAllByAuthoridContainingAndAuthornameContaining(authorid, authorname, PageRequest.of(page - 1, itemPerPage, Sort.by("authorname").ascending())));
            System.out.println(authorDTO.getMaxPage());
        }
        else
        {
            //System.out.println("get");
            List<Author> authorList = authorRepository.findAll();
            authorDTO.setMaxPage(authorList.size());
            authorDTO.setAuthorList(authorList);
        }
        return authorDTO;

    }
    public ResponseEntity<String> update(Author author, String authorIDOld)
    {
        if (!Objects.equals(author.getAuthorid(), authorIDOld))
        {

            if (authorRepository.findByAuthorid(author.getAuthorid()) == null)
            {
                System.out.println("yes");
                List<BookAuthor> bookAuthorList = bookAuthorService.findAllByAuthorid(authorIDOld);
                bookAuthorService.delete(bookAuthorList);
                authorRepository.delete(authorRepository.findByAuthorid(authorIDOld));
                authorRepository.save(author);
                for (BookAuthor bookAuthor : bookAuthorList)
                {
                    bookAuthor.setAuthorid(author.getAuthorid());
                }
                bookAuthorService.save(bookAuthorList);
                return new ResponseEntity<>("Cập nhật tác giả thành công", HttpStatus.OK);
            }
            else
            {
                System.out.println("yes1");
                return new ResponseEntity<>("Mã tác giả đã tồn tại", HttpStatus.BAD_REQUEST);
            }
        }
        else
        {
            authorRepository.save(author);
            return new ResponseEntity<>("Cập nhật tác giả thành công", HttpStatus.OK);
        }
    }
    @Transactional
    public ResponseEntity<Author> addAuthor(Author author)
    {
        if (authorRepository.findByAuthorid(author.getAuthorid()) == null)
        {
            authorRepository.save(author);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }
    public ResponseEntity<Author> deleteAuthor(String authorid)
    {
        if (authorRepository.findByAuthorid(authorid) != null)
        {
            List<BookAuthor> bookAuthorList = bookAuthorService.findAllByAuthorid(authorid);
            bookAuthorService.delete(bookAuthorList);
            authorRepository.delete(authorRepository.findByAuthorid(authorid));
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
