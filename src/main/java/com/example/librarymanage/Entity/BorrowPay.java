package com.example.librarymanage.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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
    private Integer status;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "staffid", insertable = false, updatable = false)
    private Staff staff;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "librarycardid", insertable = false, updatable = false)
    private LibraryCard libraryCard;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "bookid", insertable = false, updatable = false)
    private Book book;

}
