package com.saltkatt.returnservice.controller;

import com.saltkatt.returnservice.models.Book;
import com.saltkatt.returnservice.models.Receipt;
import com.saltkatt.returnservice.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

    /**
     * Receives user and book id and sends them
     * to user-service and stock-service.
     * @param userId
     * @param bookId
     * @return
     */
    @GetMapping("/return/{userId}/{bookId}")
    public Receipt returnBook(@PathVariable("userId") Long userId, @PathVariable("bookId") long bookId){

        User user;

        logger.info("A user has input a bookId");

        /**
         * Inform stock-service which book is being returned.
         */
        ResponseEntity<Book> response = restTemplate.exchange("http://storage-service/books/return/"
                + bookId, HttpMethod.PUT, new HttpEntity<>(userId), Book.class);
        logger.info("Message sent to stock-service");
        if(response.getStatusCode() != HttpStatus.OK){
            return new Receipt("", null, "", "Stock-service is having problems.");
        }
        /**
         * Inform user-service which user is returning which book.
         */
        try{
            user = restTemplate.getForObject("http://user-service/return-book/" + userId + "/" + bookId, User.class);
            logger.info("Message sent to user-service");
        }catch (Exception e){
            return new Receipt("", null, "", "User-service is having problems.");
        }
        Book book = response.getBody();

        /**
         * Print receipt
         */
        return new Receipt(book.getBookTitle(), book.getAuthors(),book.getReturnDate(),"Book has been returned");

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
