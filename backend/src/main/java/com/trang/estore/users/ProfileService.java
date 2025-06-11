package com.trang.estore.users;

import com.trang.estore.auth.AuthService;
import io.jsonwebtoken.impl.security.EdwardsCurve;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileService {
    private final AuthService authService;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public ProfileDto getProfile() {
        var user = authService.getCurrentUser();
        if (user == null) {
            throw new UserNotFoundException();
        }
        var userId = user.getId();
        var profile = profileRepository.findById(userId).orElseThrow();
        return profileMapper.toDto(profile);
    }
}
