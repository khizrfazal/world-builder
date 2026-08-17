package com.my.worldbuilder.user;

import com.my.worldbuilder.common.security.JwtService;
import com.my.worldbuilder.user.dto.LoginResponse;
import com.my.worldbuilder.user.dto.LoginUserRequest;
import com.my.worldbuilder.user.dto.RegisterUserRequest;
import com.my.worldbuilder.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody RegisterUserRequest request) {
        User user = userService.register(request.getUsername(), request.getPassword());
        return new UserResponse(user.getId(), user.getUsername());
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginUserRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());
        String token = jwtService.generateToken(user);
        return new LoginResponse(user.getId(), user.getUsername(), token);
    }
}