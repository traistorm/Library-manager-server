package com.example.librarymanage.Server;

import com.example.librarymanage.Entity.Author;
import com.example.librarymanage.Repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;
    public Author findByAuthorid(String authorID)
    {
        return authorRepository.findByAuthorid(authorID);
    }
    public List<Author> findAll()
    {
        return authorRepository.findAll();
    }
    public void save(Author author)
    {
        authorRepository.save(author);
    }
}
