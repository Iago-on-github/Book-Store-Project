package com.bookstore.jpa.Resources;

import com.bookstore.jpa.Models.User.RegisterUserDTO;
import com.bookstore.jpa.Models.User.User;
import com.bookstore.jpa.Models.User.UserRequest;
import com.bookstore.jpa.Repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationResources {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthenticationResources(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated UserRequest userRequest) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(userRequest.login(), userRequest.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated RegisterUserDTO registerUserDTO) {
        if (this.userRepository.findByLogin(registerUserDTO.login()) != null)
            return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerUserDTO.password());

        User newUser = new User(registerUserDTO.login(), encryptedPassword, registerUserDTO.role());
        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
