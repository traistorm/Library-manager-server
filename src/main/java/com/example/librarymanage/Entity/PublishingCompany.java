package com.example.librarymanage.Entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "publishingcomany")
public class PublishingCompany {
    @Id
    private String publishingcompanyid;
    private String publishingcompanyname;
    private LocalDate publishingcompanyfounddate;
}
