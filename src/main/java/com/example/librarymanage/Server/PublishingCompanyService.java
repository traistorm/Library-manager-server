package com.example.librarymanage.Server;

import com.example.librarymanage.Entity.PublishingCompany;
import com.example.librarymanage.Repository.PublishingCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublishingCompanyService {
    @Autowired
    PublishingCompanyRepository publishingCompanyRepository;
    public List<PublishingCompany> findAll()
    {
        return publishingCompanyRepository.findAll();
    }
    public PublishingCompany findPublishingCompanyByID(String publishingCompanyID)
    {
        return publishingCompanyRepository.findByPublishingcompanyid(publishingCompanyID);
    }
}
