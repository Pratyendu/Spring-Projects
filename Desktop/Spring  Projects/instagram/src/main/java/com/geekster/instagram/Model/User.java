package com.geekster.instagram.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_user")

public class User {
    @Id //primary key
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private int age;

}
