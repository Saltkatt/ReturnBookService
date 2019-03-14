package com.saltkatt.returnservice.controller;

import com.saltkatt.returnservice.models.Book;
import com.saltkatt.returnservice.models.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

@RestController
public class Controller {

    Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private RestTemplate restTemplate;

    public Controller (RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/hej")
    public String hej(){
        return "heeej";
    }

    /**
     * Receives user and book id, checks against stock-service and sends userId and bookId
     * to user-service and library-stock-service.
     * @param userId
     * @param bookId
     * @return
     */
    @GetMapping("/return/{userId}/{bookId}")
    public Receipt returnBook(@PathVariable("userId") Long userId, @PathVariable("bookId") long bookId){
        logger.info("A user has input a bookId");
        //call stock-service send in bookId
        ResponseEntity<Book> response = restTemplate.getForEntity("http://localhost:8081/getOneBook/" + bookId, Book.class);
        Book book = response.getBody();

        //if no book has the bookId sent in the book does not exist.
        if(!book.equals(bookId)){
            logger.info("Invalid bookId");
            return new Receipt ("", null, "", "This bookId does not seem to exist.");
        }

        //Sends bookId and userId to library-stock-service
        restTemplate.put("http://localhost:8081/books/return/", bookId, userId);
        logger.info("Message sent to library-stock-service");

        //Sends userId and bookId to user-service
        restTemplate.put("http://user-service/return-book/", userId, bookId);
        logger.info("Message sent to UserService");

        return new Receipt(book.getBookTitle(), book.getAuthors(),book.getReturnDate(),"Book has been returned");

        /**
         * Todo: should call method to remove book from user loanlist.
         */
        //userHasReturnedBook();
    }

    /**
     * @return the date of return
     */
    private String getReturnDate() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + Calendar.DAY_OF_MONTH;
    }

    //Todo: figure out how get this message up before returnBook() is used.
    private String getMessage(){
        String message = "Please input your userId number and the bookId of the book you wish to return. " +
                "\nInput the id numbers in the URL-bar in the following fashion: \n" +
                "\nhttp://return-service/return/{userId}/{bookId}/";
        return message;
    }




}
