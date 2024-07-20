package com.practice.LBM.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Entity(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    Book book;

    @OneToOne
    User user;

    @Temporal(value = TemporalType.DATE)
    Date createdAt;
}
