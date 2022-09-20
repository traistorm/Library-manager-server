package com.example.librarymanage.Repository;

import com.example.librarymanage.Entity.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findByAuthorid(String authorID);
    List<Author> findAllByAuthoridContaining(String authorid, Pageable pageable);
    List<Author> findAllByAuthornameContaining(String authorname, Pageable pageable);
    List<Author> findAllByAuthoridContainingAndAuthornameContaining(String authorid, String authorname, Pageable pageable);

}
