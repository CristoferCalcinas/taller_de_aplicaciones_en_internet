package com.uab.taller.store.usecase.account;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.CreateAccountRequest;
import com.uab.taller.store.exception.EntityNotFoundException;
import com.uab.taller.store.service.interfaces.IAccountService;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateAccountUseCase {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IUserService userService;

    @Transactional
    public Account save(CreateAccountRequest request) {
        // Validar que el usuario existe
        User user = userService.findById(request.getUserId());
        if (user == null) {
            throw new EntityNotFoundException("Usuario", request.getUserId());
        }

        // Crear la nueva cuenta
        Account account = new Account();
        account.setUser(user);
        account.setType(request.getType());
        account.setCurrency(request.getCurrency());
        account.setBalance(request.getInitialBalance() != null ? request.getInitialBalance() : BigDecimal.ZERO);
        account.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");

        // El número de cuenta se genera automáticamente en el servicio
        return accountService.save(account);
    }
}
