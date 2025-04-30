package com.example.user_service.service;

import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.RegisterRequest;
import com.example.user_service.dto.UserResponse;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import com.example.user_service.kafka.RegisteredProducer;
import com.example.user_service.kafka.event.UserRegisterEvent;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.security.CustomUserDetails;
import com.example.user_service.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RegisteredProducer registeredProducer;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       RegisteredProducer registeredProducer) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.registeredProducer = registeredProducer;
    }

    public String register(RegisterRequest request) {
        log.info("Registration request received. Email: {}", request.getEmail());
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Email already registered: {}", request.getEmail());
            throw new RuntimeException("This email is already in use.");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        UserRegisterEvent event = new UserRegisterEvent(
                user.getId(),
                user.getEmail(),
                user.getFullName()
        );
        registeredProducer.userRegisterEvent(event);

        log.info("User successfully registered. Email: {}", request.getEmail());

        return jwtUtil.generateToken(user.getId().toString(), user.getRole().name());
    }

    public String login(LoginRequest request) {
        log.info("Login request received. Email: {}", request.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("Login successful. User ID: {}", userDetails.getId());

        return jwtUtil.generateToken(userDetails.getId().toString(), userDetails.getRole().name());
    }

    public UserResponse getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        log.info("Me endpoint called. User ID: {}", userDetails.getId());

        Long userId = userDetails.getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found in Me endpoint! ID: {}", userId);
                    return new RuntimeException("User not found.");
                });

        log.info("User information retrieved from Me endpoint. ID: {}", user.getId());

        return new UserResponse(user.getId(), user.getFullName(), user.getEmail());
    }
}
