// package com.example.travelagency.config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// import com.example.travelagency.Service.Impl.CustomUserDetailsService;
// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     @Autowired
//     CustomUserDetailsService customUserDetailsService;

//     @Bean
//     public static PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http.csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(requests ->
//                 requests
//                     .requestMatchers("/register").permitAll()
//                     .requestMatchers("/login").permitAll()
//                     .requestMatchers("/home").permitAll()
//                     .requestMatchers("/welcome").authenticated()
//                     .requestMatchers("/account").authenticated()
//                     .requestMatchers("/searchBus", "/searchHotel", "/searchTrain", "/searchFlight", "/busResults", "/hotelResults", "/trainResults", "/flightResults").permitAll() // Allow access to search and results pages
//                     .anyRequest().authenticated() // Require authentication for any other requests
//             )
//             .formLogin(login -> login
//                     .loginPage("/login")
//                     .loginProcessingUrl("/login")
//                     .defaultSuccessUrl("/welcome", true)
//                     .permitAll())
//             .logout(logout -> logout
//                     .invalidateHttpSession(true)
//                     .clearAuthentication(true)
//                     .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                     .logoutSuccessUrl("/login?logout")
//                     .permitAll());

//         return http.build();
//     }

//     @Autowired
//     public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//         auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
//     }
// }
package com.example.travelagency.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.travelagency.Service.Impl.CustomUserDetailsService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    CustomSuccessHandler successHandler;
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(requests ->
                requests
                    .requestMatchers("/auth", "/register", "/login", "/searchBus", "/searchHotels", "/searchTrains", "/busResults", "/searchFlights",
                     "/verify-otp", "/forgot-password", "/verify-otp-forgot", "/change-password","/conatct","/about").permitAll()
                    .requestMatchers("/account").authenticated()
                    .requestMatchers("/bookBus", "/bookTrains", "/bookFlights", "/processTrainBooking", "/processBooking", "/processHotelBooking", "/processFlightBooking", "/bookHotels").authenticated()
                    .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/welcome", true)
                .permitAll()
            )
            .logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .oauth2Login(login -> login
                .loginPage("/login")
                .successHandler(successHandler)
            );

    return http.build();
}


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }
}
