package org.balikin.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;



@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Utang extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private Auth user;
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Auth receiver;
    @Column(nullable = false, name = "name_utang")
    private String name_utang;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Date TransactionDate;


    public enum TransactionType {
        UTANG, PIUTANG;

    }

}
