package com.example.librarymanage.SpringSecurity;

import com.example.librarymanage.Entity.User;
import com.example.librarymanage.Server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceSpringSecurity implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        //System.out.println("Test");
        User user = userService.findByUsername(username);
        if (user == null) {
            System.out.println("NULL");
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
    }
    public UserDetails loadUserById(Integer userID) {
        //System.out.println("Test");
        User user = userService.findById(userID);
        if (user == null) {
            throw new UsernameNotFoundException(userID.toString());
        }
        return new CustomUserDetails(user);
    }

}