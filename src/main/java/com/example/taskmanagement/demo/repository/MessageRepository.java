package com.example.taskmanagement.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskmanagement.demo.entity.Messages;
import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Messages,Long> {
    List<Messages> findBySenderEmailAndRecvEmail(String senderEmail,String recvEmail);
    List<Messages> findByStatus(String status);
}
