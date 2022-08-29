package com.example.librarymanage.Repository;

import com.example.librarymanage.Entity.LibraryCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryCardRepository extends JpaRepository<LibraryCard, Integer> {
    LibraryCard findByLibrarycardid(String libraryCardID);
    List<LibraryCard> findAllByLibrarycardidContainingAndNameContaining(String libraryCardID, String name);
    List<LibraryCard> findAllByNameContaining(String name);

}
