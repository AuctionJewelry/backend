package com.se.jewelryauction.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "system_wallets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemWalletEntity extends BaseEntiy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float account_balance;
}
