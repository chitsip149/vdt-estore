package com.trang.estore.auth;

import com.trang.estore.users.UserDto;
import com.trang.estore.users.UserMapper;
import com.trang.estore.users.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginDto loginDto,
            HttpServletResponse response
    ) {
        var loginResponse = authService.login(loginDto, response);

        return ResponseEntity.ok(new JwtResponse(loginResponse.getAccessToken().toString()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token after expiration time")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ){
        var accessToken = authService.refresh(refreshToken);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/me")
    @Operation(summary = "Get the current authenticated user")
    public ResponseEntity<UserDto> me(){
        var user = authService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<Void> handleTokenInvalidException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
