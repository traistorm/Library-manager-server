package com.example.librarymanage.Repository;

import com.example.librarymanage.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Token findByTokenvalue(String tokenValue);
}
