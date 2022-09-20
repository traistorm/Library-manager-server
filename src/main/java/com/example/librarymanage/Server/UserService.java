package com.example.librarymanage.Server;

import com.example.librarymanage.DTO.PublishingCompanyDTO;
import com.example.librarymanage.DTO.UserDTO;
import com.example.librarymanage.DTO.UserDTOReturnToClient;
import com.example.librarymanage.Entity.Token;
import com.example.librarymanage.Entity.User;
import com.example.librarymanage.Repository.UserRepository;
import org.mindrot.jbcrypt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
                    userDTO.setUser(userRepository.findByUserid(token.getUserid()));
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
                    token.setUserid(user.getUserid());
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
    public String changePassword(String passwordOld, String passwordNew, Integer userID)
    {
        System.out.println("Change password");
        User user = userRepository.findByUserid(userID);
        boolean valuate = BCrypt.checkpw(passwordOld, user.getPassword());
        if (valuate)
        {
            user.setPassword(BCrypt.hashpw(passwordNew, BCrypt.gensalt(12)));
            userRepository.save(user);
            return "Thay đổi mật khẩu thành công !";
        }
        else
        {
            return "Mật khẩu cũ không đúng !";
        }
    }
    public String changePasswordAdmin(Integer userID, String passwordNew)
    {
        System.out.println("Change password");
        User user = userRepository.findByUserid(userID);

        user.setPassword(BCrypt.hashpw(passwordNew, BCrypt.gensalt(12)));
        userRepository.save(user);
        return "Thay đổi mật khẩu thành công !";

    }
    public ResponseEntity<User> addUser(User user)
    {
        try
        {
            System.out.println(user.getUsername());
            if (userRepository.findByUsername(user.getUsername()) == null)
            {

                user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
                save(user);
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<User> saveUser(User user, String usernameOld)
    {
        try
        {
            User userOld = userRepository.findByUsername(usernameOld);
            System.out.println(user.getUsername());
            if (userOld != null)
            {
                userOld.setUsername(user.getUsername());
                userOld.setRole(user.getRole());
                userOld.setDatecreated(user.getDatecreated());
                save(userOld);
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<User> deleteUser(Integer userid)
    {
        try {
            User user = userRepository.findByUserid(userid);
            if (user != null)
            {
                delete(user);
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<UserDTOReturnToClient> findAllByUsername(String username, Integer page, Integer itemPerPage)
    {
        try {
            UserDTOReturnToClient userDTOReturnToClient = new UserDTOReturnToClient();

            userDTOReturnToClient.setMaxPage((int) ((userRepository.findAllByUsernameContainingIgnoreCase(username, Pageable.unpaged()).size() / itemPerPage) + 1));
            userDTOReturnToClient.setUserList(userRepository.findAllByUsernameContainingIgnoreCase(username, PageRequest.of(page - 1, itemPerPage, Sort.by("userid").ascending())));
            System.out.println();
            return new ResponseEntity<>(userDTOReturnToClient, HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }
    public User findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }
    public User findById(Integer userid)
    {
        return userRepository   .findByUserid(userid);
    }
    @Transactional
    public void save(User user)
    {
        userRepository.save(user);
    }
    @Transactional
    public void delete(User user)
    {
        userRepository.delete(user);
    }
}
