package com.trang.estore.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
@Tag(name = "Profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @Operation(summary = "Get the profile of the current authenticated user")
    public ResponseEntity<ProfileDto> getProfile() {
        var profileDto = profileService.getProfile();
        return ResponseEntity.ok(profileDto);

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

}
