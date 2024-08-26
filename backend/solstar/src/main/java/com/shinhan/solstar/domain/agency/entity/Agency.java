package com.shinhan.solstar.domain.agency.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 45)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String passowrd;

    @Column(name = "phone", nullable = false, length = 45)
    private String phone;

    @Column(name = "refresh_token", nullable = false, length = 255)
    private String refreshToken;

    @Column(name = "is_delete", nullable = false)
    private boolean isDelete;

}
