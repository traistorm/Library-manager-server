package com.example.librarymanage.Server;

import com.example.librarymanage.DTO.BookDTO;
import com.example.librarymanage.DTO.StaffDTO;
import com.example.librarymanage.Entity.BorrowPay;
import com.example.librarymanage.Entity.LibraryCard;
import com.example.librarymanage.Entity.Staff;
import com.example.librarymanage.Repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class StaffService {
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    @Lazy
    BorrowPayService borrowPayService;
    public Staff findByStaffid(String staffID)
    {
        return staffRepository.findByStaffid(staffID);
    }
    public StaffDTO findAll(Integer page, Integer itemPerPage)
    {
        if (page != null && itemPerPage != null)
        {
            System.out.println(page + itemPerPage);
            StaffDTO staffDTO =  new StaffDTO();
            List<Staff> staffList = staffRepository.findAll();
            //System.out.println(Math.ceil((double)bookRepository.findAll().size() / itemPerPage));
            staffDTO.setMaxPage((int)Math.ceil((double)staffList.size() / itemPerPage));
            if ((page - 1) * itemPerPage + itemPerPage > staffList.size())
            {
                staffList = staffList.subList((page - 1) * itemPerPage, staffList.size());
            }
            else
            {
                staffList = staffList.subList((page - 1) * itemPerPage, (page - 1) * itemPerPage + itemPerPage);
            }
            staffDTO.setStaffList(staffList);
            return staffDTO;
        }
        else
        {
            StaffDTO staffDTO = new StaffDTO();
            staffDTO.setStaffList(staffRepository.findAll());
            return staffDTO;
        }

    }
    public List<Staff> findAll()
    {
        return staffRepository.findAll();
    }
    public ResponseEntity<String> update(Staff staff, String staffIDOld)
    {
        if (!Objects.equals(staff.getStaffid(), staffIDOld))
        {
            if (staffRepository.findByStaffid(staff.getStaffid()) == null)
            {
                List<BorrowPay> borrowPayList = borrowPayService.findAllByStaffid(staffIDOld);
                borrowPayService.delete(borrowPayList);
                staffRepository.delete(staffRepository.findByStaffid(staffIDOld));
                staffRepository.save(staff);
                for (BorrowPay borrowPay : borrowPayList)
                {
                    borrowPay.setStaffid(staff.getStaffid());
                }
                borrowPayService.save(borrowPayList);
                return new ResponseEntity<>("Cập nhật nhân viên thành công", HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("Mã nhân viên đã tồn tại", HttpStatus.BAD_REQUEST);
            }
        }
        else
        {
            System.out.println(staff);
            staffRepository.save(staff);
            return new ResponseEntity<>("Cập nhật nhân viên thành công", HttpStatus.OK);
        }
    }
    public ResponseEntity<String> save(Staff staff)
    {
        if (staffRepository.findByStaffid(staff.getStaffid()) == null)
        {
            staffRepository.save(staff);
            return new ResponseEntity<>("Thêm nhân viên thành công", HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Thêm nhân viên thất bại", HttpStatus.BAD_REQUEST);
        }
    }
}
