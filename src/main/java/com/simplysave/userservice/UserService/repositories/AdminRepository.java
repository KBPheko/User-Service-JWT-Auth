package com.simplysave.userservice.UserService.repositories;

import com.simplysave.userservice.UserService.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
