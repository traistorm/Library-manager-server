package com.example.librarymanage.Entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "borrowpay")
public class BorrowPay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer borrowpayid;
    private String librarycardid;
    private String staffid;
    private String bookid;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate borrowpaydate;
    private boolean status;
}
