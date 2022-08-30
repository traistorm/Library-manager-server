package com.example.librarymanage.Server;

import com.example.librarymanage.DTO.UserDTO;
import com.example.librarymanage.Entity.Token;
import com.example.librarymanage.Entity.User;
import com.example.librarymanage.Repository.UserRepository;
import org.mindrot.jbcrypt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenService tokenService;
    public List<User> findAll()
    {
        return userRepository.findAll();
    }
    public User findByUserid(Integer userID)
    {
        return userRepository.findByUserid(userID);
    }
    public UserDTO login(String username, String password, String tokenValue)
    {
        UserDTO userDTO = new UserDTO();
        // Generate token
        if (tokenValue != null)
        {
            Token token = tokenService.findByTokenvalue(tokenValue);
            if (token != null)
            {
                Date date = new Date();
                if ((Long.parseLong(token.getInitializationtime()) + 60 * 60 * 1000) > date.getTime())
                {
                    userDTO.setToken(token);
                    userDTO.setResult("Token is valid");
                    //tokenService.delete(token);
                    return userDTO;
                }
                else // Token timeout
                {
                    tokenService.delete(token); // Delete token from database
                    userDTO.setResult("Token timeout");
                    return userDTO;
                }
            }
            else
            {
                userDTO.setToken(token);
                userDTO.setResult("Token is invalid");
                return userDTO;
            }

        }
        else
        {
            User user = userRepository.findByUsername(username);


            if (user != null)
            {
                System.out.println(user.getPassword());
                boolean valuate = BCrypt.checkpw(password, user.getPassword());
                if (valuate)
                {
                    Random random = ThreadLocalRandom.current();
                    byte[] randomBytes = new byte[32];
                    random.nextBytes(randomBytes);
                    Token token = new Token();
                    Date date = new Date();
                    token.setTokenvalue(Base64.getUrlEncoder().encodeToString(randomBytes));
                    token.setInitializationtime(String.valueOf(date.getTime()));
                    token.setRole(user.getRole());
                    tokenService.save(token);
                    userDTO.setToken(token);
                    userDTO.setUser(user);
                    return userDTO; // Return token
                }
                else
                {
                    userDTO.setResult("Password is incorrect");
                    return userDTO;
                }

            }
            else
            {
                userDTO.setResult("User not found");
                return userDTO;
            }
        }

    }
    public boolean logout(String token)
    {
        System.out.println(token);
        Token tokenFound = tokenService.findByTokenvalue(token);
        System.out.println("Token found" + tokenFound);
        if (token != null)
        {
            System.out.println("Delete");
            tokenService.delete(tokenFound);
        }
        return true;
    }
    public String getTokenRole(String token)
    {
        Token tokenFound = tokenService.findByTokenvalue(token);
        if (tokenFound != null)
        {
            return tokenFound.getRole();
        }
        return null;
    }
}
