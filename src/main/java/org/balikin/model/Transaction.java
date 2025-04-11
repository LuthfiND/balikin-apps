package org.balikin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String type; // "utang" or "piutang"

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private LocalDate date;
}