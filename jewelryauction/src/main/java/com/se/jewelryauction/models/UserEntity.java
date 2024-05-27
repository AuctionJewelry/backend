package com.se.jewelryauction.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
@Entity
@Table(name = "users")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String full_name;

    private String phone_number;

    @Email
    @Column(nullable = false)
    private String email;

    @JsonIgnore
    private String password;

    private String address;

    private String imageUrl;

    private LocalDate date_of_birth;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role_id;

    @Column(nullable = false)
    private Boolean email_verified = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TokenEntity> tokens;
}
