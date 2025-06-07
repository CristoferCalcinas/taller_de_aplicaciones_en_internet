package com.uab.taller.store.usecase.beneficiary;

import com.uab.taller.store.domain.Account;
import com.uab.taller.store.domain.Beneficiary;
import com.uab.taller.store.domain.User;
import com.uab.taller.store.domain.dto.request.BeneficiaryRequest;
import com.uab.taller.store.service.interfaces.IAccountService;
import com.uab.taller.store.service.interfaces.IBeneficiaryService;
import com.uab.taller.store.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateBeneficiaryUseCase {
    @Autowired
    IBeneficiaryService beneficiaryService;

    @Autowired
    IUserService userService;

    @Autowired
    IAccountService accountService;

    public Beneficiary save(BeneficiaryRequest beneficiaryRequest) {
        User user = userService.findById(beneficiaryRequest.getUserId());
        Account account = accountService.findById(beneficiaryRequest.getAccountId());

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setUser(user);
        beneficiary.setAccount(account);

        return beneficiaryService.save(beneficiary);
    }
}
