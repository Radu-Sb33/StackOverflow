package com.codeelevate.stackoverflow_spring.controller;


// Importing required classes

import com.codeelevate.stackoverflow_spring.entity.EmailDetails;
import com.codeelevate.stackoverflow_spring.service.EmailSendingService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Annotation
@RestController
@CrossOrigin(origins = "http://localhost:4200")
// Class
public class EmailController {

    @Autowired 
    private EmailSendingService emailService;

    // Sending a simple Email
    @PostMapping("/sendMail")
    public String
    sendMail(@RequestBody EmailDetails details)
    {

        return emailService.sendSimpleMail(details);
    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
            @RequestBody EmailDetails details)
    {

        return emailService.sendMailWithAttachment(details);
    }
}