package com.example.librarymanage.Repository;

import com.example.librarymanage.Entity.PublishingCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishingCompanyRepository extends JpaRepository<PublishingCompany, Integer> {
    PublishingCompany findByPublishingcompanyid(String publishingCompanyID);
}
