package com.example.librarymanage.Server;

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
    public String login(String username, String password, String tokenValue)
    {
        // Generate token
        if (tokenValue != null)
        {
            Token token = tokenService.findByTokenvalue(tokenValue);
            if (token != null)
            {
                Date date = new Date();
                if ((Long.parseLong(token.getInitializationtime()) + 60 * 60 * 1000) > date.getTime())
                {
                    //tokenService.delete(token);
                    return "Token is valid";
                }
                else // Token timeout
                {
                    tokenService.delete(token); // Delete token from database
                    return "Token timeout";
                }
            }
            else
            {
                return "Token is invalid";
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
                    tokenService.save(token);
                    return token.getTokenvalue(); // Return token
                }
                else
                {
                    return "Password is incorrect";
                }

            }
            else
            {
                return "User not found";
            }
        }

    }
}
