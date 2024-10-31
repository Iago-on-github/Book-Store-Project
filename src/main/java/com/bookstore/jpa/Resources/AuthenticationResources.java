package com.bookstore.jpa.Resources;

import com.bookstore.jpa.Models.User.LoginResponseDTO;
import com.bookstore.jpa.Models.User.RegisterUserDTO;
import com.bookstore.jpa.Models.User.User;
import com.bookstore.jpa.Models.User.UserRequest;
import com.bookstore.jpa.Repositories.UserRepository;
import com.bookstore.jpa.infra.Exceptions.SecurityConfigurations.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationResources {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    @Autowired
    public AuthenticationResources(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(userRequest.login(), userRequest.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateTokenJwt( (User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDTO registerUserDTO) {
        if (this.userRepository.findByLogin(registerUserDTO.login()) != null)
            return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerUserDTO.password());

        User newUser = new User(registerUserDTO.login(), encryptedPassword, registerUserDTO.role());
        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
