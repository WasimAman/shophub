package com.shophub_backend.controller;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shophub_backend.enums.Role;
import com.shophub_backend.model.User;
import com.shophub_backend.repository.UserRepository;
import com.shophub_backend.request.LoginRequest;
import com.shophub_backend.response.AuthResponse;
import com.shophub_backend.security.jwt.JwtProvider;
import com.shophub_backend.service.CartService;
import com.shophub_backend.service.CustomUserDetailsService;
import com.shophub_backend.service.UserService;

@RestController
@RequestMapping("auth/")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CartService cartService;

    @PostMapping("signup")
    public ResponseEntity<AuthResponse> userSignupHandler(@RequestBody User user) {
        boolean isPresent = userService.isUserPersent(user.getEmail());
        if (isPresent) {
            throw new BadCredentialsException("This email is already used please try with another...");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.USER); // Default role
        }

        user.setCreatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        cartService.createCart(user);   // creating user cart

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(savedUser.getRole().name());

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), null,
                Collections.singletonList(authority));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, true));
    }

    @PostMapping("signin")
    public ResponseEntity<AuthResponse> userSigninHandler(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = jwtProvider.generateToken(authentication);
        
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(token, true));
    }
    
    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
    
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password...");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    
}
