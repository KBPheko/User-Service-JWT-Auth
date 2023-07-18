package com.simplysave.userservice.UserService.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User{

    @Column(name = "admin_no", nullable = false)
    private String adminNo;

    public void AdminBuilder(){
        Admin a = Admin.builder().adminNo("mm").build();
    }

}
