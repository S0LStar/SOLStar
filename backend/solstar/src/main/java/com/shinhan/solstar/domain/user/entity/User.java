package com.shinhan.solstar.domain.user.entity;

import com.shinhan.solstar.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(length = 45, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 45, nullable = false, unique = true)
    private String nickname;

    private LocalDateTime birthDate;

    @Column(length = 45, unique = true)
    private String phone;

    @Column(length = 255)
    private String profileImage;

    @Column(length = 255)
    private String introduction;

    private float score;

    @Column(length = 255)
    private String userKey;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isDelete;

}
