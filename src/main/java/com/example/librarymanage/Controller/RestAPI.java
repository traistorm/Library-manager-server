package com.example.librarymanage.Controller;

import com.example.librarymanage.DTO.*;
import com.example.librarymanage.Entity.*;
import com.example.librarymanage.Server.*;
import com.example.librarymanage.SpringSecurity.CustomUserDetails;
import com.example.librarymanage.SpringSecurity.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
// Khong the gui FormData qua delete method

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("http://localhost:3000/")
public class RestAPI {
    @Autowired
    BookService bookService;
    @Autowired
    AuthorService authorService;
    @Autowired
    BookAuthorService bookAuthorService;
    @Autowired
    BorrowPayService borrowPayService;
    @Autowired
    LibraryCardService libraryCardService;
    @Autowired
    PublishingCompanyService publishingCompanyService;
    @Autowired
    StaffService staffService;
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestParam(required = false, name = "username") String username,
                                          @RequestParam(required = false, name = "password") String password,
                                          @RequestParam(required = false, name = "token") String token) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
        System.out.println(username + password);

        LoginDTO loginDTO = new LoginDTO();
        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        //SecurityContextHolder.getContext().setAuthentication(authentication);
        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());

        User user = userService.findByUsername(((CustomUserDetails) authentication.getPrincipal()).getUsername());

        loginDTO.setToken(jwt);
        loginDTO.setUsername(((CustomUserDetails) authentication.getPrincipal()).getUsername());
        loginDTO.setRole(((CustomUserDetails) authentication.getPrincipal()).getAuthorities().toString());
        //System.out.println(dinnerTable);
        return new ResponseEntity<>(loginDTO, HttpStatus.OK);

    }

    @PostMapping("/check-token")
    public ResponseEntity<LoginDTO> checkToken() {
        System.out.println("CheckToken");
        LoginDTO loginDTO = new LoginDTO();
        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        //SecurityContextHolder.getContext().setAuthentication(authentication);
        // Trả về jwt cho người dùng.
        User user = new User();

        loginDTO.setUsername(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        loginDTO.setRole(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities().toString());
        //System.out.println(dinnerTable);
        return new ResponseEntity<>(loginDTO, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/test-admin")
    public String testAdmin() {
        return "Success";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/test-user")
    public String testUser() {
        return "Success";
    }

    @GetMapping("/login/role")
    public ResponseEntity<String> getUser(@RequestParam(required = false, name = "username") String username,
                                          @RequestParam(required = false, name = "password") String password,
                                          @RequestParam(required = false, name = "token") String token) {
        //int a = 1;
        try {
            return new ResponseEntity<>(userService.getTokenRole(token), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam(required = false, name = "username") String username,
                                         @RequestParam(required = false, name = "password") String password,
                                         @RequestParam(required = false, name = "token") String token) {
        //int a = 1;
        try {

            userService.logout(token); // Delete token
            return new ResponseEntity<>("Logout success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam(required = false, name = "passwordold") String passwordOld,
                                                 @RequestParam(required = false, name = "passwordnew") String passwordNew) {
        //int a = 1;
        try {
            if (passwordNew.equals(passwordOld)) {
                return new ResponseEntity<>("Mật khẩu mới phải khác mật khẩu cũ", HttpStatus.OK);
            }

                String result = userService.changePassword(passwordOld, passwordNew, ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserid());
                return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/change-password-admin")
    public ResponseEntity<String> changePasswordAdmin(@RequestParam(required = false, name = "passwordnew") String passwordNew,
                                                      @RequestParam(required = false, name = "userid") Integer userid) {
        try {


                String result = userService.changePasswordAdmin(userid, passwordNew);
                return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public List<User> getUserList() {
        //int a = 1;
        return userService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/users/{id}")
    public User findUserByID(@PathVariable(name = "id") Integer id) {
        return userService.findByUserid(id);
    }

    @PutMapping("/users")
    public ResponseEntity<User> saveUser(User user, @RequestParam(name = "usernameold", required = false) String usernameOld) {

        try {

                    return userService.saveUser(user, usernameOld);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<User> addUser(User user) {

        try {
            return userService.addUser(user);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/users/search")
    public ResponseEntity<UserDTOReturnToClient> searchUsers(@RequestParam(name = "username", required = false) String username,
                                                             @RequestParam(name = "page", required = false) Integer page,
                                                             @RequestParam(name = "itemPerPage", required = false) Integer itemPerPage) {
        try {

            return userService.findAllByUsername(username, page, itemPerPage);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/users")
    public ResponseEntity<User> addUser(@RequestParam(name = "userid") Integer userid) {

        try {

            return userService.deleteUser(userid);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")

    @GetMapping("/publishing-companies")
    @ResponseBody
    public ResponseEntity<PublishingCompanyDTO> getPublishingCompanyList(@RequestParam(required = false, name = "page") Integer page,
                                                                         @RequestParam(required = false, name = "itemperpage") Integer itemPerPage) {
        //int a = 1;
        try {

            return new ResponseEntity<>(publishingCompanyService.findAll(page, itemPerPage), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PostMapping("/publishing-companies/search")
    public ResponseEntity<PublishingCompanyDTO> searchPublishingCompany(@RequestParam(name = "publishingcompanyid", required = false) String publishingCompanyID,
                                                                        @RequestParam(name = "publishingcompanyname", required = false) String publishingCompanyName,
                                                                        @RequestParam(name = "page", required = false) Integer page,
                                                                        @RequestParam(name = "itemPerPage", required = false) Integer itemPerPage) {
        try {

            if (!publishingCompanyID.equals("") && publishingCompanyName.equals("")) {
                return publishingCompanyService.findAllByPublishingcompanyid(publishingCompanyID, page, itemPerPage);
            } else if (publishingCompanyID.equals("") && !publishingCompanyName.equals("")) {
                return publishingCompanyService.findAllByPublishingcompanyname(publishingCompanyName, page, itemPerPage);
            } else {
                return publishingCompanyService.findAllByPublishingcompanyidAndPublishingcompanyname(publishingCompanyID, publishingCompanyName, page, itemPerPage);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/publishing-companies")
    public ResponseEntity<PublishingCompanyDTO> savePublishingCompany(PublishingCompany publishingCompany, @RequestParam(name = "token", required = false) String token,
                                                                      @RequestParam(name = "publishingcompanyidold", required = false) String publishingCompanyIDOld) {

        try {

            return publishingCompanyService.update(publishingCompany, publishingCompanyIDOld);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/publishing-companies")
    public ResponseEntity<PublishingCompanyDTO> addPublishingCompany(PublishingCompany publishingCompany, @RequestParam(name = "token", required = false) String token) {

        try {

            System.out.println(publishingCompany);
            return publishingCompanyService.addPublishingCompany(publishingCompany);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/publishing-companies/{id}")
    @ResponseBody
    public ResponseEntity<PublishingCompany> findPublishingCompanyByID(@PathVariable(name = "id") String id) {
        return new ResponseEntity<>(publishingCompanyService.findPublishingCompanyByID(id), HttpStatus.OK);
    }

    @DeleteMapping("/publishing-companies")
    public ResponseEntity<PublishingCompany> deletePublishingCompany(@RequestParam(name = "publishingcompanyid") String publishingcompanyid) {
        return publishingCompanyService.deletePublishingCompany(publishingcompanyid);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/books")
    @ResponseBody
    public ResponseEntity<BookDTO> getBookList(@RequestParam(name = "page", required = false) Integer page,
                                               @RequestParam(name = "itemperpage", required = false) Integer itemPerPage) {
        System.out.println(page + " " + itemPerPage);
        try {

            return new ResponseEntity<>(bookService.findAll(page, itemPerPage), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/books/top-views")
    @ResponseBody
    public ResponseEntity<List<Book>> getBookTopViewsList(@RequestParam(name = "number", required = false) Integer number) {
        try {
            System.out.println("Check");
            List<Book> bookList = bookService.getTopViewList(number);
            System.out.println(bookList);
            return new ResponseEntity<>(bookList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/books/{id}")
    public Book findBookByID(@PathVariable(name = "id") String id) {
        return bookService.findByBookid(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/books")
    @ResponseBody
    public ResponseEntity<String> saveBook(Book book, @RequestParam(required = false) Map<String, String> authorIDMap,
                                           @RequestParam(name = "bookidold") String bookIDOld) {
        //System.out.println("Save");
        try {

            if (bookService.save(book, bookIDOld)) {
                int authorID = 0;
                while (authorIDMap.get("author" + authorID) != null) {
                    BookAuthor bookAuthor = new BookAuthor();
                    bookAuthor.setBookid(book.getBookid());
                    bookAuthor.setAuthorid(authorIDMap.get("author" + authorID));
                    bookAuthorService.save(bookAuthor);

                    authorID++;
                    //System.out.println("Save0");
                }
                return new ResponseEntity<>("Lưu thông tin thành công !", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Lưu thông tin thất bại !", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lưu thông tin thất bại !", HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/books")
    @ResponseBody
    public ResponseEntity<String> addNewBook(Book book, @RequestParam(required = false) Map<String, String> authorIDMap) {
        //System.out.println("Save");
        try {

            if (bookService.add(book)) {
                int authorID = 0;
                while (authorIDMap.get("author" + authorID) != null) {
                    BookAuthor bookAuthor = new BookAuthor();
                    bookAuthor.setBookid(book.getBookid());
                    bookAuthor.setAuthorid(authorIDMap.get("author" + authorID));
                    bookAuthorService.save(bookAuthor);

                    authorID++;
                }
                return new ResponseEntity<>("Thêm sách thành công !", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Thêm sách thất bại !", HttpStatus.BAD_REQUEST);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Thêm sách thất bại !", HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/books")
    @ResponseBody
    public ResponseEntity<String> deleteBook(@RequestParam(name = "bookid") String bookid) {
        try {

            bookService.delete(bookService.findByBookid(bookid));
            return new ResponseEntity<>("Xoá sách thành công !", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        //bookService.delete(book);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PostMapping("/books/search")
    public ResponseEntity<BookDTO> fingBookByID(@RequestParam(name = "bookid", required = false) String bookID,
                                                @RequestParam(name = "bookname", required = false) String bookName,
                                                @RequestParam(name = "page", required = false) Integer page,
                                                @RequestParam(name = "itemperpage", required = false) Integer itemPerPage) {
        try {

            if (!bookID.equals("") && bookName.equals("")) {
                return new ResponseEntity<>(bookService.findAllByBookid(bookID, page, itemPerPage), HttpStatus.OK);
            } else if (bookID.equals("") && !bookName.equals("")) {
                return new ResponseEntity<>(bookService.findAllBooktitle(bookName, page, itemPerPage), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(bookService.findAllByBookidAndBooktitle(bookID, bookName, page, itemPerPage), HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/library-cards")
    public ResponseEntity<LibraryCardDTO> getLibraryCardList(@RequestParam(name = "page", required = false) Integer page,
                                                             @RequestParam(name = "itemperpage", required = false) Integer itemPerPage) {
        try {

            return new ResponseEntity<>(libraryCardService.findAll(page, itemPerPage), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/library-cards")
    public ResponseEntity<LibraryCardDTO> saveLibraryCard(LibraryCard libraryCard,
                                                          @RequestParam(name = "librarycardidold") String libraryCardIDOld) {
        try {

            libraryCardService.update(libraryCard, libraryCardIDOld);
            return new ResponseEntity<>(null, HttpStatus.OK);


        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/library-cards")
    public ResponseEntity<String> addLibraryCard(LibraryCard libraryCard) {
        System.out.println(libraryCard);
        try {

            if (libraryCardService.findByLibrarycardid(libraryCard.getLibrarycardid()) == null) {
                libraryCardService.save(libraryCard);
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/library-cards")
    public ResponseEntity<LibraryCardDTO> deleteLibraryCard(@RequestParam(name = "librarycardid", required = false) String libraryCardID) {
        //System.out.println(test);
        //libraryCardService.update(libraryCard, libraryCardIDOld);
        try {

            libraryCardService.delete(libraryCardID);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/library-cards/search")
    public ResponseEntity<LibraryCardDTO> searchLibraryCard(@RequestParam(name = "librarycardid", required = false) String libraryCardID,
                                                            @RequestParam(name = "librarycardname", required = false) String libraryCardName,
                                                            @RequestParam(name = "page", required = false) Integer page,
                                                            @RequestParam(name = "itemperpage", required = false) Integer itemPerPage) {
        System.out.println(libraryCardID + " " + libraryCardName);
        try {

            if (!libraryCardID.equals("") && libraryCardName.equals("")) {
                return new ResponseEntity<>(libraryCardService.findByLibrarycardidContaining(libraryCardID, page, itemPerPage), HttpStatus.OK);
            } else if (libraryCardID.equals("") && !libraryCardName.equals("")) {
                return new ResponseEntity<>(libraryCardService.findAllByName(libraryCardName, page, itemPerPage), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(libraryCardService.findAllByLibrarycardidAndName(libraryCardID, libraryCardName, page, itemPerPage), HttpStatus.OK);
            }


        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/library-cards/{id}")
    public LibraryCard finLibraryCardByID(@PathVariable(name = "id") String id) {
        return libraryCardService.findByLibrarycardid(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/staffs")
    public ResponseEntity<StaffDTO> getStaffList(@RequestParam(name = "page", required = false) Integer page,
                                                 @RequestParam(name = "itemperpage", required = false) Integer itemPerPage) {
        try {

            return new ResponseEntity<>(staffService.findAll(page, itemPerPage), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(staffService.findAll(null, null), HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/staffs/search")
    public ResponseEntity<StaffDTO> searchStaff(@RequestParam(name = "staffid", required = false) String staffID,
                                                @RequestParam(name = "staffname", required = false) String staffName,
                                                @RequestParam(name = "page", required = false) Integer page,
                                                @RequestParam(name = "itemPerPage", required = false) Integer itemPerPage) {
        try {

            if (!staffID.equals("") && staffName.equals("")) {

                return staffService.findAllByStaffid(staffID, page, itemPerPage);
            } else if (staffID.equals("") && !staffName.equals("")) {
                return staffService.findAllByStaffname(staffName, page, itemPerPage);
            } else {
                return staffService.findAllByStaffidAndStaffname(staffID, staffName, page, itemPerPage);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/staffs")
    public ResponseEntity<String> getStaffList(Staff staff, @RequestParam(name = "staffidold", required = false) String staffIDOld) {

        try {

            staffService.update(staff, staffIDOld);
            return new ResponseEntity<>("Lưu thông tin thành công", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/staffs")
    public ResponseEntity<String> deleteStaff(@RequestParam(name = "staffid", required = false) String staffid) {
        try {

            staffService.deleteStaff(staffid);
            return new ResponseEntity<>("Xoá nhân viên thành công", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/staffs")
    public ResponseEntity<String> getStaffList(Staff staff) {
        try {

            staffService.save(staff);
            return new ResponseEntity<>("Lưu thông tin thành công", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/staffs/{id}")
    public Staff finStaffByID(@PathVariable(name = "id") String id) {
        return staffService.findByStaffid(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/authors")
    @ResponseBody
    public ResponseEntity<AuthorDTO> getAuthorList(@RequestParam(name = "page", required = false) Integer page,
                                                   @RequestParam(name = "itemperpage", required = false) Integer itemPerPage) {
        try {
            return new ResponseEntity<>(authorService.findAll(page, itemPerPage), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/authors/{id}")
    @ResponseBody
    public ResponseEntity<Author> finAuthorByID(@PathVariable(name = "id") String id) {
        return new ResponseEntity<>(authorService.findByAuthorid(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/authors")
    public ResponseEntity<String> saveAuthor(Author author, @RequestParam(name = "authoridold", required = false) String authorIDOld) {

        try {

            return authorService.update(author, authorIDOld);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/authors")
    public ResponseEntity<Author> addAuthor(Author author) {

        try {

            return authorService.addAuthor(author);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ResponseBody
    @PostMapping("/authors/search")
    public ResponseEntity<AuthorDTO> searchAuthor(@RequestParam(name = "authorname", required = false) String authorname,
                                                  @RequestParam(name = "authorid", required = false) String authorid,
                                                  @RequestParam(name = "page", required = false) Integer page,
                                                  @RequestParam(name = "itemperpage", required = false) Integer itemPerPage) {
        try {
            if (!authorid.equals("") && authorname.equals("")) {
                return new ResponseEntity<>(authorService.findAllByAuthorid(authorid, page, itemPerPage), HttpStatus.OK);
            } else if (authorid.equals("") && !authorname.equals("")) {
                return new ResponseEntity<>(authorService.findAllByAuthorname(authorname, page, itemPerPage), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(authorService.findAllByAuthoridAndAuthorname(authorid, authorname, page, itemPerPage), HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/authors")
    public ResponseEntity<Author> deleteAuthor(@RequestParam(name = "authorid") String authorid) {

        try {

            return authorService.deleteAuthor(authorid);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/book-author")
    @ResponseBody
    public ResponseEntity<BookAuthorDTO> getBookAuthorList(@RequestParam(name = "page", required = false) Integer page,
                                                           @RequestParam(name = "itemperpage", required = false) Integer itemPerPage) {
        try {
            return new ResponseEntity<>(bookAuthorService.findAll(page, itemPerPage), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/book-author")
    @ResponseBody
    public ResponseEntity<String> addBookAuthor(BookAuthor bookAuthor) {
        try {

            bookAuthorService.save(bookAuthor);
            return new ResponseEntity<>("Lưu thông tin thành công", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/book-author")
    @ResponseBody
    public ResponseEntity<String> updateBookAuthor(BookAuthor bookAuthor) {
        try {
            bookAuthorService.save(bookAuthor);
            return new ResponseEntity<>("Lưu thông tin thành công", HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Lưu thông tin thất bại", HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/book-author/{bookID}/{authorID}")
    @ResponseBody
    public ResponseEntity<List<BookAuthor>> deleteBookAuthor(@PathVariable(name = "bookID") String bookID,
                                                             @PathVariable(name = "authorID") String authorID) {
        System.out.println(bookID);
        bookAuthorService.delete(bookID, authorID);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /*@GetMapping("/book-author/{id}")
    @ResponseBody
    public ResponseEntity<Boo==> finAuthorByID(@PathVariable(name = "id") String id)
    {
        return new ResponseEntity<>(authorService.findByAuthorid(id), HttpStatus.OK);
    }*/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/borrow-pay")
    @ResponseBody
    public ResponseEntity<BorrowPayDTO> getBookPayList(@RequestParam(name = "page", required = false) Integer page,
                                                       @RequestParam(name = "itemperpage", required = false) Integer itemPerPage) {
        try {
            return new ResponseEntity<>(borrowPayService.findAll(page, itemPerPage), HttpStatus.OK);

        } catch (Exception e) {

        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/borrow-pay")
    @ResponseBody
    public ResponseEntity<String> addNewBorrowPay(BorrowPay borrowPay) {
        try {

            borrowPayService.save(borrowPay);
            return new ResponseEntity<>("Thêm phiếu thành công", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/borrow-pay")
    @ResponseBody
    public ResponseEntity<String> saveBorrowPay(BorrowPay borrowPay,
                                                @RequestParam(name = "token") String token) {
        try {

            borrowPayService.save(borrowPay);
            return new ResponseEntity<>("Lưu phiếu thành công", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/borrow-pay/search")
    @ResponseBody
    public ResponseEntity<BorrowPayDTO> searchBorrowPay(@RequestParam(name = "borrowpayid", required = false) Integer borrowPayID,
                                                        @RequestParam(name = "borrowpayname", required = false) String borrowPayName,
                                                        @RequestParam(name = "page", required = false) Integer page,
                                                        @RequestParam(name = "itemperpage", required = false) Integer itemPerPage) {
        try {

            if (!borrowPayName.equals("")) {
                return new ResponseEntity<>(borrowPayService.findAllByBorrowPayName(borrowPayName, page, itemPerPage), HttpStatus.OK);

            } else {
                System.out.println("Check 3");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/borrow-pay/{id}")
    @ResponseBody
    public ResponseEntity<BorrowPay> getBookPayList(@PathVariable(name = "id", required = false) Integer id) {
        return new ResponseEntity<>(borrowPayService.findByBorrowPayid(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/borrow-pay")
    @ResponseBody
    public ResponseEntity<String> deleteBorrowPay(@RequestParam(name = "borrowpayid", required = false) Integer borrowPayID) {
        try {

            System.out.println("ID : " + borrowPayID);
            borrowPayService.delete(borrowPayService.findByBorrowPayid(borrowPayID));
            return new ResponseEntity<>("Xoá thành công", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    }
}
