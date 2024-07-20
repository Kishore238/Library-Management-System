package com.practice.LBM.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 1024,nullable = false,unique = true)
    private String username;
    private String password;
    private String role;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Fine fine;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Order order;

}
