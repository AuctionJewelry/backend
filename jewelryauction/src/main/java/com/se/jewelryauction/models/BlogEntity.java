package com.se.jewelryauction.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blogs")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogEntity extends BaseEntiy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @ManyToOne
    private UserEntity user;
}
