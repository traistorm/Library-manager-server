package com.example.librarymanage.Server;

import com.example.librarymanage.Entity.Token;
import com.example.librarymanage.Repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    @Autowired
    TokenRepository tokenRepository;
    public Token findByTokenvalue(String tokenValue)
    {
        return tokenRepository.findByTokenvalue(tokenValue);
    }
    public void delete(Token token)
    {
        tokenRepository.delete(token);
    }
    public void save(Token token)
    {
        tokenRepository.save(token);
    }
}
