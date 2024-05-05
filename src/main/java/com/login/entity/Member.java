package com.login.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Short type;

    @Column(length = 50)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 50)
    private String socialId;

    @Column(length = 200)
    private String refreshToken;

    private Boolean deleted;

    public void withdrawal() {
        this.deleted = true;
    }

}
