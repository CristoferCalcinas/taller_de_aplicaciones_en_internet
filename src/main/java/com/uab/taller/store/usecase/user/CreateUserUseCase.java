package com.uab.taller.store.usecase.user;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.Profile;
import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.CreateUserRequest;
import com.uab.taller.store.service.interfaces.IAccountService;
import com.uab.taller.store.service.interfaces.IProfileService;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CreateUserUseCase {
    @Autowired
    private IUserService userService;

    @Autowired
    private IProfileService profileService;

    @Autowired
    private IAccountService accountService;

    public User save(CreateUserRequest createUserRequest) {
        Profile profile = new Profile();
        profile.setName(createUserRequest.getName());
//        profile.setGender(createUserRequest.getGender());
//        profile.setBirthDate(createUserRequest.getBirthDate());
        profile.setLastName(createUserRequest.getLastName());

        Profile profile$ = profileService.save(profile);

        User user = new User();
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        user.setProfile(profile$);
        user.setAccounts(new ArrayList<>());

        User savedUser = userService.save(user);

        if (createUserRequest.getSaldo() != null && createUserRequest.getSaldo() >= 0.0 && createUserRequest.getType() != null && !createUserRequest.getType().isEmpty()) {
            Account account = new Account();
//            account.setSaldo(createUserRequest.getSaldo());
            account.setType(createUserRequest.getType());
            account.setUser(savedUser);

            Account savedAccount = accountService.save(account);
            savedUser.getAccounts().add(savedAccount);

            savedUser = userService.save(savedUser);
        }

        return savedUser;
    }

}
