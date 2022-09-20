package com.example.librarymanage.DTO;

import lombok.Data;

@Data
public class LoginDTO {
    private String token = "";
    private String role = "";
    private String username = "";
}
