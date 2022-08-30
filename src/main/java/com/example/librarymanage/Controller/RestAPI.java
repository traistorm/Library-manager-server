package com.example.librarymanage.Controller;

import com.example.librarymanage.DTO.*;
import com.example.librarymanage.Entity.*;
import com.example.librarymanage.Server.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestParam(required = false, name = "username") String username,
                                         @RequestParam(required = false, name = "password") String password,
                                         @RequestParam(required = false, name = "token") String token) {
        //int a = 1;
        if ((username != null && password != null) || token != null) {
            UserDTO userDTO = userService.login(username, password, token);
            return switch (userDTO.getResult()) {
                case "Token timeout" -> new ResponseEntity<>(userDTO, HttpStatus.UNAUTHORIZED);
                case "Token is invalid", "Password is incorrect", "User not found" ->
                        new ResponseEntity<>(userDTO, HttpStatus.BAD_REQUEST);
                default -> new ResponseEntity<>(userDTO, HttpStatus.OK); // Result is token

            };
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }

    }
    @GetMapping("/login/role")
    public ResponseEntity<String> getUser(@RequestParam(required = false, name = "username") String username,
                                        @RequestParam(required = false, name = "password") String password,
                                        @RequestParam(required = false, name = "token") String token) {
        //int a = 1;
        try
        {
            return new ResponseEntity<>(userService.getTokenRole(token), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam(required = false, name = "username") String username,
                                        @RequestParam(required = false, name = "password") String password,
                                        @RequestParam(required = false, name = "token") String token) {
        //int a = 1;
       try
       {

           userService.logout(token); // Delete token
           return new ResponseEntity<>("Logout success", HttpStatus.OK);
       }
       catch (Exception e)
       {
           e.printStackTrace();
           return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
       }

    }

    @GetMapping("/users")
    public List<User> getUserList() {
        //int a = 1;
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User findUserByID(@PathVariable(name = "id") Integer id) {
        return userService.findByUserid(id);
    }

    @GetMapping("/publishing-companies")
    @ResponseBody
    public ResponseEntity<List<PublishingCompany>> getPublishingCompanyList() {
        //int a = 1;
        return new ResponseEntity<>(publishingCompanyService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/publishing-companies/{id}")
    @ResponseBody
    public ResponseEntity<PublishingCompany> findPublishingCompanyByID(@PathVariable(name = "id") String id) {
        return new ResponseEntity<>(publishingCompanyService.findPublishingCompanyByID(id), HttpStatus.OK);
    }

    @GetMapping("/books")
    @ResponseBody
    public ResponseEntity<BookDTO> getBookList(@RequestParam(name = "page", required = false) Integer page,
                                               @RequestParam(name = "itemperpage", required = false) Integer itemPerPage,
                                               @RequestParam(name = "token", required = false) String token) {
        System.out.println(page + " " + itemPerPage);
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
                return new ResponseEntity<>(bookService.findAll(page, itemPerPage), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/books/top-views")
    @ResponseBody
    public ResponseEntity<List<Book>> getBookTopViewsList(@RequestParam(name = "number", required = false) Integer number) {
        try {
            List<Book> bookList = bookService.getTopViewList(number);
            System.out.println(bookList);
            return new ResponseEntity<>(bookList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/books/{id}")
    public Book findBookByID(@PathVariable(name = "id") String id) {
        return bookService.findByBookid(id);
    }

    @PutMapping(value = "/books")
    @ResponseBody
    public ResponseEntity<String> saveBook(Book book, @RequestParam(required = false) Map<String, String> authorIDMap,
                                           @RequestParam(name = "bookidold") String bookIDOld,
                                           @RequestParam(name = "token") String token) {
        //System.out.println("Save");
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
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

            } else {
                return new ResponseEntity<>("Lưu thông tin thất bại !", HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lưu thông tin thất bại !", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/books")
    @ResponseBody
    public ResponseEntity<String> addNewBook(Book book, @RequestParam(required = false) Map<String, String> authorIDMap,
                                             @RequestParam(name = "token") String token) {
        //System.out.println("Save");
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
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

            } else {
                return new ResponseEntity<>("Thêm sách thất bại !", HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Thêm sách thất bại !", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping(value = "/books")
    @ResponseBody
    public ResponseEntity<String> deleteBook(@RequestParam(name = "bookid") String bookid,
                                             @RequestParam(name = "token") String token) {
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
                //bookAuthorService.delete(bookAuthorService.findAllByBookidContaining(bookid));
                bookService.delete(bookService.findByBookid(bookid));
                return new ResponseEntity<>("Xoá sách thành công !", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Xoá sách thất bại !", HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        //bookService.delete(book);
    }

    @ResponseBody
    @PostMapping("/books/search")
    public ResponseEntity<BookDTO> fingBookByID(@RequestParam(name = "bookid", required = false) String bookID,
                                                @RequestParam(name = "bookname", required = false) String bookName,
                                                @RequestParam(name = "page", required = false) Integer page,
                                                @RequestParam(name = "itemperpage", required = false) Integer itemPerPage,
                                                @RequestParam(name = "token", required = false) String token) {
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
                if (!bookID.equals("") && bookName.equals("")) {
                    return new ResponseEntity<>(bookService.findAllByBookid(bookID, page, itemPerPage), HttpStatus.OK);
                } else if (bookID.equals("") && !bookName.equals("")) {
                    return new ResponseEntity<>(bookService.findAllBooktitle(bookName, page, itemPerPage), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(bookService.findAllByBookidAndBooktitle(bookID, bookName, page, itemPerPage), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/library-cards")
    public ResponseEntity<LibraryCardDTO> getLibraryCardList(@RequestParam(name = "page", required = false) Integer page,
                                                             @RequestParam(name = "itemperpage", required = false) Integer itemPerPage,
                                                             @RequestParam(name = "token") String token) {
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
                return new ResponseEntity<>(libraryCardService.findAll(page, itemPerPage), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/library-cards")
    public ResponseEntity<LibraryCardDTO> saveLibraryCard(LibraryCard libraryCard,
                                                          @RequestParam(name = "librarycardidold") String libraryCardIDOld,
                                                          @RequestParam(name = "token") String token) {
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
                libraryCardService.update(libraryCard, libraryCardIDOld);
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/library-cards")
    public ResponseEntity<String> addLibraryCard(LibraryCard libraryCard,
                                                         @RequestParam(name = "token") String token) {
        System.out.println(libraryCard);
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
                if (libraryCardService.findByLibrarycardid(libraryCard.getLibrarycardid()) == null) {
                    libraryCardService.save(libraryCard);
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/library-cards")
    public ResponseEntity<LibraryCardDTO> deleteLibraryCard(@RequestParam(name = "librarycardid", required = false) String libraryCardID,
                                                            @RequestParam(name = "token") String token) {
        //System.out.println(test);
        //libraryCardService.update(libraryCard, libraryCardIDOld);
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
                libraryCardService.delete(libraryCardID);
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/library-cards/search")
    public ResponseEntity<LibraryCardDTO> searchLibraryCard(@RequestParam(name = "librarycardid", required = false) String libraryCardID,
                                                            @RequestParam(name = "librarycardname", required = false) String libraryCardName,
                                                            @RequestParam(name = "page", required = false) Integer page,
                                                            @RequestParam(name = "itemperpage", required = false) Integer itemPerPage,
                                                            @RequestParam(name = "token") String token) {
        System.out.println(libraryCardID + " " + libraryCardName);
        if (libraryCardID != null && libraryCardName != null) {
            if (!libraryCardID.equals("") && libraryCardName.equals("")) {
                return new ResponseEntity<>(libraryCardService.findByLibrarycardidContaining(libraryCardID, page, itemPerPage), HttpStatus.OK);
            } else if (libraryCardID.equals("") && !libraryCardName.equals("")) {
                return new ResponseEntity<>(libraryCardService.findAllByName(libraryCardName, page, itemPerPage), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(libraryCardService.findAllByLibrarycardidAndName(libraryCardID, libraryCardName, page, itemPerPage), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/library-cards/{id}")
    public LibraryCard finLibraryCardByID(@PathVariable(name = "id") String id) {
        return libraryCardService.findByLibrarycardid(id);
    }

    @GetMapping("/staffs")
    public ResponseEntity<StaffDTO> getStaffList(@RequestParam(name = "page", required = false) Integer page,
                                                 @RequestParam(name = "itemperpage", required = false) Integer itemPerPage,
                                                 @RequestParam(name = "token") String token) {
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
                return new ResponseEntity<>(staffService.findAll(page, itemPerPage), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(staffService.findAll(null, null), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/staffs")
    public ResponseEntity<String> getStaffList(Staff staff, @RequestParam(name = "token", required = false) String token,
                                               @RequestParam(name = "staffidold", required = false) String staffIDOld) {

        String result = userService.login(null, null, token).getResult();
        System.out.println(result);
        if (result.equals("Token is valid")) {
            System.out.println(token);
            return staffService.update(staff, staffIDOld);
        } else {
            return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
        }


    }

    @PostMapping("/staffs")
    public ResponseEntity<String> getStaffList(Staff staff, @RequestParam(name = "token", required = false) String token) {
        String result = userService.login(null, null, token).getResult();
        System.out.println(staff);
        if (result.equals("Token is valid")) {
            System.out.println(token);
            return staffService.save(staff);
        } else {
            return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
        }


    }

    @GetMapping("/staffs/{id}")
    public Staff finStaffByID(@PathVariable(name = "id") String id) {
        return staffService.findByStaffid(id);
    }

    @GetMapping("/authors")
    @ResponseBody
    public ResponseEntity<List<Author>> getAuthorList() {
        return new ResponseEntity<>(authorService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/authors/{id}")
    @ResponseBody
    public ResponseEntity<Author> finAuthorByID(@PathVariable(name = "id") String id) {
        return new ResponseEntity<>(authorService.findByAuthorid(id), HttpStatus.OK);
    }

    @GetMapping("/book-author")
    @ResponseBody
    public ResponseEntity<List<BookAuthor>> getBookAuthorList() {
        return new ResponseEntity<>(bookAuthorService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/book-author")
    @ResponseBody
    public ResponseEntity<List<BookAuthor>> addBookAuthor(BookAuthor bookAuthor) {
        bookAuthorService.save(bookAuthor);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/book-author")
    @ResponseBody
    public ResponseEntity<List<BookAuthor>> updateBookAuthor(BookAuthor bookAuthor) {
        System.out.println(bookAuthor);
        bookAuthorService.save(bookAuthor);
        return new ResponseEntity<>(bookAuthorService.findAll(), HttpStatus.OK);
    }

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

    @GetMapping("/borrow-pay")
    @ResponseBody
    public ResponseEntity<BorrowPayDTO> getBookPayList(@RequestParam(name = "page", required = false) Integer page,
                                                       @RequestParam(name = "itemperpage", required = false) Integer itemPerPage,
                                                       @RequestParam(name = "token", required = false) String token) {
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
                return new ResponseEntity<>(borrowPayService.findAll(page, itemPerPage), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {

        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/borrow-pay")
    @ResponseBody
    public ResponseEntity<String> addNewBorrowPay(BorrowPay borrowPay,
                                                  @RequestParam(name = "token") String token) {
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
                borrowPayService.save(borrowPay);
                return new ResponseEntity<>("Thêm phiếu thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/borrow-pay")
    @ResponseBody
    public ResponseEntity<String> saveBorrowPay(BorrowPay borrowPay,
                                                @RequestParam(name = "token") String token) {
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
                borrowPayService.save(borrowPay);
                return new ResponseEntity<>("Lưu phiếu thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/borrow-pay/search")
    @ResponseBody
    public ResponseEntity<BorrowPayDTO> searchBorrowPay(@RequestParam(name = "borrowpayid", required = false) Integer borrowPayID,
                                                        @RequestParam(name = "borrowpayname", required = false) String borrowPayName,
                                                        @RequestParam(name = "page", required = false) Integer page,
                                                        @RequestParam(name = "itemperpage", required = false) Integer itemPerPage,
                                                        @RequestParam(name = "token") String token) {
        try
        {
            if (userService.login(null, null, token).equals("Token is valid")) {
                if (!borrowPayName.equals("")) {
                    return new ResponseEntity<>(borrowPayService.findAllByBorrowPayName(borrowPayName, page, itemPerPage), HttpStatus.OK);

                } else {
                    System.out.println("Check 3");
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("/borrow-pay/{id}")
    @ResponseBody
    public ResponseEntity<BorrowPay> getBookPayList(@PathVariable(name = "id", required = false) Integer id) {
        return new ResponseEntity<>(borrowPayService.findByBorrowPayid(id), HttpStatus.OK);
    }

    @DeleteMapping("/borrow-pay")
    @ResponseBody
    public ResponseEntity<String> deleteBorrowPay(@RequestParam(name = "borrowpayid", required = false) Integer borrowPayID,
                                                  @RequestParam(name = "token", required = false) String token) {
        try {
            if (userService.login(null, null, token).equals("Token is valid")) {
                System.out.println("ID : " + borrowPayID);
                borrowPayService.delete(borrowPayService.findByBorrowPayid(borrowPayID));
                return new ResponseEntity<>("Xoá thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.REQUEST_TIMEOUT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    }
}
