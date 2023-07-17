package com.simplysave.userservice.UserService.repositories;

import com.simplysave.userservice.UserService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
