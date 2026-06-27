package com.scada.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String username;

    private String password; // will store encrypted password

    private String role = "USER"; // default role

    // ✅ NEW FIELD
    private String designation;

    // ✅ PHOTO STORE (BLOB)
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;


    public User() {}

    public User(String username, String password, String designation, byte[] photo) {
        this.username = username;
        this.password = password;
        this.designation = designation;
        this.photo = photo;
    }

}