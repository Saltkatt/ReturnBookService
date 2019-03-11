package com.saltkatt.returnservice.controller;

import com.saltkatt.returnservice.models.Book;
import com.saltkatt.returnservice.models.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * Receives user and book id, checks against stock-service
     * @param userId
     * @param bookId
     * @return
     */
    @GetMapping("/return/{userId}/{bookId}")
    public Receipt returnBook(@PathVariable("userId") long userId, @PathVariable("bookId") long bookId){
        logger.info("A user has input a bookId");
        //call stock-service send in bookId
        ResponseEntity<Book> response = restTemplate.getForEntity("http://library-stock-service/getOneBook/" + bookId, Book.class);
        Book book = response.getBody();

        //if no book has the bookId sent in the book does not exist.
        if(!book.equals(bookId)){
            logger.info("Invalid bookId");
            return new Receipt ("", null, "", "This bookId does not seem to exist.");
        }

        //Todo: change bookId to available
        restTemplate.put("http://library-stock-service/books/update/", bookId);
        logger.info("Message sent to library-stock-service");

        return new Receipt(book.getBookTitle(), book.getAuthors(),book.getReturnDate(),"Book has been returned");

        /**
         * Todo: should call method to remove book from user loanlist.
         */
        //userHasReturnedBook();
    }

    /**
     * Sends userId and bookId to user-service to be removed from loanlist
     * @param userId
     * @param bookId
     */
    private void userHasReturnedBook(@PathVariable("userId") long userId, @PathVariable("bookId") long bookId) {
        restTemplate.put("http://user-service/return-book/", userId, bookId);
        logger.info("BookId sent to UserService");
    }

    /**
     * @return todays date
     */
    private String getReturnDate() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + Calendar.DAY_OF_MONTH;
    }


}
