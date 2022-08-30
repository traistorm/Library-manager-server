package com.example.librarymanage.DTO;

import com.example.librarymanage.Entity.Token;
import com.example.librarymanage.Entity.User;
import lombok.Data;

@Data
public class UserDTO {
    private User user;
    private Token token;
    private String result = "";
}
