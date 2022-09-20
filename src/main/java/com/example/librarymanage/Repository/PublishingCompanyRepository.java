package com.example.librarymanage.Repository;

import com.example.librarymanage.Entity.PublishingCompany;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublishingCompanyRepository extends JpaRepository<PublishingCompany, Integer> {
    PublishingCompany findByPublishingcompanyid(String publishingCompanyID);
    List<PublishingCompany> findAllByPublishingcompanyidContainingIgnoreCase(String publishingCompanyID, Pageable pageable);
    List<PublishingCompany> findAllByPublishingcompanynameContainingIgnoreCase(String publishingCompanyName, Pageable pageable);

    List<PublishingCompany> findAllByPublishingcompanyidContainingIgnoreCaseAndPublishingcompanynameContainingIgnoreCase(String publishingCompanyID, String publishingCompanyName, Pageable pageable);
}
