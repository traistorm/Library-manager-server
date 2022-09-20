package com.example.librarymanage.Server;

import com.example.librarymanage.DTO.BookDTO;
import com.example.librarymanage.DTO.BorrowPayDTO;
import com.example.librarymanage.Entity.Book;
import com.example.librarymanage.Entity.BorrowPay;
import com.example.librarymanage.Entity.LibraryCard;
import com.example.librarymanage.Entity.Staff;
import com.example.librarymanage.Repository.BorrowPayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BorrowPayService {
    @Autowired
    BorrowPayRepository borrowPayRepository;
    @Autowired
    LibraryCardService libraryCardService;
    @Autowired
    StaffService staffService;

    public BorrowPayDTO findAll(Integer page, Integer itemPerPage)
    {
        if (page != null && itemPerPage != null)
        {
            //System.out.println(page + itemPerPage);
            BorrowPayDTO borrowPayDTO =  new BorrowPayDTO();
            //System.out.println(Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            borrowPayDTO.setMaxPage((int)Math.ceil((double)borrowPayRepository.findAll().size() / itemPerPage));
            //borrowPayDTO.setBorrowPayList(borrowPayRepository.findAll(PageRequest.of(page - 1, itemPerPage, Sort.by("borrowpayid").ascending())).getContent());

            List<BorrowPay> borrowPayList = borrowPayRepository.findAll(PageRequest.of(page - 1, itemPerPage, Sort.by("borrowpayid").ascending())).getContent();
            List<LibraryCard> libraryCardList = new ArrayList<>();
            List<Staff> staffList = new ArrayList<>();
            for (BorrowPay borrowPay : borrowPayList)
            {
                LibraryCard libraryCard = libraryCardService.findByLibrarycardid(borrowPay.getLibrarycardid());
                if (!libraryCardList.contains(libraryCard))
                {
                    libraryCardList.add(libraryCard);
                }
                Staff staff = staffService.findByStaffid(borrowPay.getStaffid());
                if (!staffList.contains(staff))
                {
                    staffList.add(staff);
                }
            }

            borrowPayDTO.setLibraryCardList(libraryCardList);
            borrowPayDTO.setStaffList(staffList);
            borrowPayDTO.setBorrowPayList(borrowPayList);
            //System.out.println(borrowPayDTO.getBorrowPayList());
            return borrowPayDTO;
        }
        else
        {
            BorrowPayDTO borrowPayDTO = new BorrowPayDTO();
            borrowPayDTO.setBorrowPayList(borrowPayRepository.findAll());
            borrowPayDTO.setLibraryCardList(libraryCardService.findAll());
            borrowPayDTO.setStaffList(staffService.findAll());
            //borrowPayDTO.setMaxPage((int)Math.ceil((double)borrowPayRepository.findAll().size() / itemPerPage));
            return borrowPayDTO;
        }
    }
    public BorrowPayDTO findByBorrowpayid(Integer borrowPayID, Integer page, Integer itemPerPage)
    {
        System.out.println("Check");
        BorrowPayDTO borrowPayDTO = new BorrowPayDTO();
        BorrowPay borrowPay = borrowPayRepository.findByBorrowpayid(borrowPayID);
        System.out.println("Check");
        borrowPayDTO.setMaxPage(1);
        List<LibraryCard> libraryCardList = new ArrayList<>();
        List<Staff> staffList = new ArrayList<>();

        LibraryCard libraryCard = libraryCardService.findByLibrarycardid(borrowPay.getLibrarycardid());
        Staff staff = staffService.findByStaffid(borrowPay.getStaffid());
        libraryCardList.add(libraryCard);
        staffList.add(staff);

        borrowPayDTO.setLibraryCardList(libraryCardList);
        borrowPayDTO.setStaffList(staffList);
        List<BorrowPay> borrowPayList = new ArrayList<>();
        borrowPayList.add(borrowPay);
        borrowPayDTO.setBorrowPayList(borrowPayList);
        return borrowPayDTO;
    }
    public List<BorrowPay> findAllByLibrarycardid(String libraryCardID)
    {
        return borrowPayRepository.findAllByLibrarycardid(libraryCardID);
    }
    @Transactional
    public void delete(List<BorrowPay> borrowPayList)
    {
        borrowPayRepository.deleteAll(borrowPayList);
    }
    public void save(List<BorrowPay> borrowPayList)
    {
        borrowPayRepository.saveAll(borrowPayList);
    }
    public void save(BorrowPay borrowPay)
    {
        borrowPayRepository.save(borrowPay);
    }
    public BorrowPayDTO findAllByBorrowPayName(String borrowPayName, Integer page, Integer itemPerPage)
    {
        BorrowPayDTO borrowPayDTO = new BorrowPayDTO();

        List<LibraryCard> libraryCardList = libraryCardService.findAllByNameContaining(borrowPayName);
        List<BorrowPay> borrowPayList = new ArrayList<>();
        List<Staff> staffList = new ArrayList<>();
        for (LibraryCard libraryCard : libraryCardList)
        {
            borrowPayList.addAll(borrowPayRepository.findByLibrarycardid(libraryCard.getLibrarycardid()));
        }

        for (BorrowPay borrowPay : borrowPayList)
        {
            Staff staff = staffService.findByStaffid(borrowPay.getStaffid());
            if (!staffList.contains(staff))
            {
                staffList.add(staff);
            }
        }
        borrowPayDTO.setMaxPage((int)Math.ceil((double)borrowPayList.size() / itemPerPage));
        if ((page - 1) * itemPerPage + itemPerPage > libraryCardList.size())
        {
            borrowPayList = borrowPayList.subList((page - 1) * itemPerPage, borrowPayList.size());
        }
        else
        {
            borrowPayList = borrowPayList.subList((page - 1) * itemPerPage, (page - 1) * itemPerPage + itemPerPage);
        }
        borrowPayDTO.setBorrowPayList(borrowPayList);
        borrowPayDTO.setStaffList(staffList);
        borrowPayDTO.setLibraryCardList(libraryCardList);
        return borrowPayDTO;
    }
    public BorrowPayDTO findAllByBorrowPayIDAndBorrowPayName(Integer borrowPayID, String borrowPayName, Integer page, Integer itemPerPage)
    {
        BorrowPayDTO borrowPayDTO = new BorrowPayDTO();

        List<LibraryCard> libraryCardList = libraryCardService.findAllByNameContaining(borrowPayName);
        List<BorrowPay> borrowPayList = new ArrayList<>();
        List<Staff> staffList = new ArrayList<>();
        for (LibraryCard libraryCard : libraryCardList)
        {
            borrowPayList.addAll(borrowPayRepository.findByLibrarycardid(libraryCard.getLibrarycardid()));
        }
        System.out.println(borrowPayList);
        borrowPayList.removeIf(borrowPay -> !borrowPay.getBorrowpayid().toString().contains(borrowPayID.toString()));
        System.out.println(borrowPayList);
        for (BorrowPay borrowPay : borrowPayList)
        {
            Staff staff = staffService.findByStaffid(borrowPay.getStaffid());
            if (!staffList.contains(staff))
            {
                staffList.add(staff);
            }
        }
        borrowPayDTO.setMaxPage((int)Math.ceil((double)borrowPayList.size() / itemPerPage));
        if ((page - 1) * itemPerPage + itemPerPage > libraryCardList.size())
        {
            borrowPayList = borrowPayList.subList((page - 1) * itemPerPage, borrowPayList.size());
        }
        else
        {
            borrowPayList = borrowPayList.subList((page - 1) * itemPerPage, (page - 1) * itemPerPage + itemPerPage);
        }
        borrowPayDTO.setBorrowPayList(borrowPayList);
        borrowPayDTO.setStaffList(staffList);
        borrowPayDTO.setLibraryCardList(libraryCardList);
        return borrowPayDTO;
        //return bookRepository.findAllByBookidAndBooktitleContaining(bookID, bookTitle);
    }
    public List<BorrowPay> findAllByStaffid(String staffID)
    {
        return borrowPayRepository.findAllByStaffid(staffID);
    }
    List<BorrowPay> findAllByBookid(String bookID)
    {
        return borrowPayRepository.findAllByBookid(bookID);
    }
    @Transactional
    public void delete(BorrowPay borrowPay)
    {
        System.out.println(borrowPayRepository.findByBorrowpayid(borrowPay.getBorrowpayid()));
        //System.out.println(borrowPay.getBorrowpayid());
        borrowPayRepository.delete(borrowPay);
    }
    public BorrowPay findByBorrowPayid(Integer borrowPayID)
    {
        return borrowPayRepository.findByBorrowpayid(borrowPayID);
    }
}
