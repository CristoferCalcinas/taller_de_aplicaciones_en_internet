package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.CreateUserRequest;
import com.uab.taller.store.service.IProfileService;
import com.uab.taller.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase {
    @Autowired
    private IUserService userService;

    @Autowired
    private IProfileService profileService;

    public User save(CreateUserRequest createUserRequest) {
        Profile profile = new Profile();
        profile.setName(createUserRequest.getName());
        profile.setGender(createUserRequest.getGender());
        profile.setBirthDate(createUserRequest.getBirthDate());
        profile.setLastName(createUserRequest.getLastName());

        Profile profile$ = profileService.save(profile);

        User user = new User();
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        user.setProfile(profile$);

        return userService.save(user);
    }

}
