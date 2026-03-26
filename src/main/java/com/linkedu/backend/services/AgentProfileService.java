package com.linkedu.backend.services;

import com.linkedu.backend.dto.AgentProfileDTO;
import com.linkedu.backend.entities.AgentProfile;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.entities.enums.OnlineStatus;
import com.linkedu.backend.repositories.AgentProfileRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentProfileService {

    private final AgentProfileRepository agentProfileRepository;
    private final UserRepository userRepository;

    public AgentProfile createProfile(Long userId, AgentProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (agentProfileRepository.findByUserId(userId).isPresent())
            throw new RuntimeException("Profile already exists");

        AgentProfile profile = new AgentProfile();
        profile.setUser(user);
        mapDtoToProfile(dto, profile);
        return agentProfileRepository.save(profile);
    }

    public AgentProfile updateProfile(Long userId, AgentProfileDTO dto) {
        AgentProfile profile = agentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        mapDtoToProfile(dto, profile);
        return agentProfileRepository.save(profile);
    }

    public AgentProfile getProfile(Long userId) {
        return agentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public AgentProfile saveProfile(AgentProfile profile) {
        return agentProfileRepository.save(profile);
    }

    private void mapDtoToProfile(AgentProfileDTO dto, AgentProfile profile) {
        if (dto.getDateOfBirth() != null) profile.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getBio() != null) profile.setBio(dto.getBio());
        if (dto.getAvatar() != null) profile.setAvatar(dto.getAvatar());
        if (dto.getAddress() != null) profile.setAddress(dto.getAddress());
        if (dto.getPhoneNumber() != null) profile.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getContactName() != null) profile.setContactName(dto.getContactName());
        if (dto.getEmail() != null) profile.setEmail(dto.getEmail());
        if (dto.getAvailabilityTime() != null) profile.setAvailabilityTime(dto.getAvailabilityTime());
        if (dto.getOnlineStatus() != null)
            profile.setOnlineStatus(OnlineStatus.valueOf(dto.getOnlineStatus()));
    }
}