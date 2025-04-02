package com.uab.taller.store.repository;

import com.uab.taller.store.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
