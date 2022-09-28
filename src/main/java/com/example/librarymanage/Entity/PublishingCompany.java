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
@Table(name = "publishingcomany")
public class PublishingCompany {
    @Id
    private String publishingcompanyid;
    private String publishingcompanyname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishingcompanyfounddate;

    @JsonIgnore
    @OneToMany(mappedBy = "publishingCompany", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoog sử dụng trong toString()
    private List<Book> bookList = new ArrayList<>();
}
