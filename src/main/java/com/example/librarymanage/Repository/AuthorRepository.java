package com.example.librarymanage.Repository;

import com.example.librarymanage.Entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findByAuthorid(String authorID);
}
