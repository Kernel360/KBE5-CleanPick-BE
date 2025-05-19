package com.kdev5.cleanpick.user.infra.repositories;

import com.kdev5.cleanpick.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
