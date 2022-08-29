package com.example.librarymanage.Server;

import com.example.librarymanage.DTO.BookDTO;
import com.example.librarymanage.DTO.BorrowPayDTO;
import com.example.librarymanage.DTO.LibraryCardDTO;
import com.example.librarymanage.Entity.Book;
import com.example.librarymanage.Entity.BorrowPay;
import com.example.librarymanage.Entity.LibraryCard;
import com.example.librarymanage.Entity.Staff;
import com.example.librarymanage.Repository.LibraryCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryCardService {
    @Autowired
    LibraryCardRepository libraryCardRepository;
    @Autowired
    @Lazy
    BorrowPayService borrowPayService;
    public LibraryCardDTO findAll(Integer page, Integer itemPerPage)
    {
        if (page != null && itemPerPage != null)
        {
            //System.out.println(page + itemPerPage);
            LibraryCardDTO libraryCardDTO =  new LibraryCardDTO();
            //System.out.println(Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            libraryCardDTO.setMaxPage((int)Math.ceil((double)libraryCardRepository.findAll().size() / itemPerPage));
            //borrowPayDTO.setBorrowPayList(borrowPayRepository.findAll(PageRequest.of(page - 1, itemPerPage, Sort.by("borrowpayid").ascending())).getContent());

            List<LibraryCard> libraryCardList = libraryCardRepository.findAll(PageRequest.of(page - 1, itemPerPage, Sort.by("librarycardid").ascending())).getContent();

            libraryCardDTO.setLibraryCardList(libraryCardList);
            //System.out.println(borrowPayDTO.getBorrowPayList());
            return libraryCardDTO;
        }
        else
        {
            LibraryCardDTO libraryCardDTO =  new LibraryCardDTO();
            List<LibraryCard> libraryCardList = libraryCardRepository.findAll();
            libraryCardDTO.setLibraryCardList(libraryCardList);
            return libraryCardDTO;
        }
    }
    public List<LibraryCard> findAll()
    {
        return libraryCardRepository.findAll();
    }
    public LibraryCard findByLibrarycardid(String libraryCardID)
    {
        return libraryCardRepository.findByLibrarycardid(libraryCardID);
    }
    public void save(LibraryCard libraryCard)
    {
        //LibraryCard libraryCardFound = libraryCardRepository.findByLibrarycardid(libraryCard)
        libraryCardRepository.save(libraryCard);
    }
    public void update(LibraryCard libraryCardNew, String libraryCardIDOld)
    {
        LibraryCard libraryCardFound = libraryCardRepository.findByLibrarycardid(libraryCardIDOld);
        List<BorrowPay> borrowPayList = borrowPayService.findAllByLibrarycardid(libraryCardIDOld);

        borrowPayService.delete(borrowPayList);
        libraryCardRepository.delete(libraryCardFound);

        System.out.println(borrowPayList);
        for (BorrowPay borrowPay : borrowPayList)
        {
            borrowPay.setLibrarycardid(libraryCardNew.getLibrarycardid());
        }
        libraryCardRepository.save(libraryCardNew);
        borrowPayService.save(borrowPayList);
    }
    public void delete(String libraryCardID)
    {
        List<BorrowPay> borrowPayList = borrowPayService.findAllByLibrarycardid(libraryCardID);
        borrowPayService.delete(borrowPayList);
        libraryCardRepository.delete(libraryCardRepository.findByLibrarycardid(libraryCardID));
    }
    public void add(LibraryCard libraryCard)
    {
        libraryCardRepository.save(libraryCard);
    }
    public LibraryCardDTO findAllByLibrarycardidAndName(String libraryCardID, String name, Integer page, Integer itemPerPage)
    {
        if (page != null && itemPerPage != null)
        {
            LibraryCardDTO libraryCardDTO =  new LibraryCardDTO();
            List<LibraryCard> libraryCardList = libraryCardRepository.findAllByLibrarycardidContainingAndNameContaining(libraryCardID, name);
            libraryCardDTO.setMaxPage((int)Math.ceil((double)libraryCardList.size() / itemPerPage));
            System.out.println(libraryCardList);
            if ((page - 1) * itemPerPage + itemPerPage > libraryCardList.size())
            {
                libraryCardList = libraryCardList.subList((page - 1) * itemPerPage, libraryCardList.size());
            }
            else
            {
                libraryCardList = libraryCardList.subList((page - 1) * itemPerPage, (page - 1) * itemPerPage + itemPerPage);
            }


            libraryCardDTO.setLibraryCardList(libraryCardList);
            //System.out.println(libraryCardList.size());
            return libraryCardDTO;
        }
        else
        {

            return null;
        }
    }
    public LibraryCardDTO findAllByName(String name, Integer page, Integer itemPerPage)
    {
        if (page != null && itemPerPage != null)
        {
            LibraryCardDTO libraryCardDTO =  new LibraryCardDTO();
            List<LibraryCard> libraryCardList = libraryCardRepository.findAllByNameContaining(name);
            libraryCardDTO.setMaxPage((int)Math.ceil((double)libraryCardList.size() / itemPerPage));
            //System.out.println(bookList.size());
            if ((page - 1) * itemPerPage + itemPerPage > libraryCardList.size())
            {
                libraryCardList = libraryCardList.subList((page - 1) * itemPerPage, libraryCardList.size());
            }
            else
            {
                libraryCardList = libraryCardList.subList((page - 1) * itemPerPage, (page - 1) * itemPerPage + itemPerPage);
            }


            libraryCardDTO.setLibraryCardList(libraryCardList);
            //System.out.println(libraryCardList.size());
            return libraryCardDTO;
        }
        else
        {

            return null;
        }
    }
    public List<LibraryCard> findAllByNameContaining(String name)
    {
        return libraryCardRepository.findAllByNameContaining(name);
    }

}
