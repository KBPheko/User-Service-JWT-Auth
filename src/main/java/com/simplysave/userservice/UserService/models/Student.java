package com.simplysave.userservice.UserService.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author Karabo Pheko
 * */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id")
public class Student extends User {

    @Column(name = "student_no", nullable = false)
    private String studentNo;

    @Column(name = "id_no", nullable = false)
    private String idNo;

    @Column(name = "image_url")
    private String imageUrl;


}
