package com.linkedu.backend.services;

import com.linkedu.backend.dto.GuestProfileDTO;
import com.linkedu.backend.entities.GuestProfile;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.OnlineStatus;
import com.linkedu.backend.repositories.GuestProfileRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestProfileService {

    private final GuestProfileRepository guestProfileRepository;
    private final UserRepository userRepository;

    public GuestProfile createProfile(Long userId, GuestProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (guestProfileRepository.findByUserId(userId).isPresent())
            throw new RuntimeException("Profile already exists");

        GuestProfile profile = new GuestProfile();
        profile.setUser(user);
        mapDtoToProfile(dto, profile);
        return guestProfileRepository.save(profile);
    }

    public GuestProfile updateProfile(Long userId, GuestProfileDTO dto) {
        GuestProfile profile = guestProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        mapDtoToProfile(dto, profile);
        return guestProfileRepository.save(profile);
    }

    public GuestProfile getProfile(Long userId) {
        return guestProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public GuestProfile saveProfile(GuestProfile profile) {
        return guestProfileRepository.save(profile);
    }

    private void mapDtoToProfile(GuestProfileDTO dto, GuestProfile profile) {
        if (dto.getDateOfBirth() != null) profile.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getBio() != null) profile.setBio(dto.getBio());
        if (dto.getAvatar() != null) profile.setAvatar(dto.getAvatar());
        if (dto.getAddress() != null) profile.setAddress(dto.getAddress());
        if (dto.getPhoneNumber() != null) profile.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getAvailabilityTime() != null) profile.setAvailabilityTime(dto.getAvailabilityTime());
        if (dto.getOnlineStatus() != null)
            profile.setOnlineStatus(OnlineStatus.valueOf(dto.getOnlineStatus()));
    }
}