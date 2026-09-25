package com.ecommece.service;

import com.ecommece.dto.UserProfileDto;
import com.ecommece.dto.UserUpdateRequest;
import com.ecommece.entity.User;
import com.ecommece.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserService currentUserService;

    public UserProfileDto getCurrentUser() {
        String email = currentUserService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new com.ecommece.exception.ResourceNotFoundException("User not found"));

        return new UserProfileDto(user.getId(), user.getName(), user.getEmail());
    }

    public UserProfileDto updateCurrentUser(UserUpdateRequest request) {
        String email = currentUserService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new com.ecommece.exception.ResourceNotFoundException("User not found"));

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        userRepository.save(user);

        return new UserProfileDto(user.getId(), user.getName(), user.getEmail());
    }
}
