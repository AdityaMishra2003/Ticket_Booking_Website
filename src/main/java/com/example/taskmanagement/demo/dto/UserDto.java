package com.example.taskmanagement.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private String fullname;

    public UserDto(boolean verified) {
        this.verified = verified;
    }

    @NotBlank
    @Email(message = "Please provide a valid email address")
    private String username;

    @NotBlank
    private String password;
    private String empId;
    private boolean verified;
    private String otp;
    // getters and setters
   
    public UserDto(String username, String password, String fullname) {
     super();
     this.username = username;
     this.password = password;
     this.fullname = fullname;
    }

    public String getUsername() {
     return username;
    }
   
    public void setUsername(String username) {
     this.username = username;
    }
   
    public String getPassword() {
     return password;
    }
   
    public void setPassword(String password) {
     this.password = password;
    }
   
    public String getFullname() {
     return fullname;
    }
   
    public void setFullname(String fullname) {
     this.fullname = fullname;
    }
   
    @Override
    public String toString() {
     return "UserDto [username=" + username + ", password=" + password + ", fullname=" + fullname + "]";
    }
   }