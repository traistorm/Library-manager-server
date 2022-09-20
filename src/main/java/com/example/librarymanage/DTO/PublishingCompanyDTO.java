package com.example.librarymanage.DTO;

import com.example.librarymanage.Entity.PublishingCompany;
import lombok.Data;

import java.util.List;

@Data
public class PublishingCompanyDTO {
    private Integer maxPage;
    private List<PublishingCompany> publishingCompanyList;
}
