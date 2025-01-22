package com.example.taskmanagement.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageId;
    private String senderEmail;
    private String recvEmail;
    private String subject;
    private String body;
    private Date sendDate;
    private String status;

}
