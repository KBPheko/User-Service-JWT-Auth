package com.simplysave.userservice.UserService.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Karabo Pheko
 * */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student extends User {

    @Id
    @Column(name = "student_no", nullable = false)
    private String studentNo;

    @Column(name = "id_no", nullable = false)
    private String idNo;

    @Column(name = "image_url")
    private String imageUrl;

}
