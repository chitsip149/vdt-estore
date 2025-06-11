package com.trang.estore.users;

import com.trang.estore.common.ErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Operation(summary = "Get all users, can specify the sort method, either by name or email (optional)")
    @GetMapping
    public Iterable<UserDto> getAllUsers(@RequestParam( required = false, defaultValue = "", name = "sort") String sortBy) {
        return userService.getAllUsers(sortBy);
    }

    @Operation(summary = "Get a specific user")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var userDto = userService.getUser(id);

        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Register a new user")
    @PostMapping
    //MethodArgumentNotValidException
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserRequest registerUserRequest,
            UriComponentsBuilder uriBuilder
    ) {

        var userDto = userService.registerUser(registerUserRequest);

        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @Operation(summary = "Update a user's information")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest request){
        var userDto = userService.updateUser(id, request);

        return ResponseEntity.ok(userDto);

    }

    @Operation(summary = "Delete a user from the database")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Change the password of a user")
    @PostMapping("/{id}/password-change")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }

    //UserNotFoundException -> Not Found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //DuplicateUserException -> Bad Request
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorDto> handleDuplicateUserException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


}
