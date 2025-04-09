package org.balikin.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dompet extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @OneToOne
    @JoinColumn(name = "user_id" ,nullable = false)
    public Auth user;
    @Column(nullable = false, name = "total_hutang")
    public Long totalHutang;
    @Column(nullable = false, name = "total_piutang")
    public Long totalPiutang;
    @Column(nullable = false , name = "updated_at")
    public Date updatedAt;


}
