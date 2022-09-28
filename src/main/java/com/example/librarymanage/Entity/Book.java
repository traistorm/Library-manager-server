package com.example.librarymanage.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "book")
public class Book {
    @Id
    private String bookid;
    private String booktitle;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishingdate;
    private String publishingcompanyid;
    private Integer view;
    private Integer remainingamount;
    private String booklink;

    @JsonIgnore
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoog sử dụng trong toString()
    private List<BookAuthor> bookAuthorList = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "publishingcompanyid", insertable = false, updatable = false) // thông qua khóa ngoại
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private PublishingCompany publishingCompany;

}
