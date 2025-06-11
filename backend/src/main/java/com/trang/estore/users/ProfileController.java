package com.trang.estore.users;

import com.trang.estore.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    private final AuthService authService;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileDto> getProfile() {
        var profileDto = profileService.getProfile();
        return ResponseEntity.ok(profileDto);

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

}
