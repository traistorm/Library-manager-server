package com.example.librarymanage.DTO;

import com.example.librarymanage.Entity.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTOReturnToClient {
    private Integer maxPage;
    private List<User> userList = new ArrayList<>();
}
