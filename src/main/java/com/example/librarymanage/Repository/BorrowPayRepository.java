package com.example.librarymanage.Repository;

import com.example.librarymanage.Entity.BorrowPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowPayRepository extends JpaRepository<BorrowPay, Integer> {
    BorrowPay findByBorrowpayid(Integer borrowPayID);
    List<BorrowPay> findAllByLibrarycardid(String libraryCardID);
    List<BorrowPay> findByLibrarycardid(String libraryCardID);
    List<BorrowPay> findAllByStaffid(String staffID);
    List<BorrowPay> findAllByBookid(String bookID);
}
