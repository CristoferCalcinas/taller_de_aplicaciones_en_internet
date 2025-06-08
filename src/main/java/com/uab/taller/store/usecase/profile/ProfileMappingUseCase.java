package com.uab.taller.store.usecase.profile;

import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.domain.dto.request.ProfileRequest;
import com.uab.taller.store.domain.dto.request.UpdateProfileRequest;
import com.uab.taller.store.domain.dto.response.ProfileResponse;
import com.uab.taller.store.domain.dto.response.ProfileSummaryResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileMappingUseCase {
    /**
     * Convierte un ProfileRequest a una entidad Profile
     */
    public Profile toProfile(ProfileRequest request) {
        return toEntity(request);
    }

    /**
     * Convierte un ProfileRequest a una entidad Profile
     */
    public Profile toEntity(ProfileRequest request) {
        if (request == null) {
            return null;
        }

        Profile profile = new Profile();
        profile.setName(request.getName());
        profile.setLastName(request.getLastName());
        profile.setCi(request.getCi());
        profile.setMobile(request.getMobile());
        profile.setAddress(request.getAddress());
        profile.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");

        return profile;
    }

    /**
     * Convierte un UpdateProfileRequest a una entidad Profile
     */
    public Profile toProfile(UpdateProfileRequest request) {
        if (request == null) {
            return null;
        }

        Profile profile = new Profile();
        profile.setName(request.getName());
        profile.setLastName(request.getLastName());
        profile.setCi(request.getCi());
        profile.setMobile(request.getMobile());
        profile.setAddress(request.getAddress());
        profile.setStatus(request.getStatus());

        return profile;
    }

    /**
     * Actualiza una entidad Profile con los datos de UpdateProfileRequest
     */
    public Profile updateEntity(Profile existing, UpdateProfileRequest request) {
        if (existing == null || request == null) {
            return existing;
        }

        if (request.getName() != null) {
            existing.setName(request.getName());
        }
        if (request.getLastName() != null) {
            existing.setLastName(request.getLastName());
        }
        if (request.getCi() != null) {
            existing.setCi(request.getCi());
        }
        if (request.getMobile() != null) {
            existing.setMobile(request.getMobile());
        }
        if (request.getAddress() != null) {
            existing.setAddress(request.getAddress());
        }
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }

        return existing;
    }

    /**
     * Convierte una entidad Profile a ProfileResponse
     */
    public ProfileResponse toProfileResponse(Profile profile) {
        return toResponse(profile);
    }

    /**
     * Convierte una entidad Profile a ProfileResponse
     */
    public ProfileResponse toResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        return ProfileResponse.builder()
                .id(profile.getId())
                .name(profile.getName())
                .lastName(profile.getLastName())
                .ci(profile.getCi())
                .mobile(profile.getMobile())
                .address(profile.getAddress())
                .status(profile.getStatus())
                .addDate(profile.getAddDate())
                .addUser(profile.getAddUser())
                .changeDate(profile.getChangeDate())
                .changeUser(profile.getChangeUser())
                .deleted(profile.isDeleted())
                .build();
    }

    /**
     * Convierte una entidad Profile a ProfileSummaryResponse
     */
    public ProfileSummaryResponse toProfileSummaryResponse(Profile profile) {
        return toSummaryResponse(profile);
    }

    /**
     * Convierte una entidad Profile a ProfileSummaryResponse
     */
    public ProfileSummaryResponse toSummaryResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        String fullName = (profile.getName() != null ? profile.getName() : "") +
                " " + (profile.getLastName() != null ? profile.getLastName() : "");

        return ProfileSummaryResponse.builder()
                .id(profile.getId())
                .fullName(fullName.trim())
                .ci(profile.getCi())
                .status(profile.getStatus())
                .mobile(profile.getMobile())
                .active("ACTIVE".equals(profile.getStatus()) && !profile.isDeleted())
                .build();
    }

    /**
     * Convierte una lista de entidades Profile a lista de ProfileSummaryResponse
     */
    public List<ProfileSummaryResponse> toProfileSummaryResponseList(List<Profile> profiles) {
        return toSummaryResponseList(profiles);
    }

    /**
     * Convierte una lista de entidades Profile a lista de ProfileResponse
     */
    public List<ProfileResponse> toResponseList(List<Profile> profiles) {
        if (profiles == null) {
            return null;
        }

        return profiles.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una lista de entidades Profile a lista de ProfileSummaryResponse
     */
    public List<ProfileSummaryResponse> toSummaryResponseList(List<Profile> profiles) {
        if (profiles == null) {
            return null;
        }

        return profiles.stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }
}
