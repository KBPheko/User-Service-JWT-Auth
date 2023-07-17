package com.simplysave.userservice.UserService.repositories;

import com.simplysave.userservice.UserService.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
