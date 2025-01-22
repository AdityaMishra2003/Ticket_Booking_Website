package com.example.taskmanagement.demo.entity;




import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    
    @Column(unique = true)
    private String empId;

    private String fullname;
    private boolean verified;

    public User() {}

    public User(String username, String password, String fullname, boolean verified) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.verified = verified;
    }
}
