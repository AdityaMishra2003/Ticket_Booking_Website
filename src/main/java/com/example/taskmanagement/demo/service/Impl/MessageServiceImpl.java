package com.example.taskmanagement.demo.service.Impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskmanagement.demo.entity.Messages;
import com.example.taskmanagement.demo.repository.MessageRepository;
import com.example.taskmanagement.demo.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Override
    public Messages save(Messages messages) {
      return messageRepository.save(messages);
    }


    public List<Messages> findMessagesBySenderEmailAndRecieverEmail(String senderEmail){
      return messageRepository.findBySenderEmailAndRecvEmail("am9982061@gmail.com",senderEmail);
    }

    public List<Messages> findMessagesByStatus(){
      return messageRepository.findByStatus("unread");
    }
    
}
