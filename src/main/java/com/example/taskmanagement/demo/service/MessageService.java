package com.example.taskmanagement.demo.service;

import java.util.List;

import com.example.taskmanagement.demo.entity.Messages;

public interface MessageService {

    Messages save(Messages messages);
    public List<Messages> findMessagesBySenderEmailAndRecieverEmail(String senderEmail);
    public List<Messages> findMessagesByStatus();
}
