package com.example.taskmanagement.demo.service.Impl;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.taskmanagement.demo.entity.User;
import com.example.taskmanagement.demo.repository.UserRepository;
import com.example.taskmanagement.demo.service.CustomUserDetails;




@Service
public class CustomUserDetailsService implements UserDetailsService {

 private UserRepository userRepository;

 public CustomUserDetailsService(UserRepository userRepository) {
  super();
  this.userRepository = userRepository;
 }

 @Override
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

  User user = userRepository.findByUsername(username);
  if (user == null) {
   throw new UsernameNotFoundException("Username or Password not found");
  }
  return new CustomUserDetails(user.getUsername(), user.getPassword(), authorities(), user.getFullname());
 }

 public Collection<? extends GrantedAuthority> authorities() {
  return Arrays.asList(new SimpleGrantedAuthority("USER"));
 }

}