package com.example.taskmanagement.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taskmanagement.demo.entity.Messages;
import com.example.taskmanagement.demo.entity.User;
import com.example.taskmanagement.demo.service.EmailService;
import com.example.taskmanagement.demo.service.MessageService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date; 





@Controller
public class EmailController {
    @Autowired
    private TaskController taskController;

    @Autowired
    private EmailService emailService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/user/sendMessage")
    public String MessageHandling(Authentication authentication,
                                @RequestParam("Subject") String subject,
                                @RequestParam("body") String body, Model model) {
   User user=taskController.getUserFromAuthentication(authentication);

    emailService.sendMessageFromUser(user.getUsername(),"am9982061@gmail.com", "From :"+user.getUsername()
                                     +" "+subject, body);

        
        Messages messages=new Messages();
        messages.setSenderEmail(user.getUsername());
        messages.setRecvEmail("am9982061@gmail.com");
        messages.setBody(body);
        messages.setSubject(subject);
        Date sendDate = Date.from(LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .toInstant());
        messages.setSendDate(sendDate);
        messages.setStatus("unread");
        messageService.save(messages);
        model.addAttribute("successMessage", "Message sent successfully!");


         return "/UserMessage";
    }
    @PostMapping("/Admin/sendMessage")
    public String MessageHandlingAdmin(
                                @RequestParam("receiverEmail") String receiverEmail,  // Corrected casing here
                                @RequestParam("subject") String subject,              // Fixed subject casing
                                @RequestParam("body") String body, 
                                RedirectAttributes redirectAttributes) {
    
   emailService.sendMessageFromUser("am9982061@gmail.com", receiverEmail, subject, body);

                                    // Use the correct receiverEmail variable
    
        Messages messages = new Messages();
        messages.setSenderEmail("am9982061@gmail.com");
        messages.setRecvEmail(receiverEmail);
        messages.setBody(body);
        messages.setSubject(subject);
    
        Date sendDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        messages.setSendDate(sendDate);
        messages.setStatus("read");
        System.out.println("Message sent Suscessfull");
        messageService.save(messages);
        redirectAttributes.addFlashAttribute("successMessage", "Message sent successfully!");
    
        return "redirect:/AdminMessage";
    }
    @PostMapping("/Admin/sendReply")
    public String AdminReplyingToUser(
                                @RequestParam("receiverEmail") String receiverEmail,  // Corrected casing here
                                @RequestParam("subject") String subject,              // Fixed subject casing
                                @RequestParam("body") String body, 
                               RedirectAttributes redirectAttributes) {
    
   emailService.sendMessageFromUser("am9982061@gmail.com", receiverEmail, subject, body);

                                    // Use the correct receiverEmail variable
    
        Messages messages = new Messages();
        messages.setSenderEmail("am9982061@gmail.com");
        messages.setRecvEmail(receiverEmail);
        messages.setBody(body);
        messages.setSubject(subject);
        Date sendDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        messages.setSendDate(sendDate);
        messages.setStatus("read");
        messageService.save(messages);
        redirectAttributes.addFlashAttribute("successMessage", "Message sent successfully!");
    
        return "redirect:/AdminMessage";
    }
  
    
}
