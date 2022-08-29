package com.example.librarymanage.Repository;

import com.example.librarymanage.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserid(Integer userID);
    User findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
}
