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

@Entity
@Data
@Table(name = "author")
public class Author {
    @Id
    private String authorid;
    private String authorname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateofbirth;

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Khoog sử dụng trong toString()
    private List<BookAuthor> bookAuthorList = new ArrayList<>();
}
