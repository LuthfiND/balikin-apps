package org.balikin.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

@Entity

@Table(name = "auth")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Auth extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @Column(nullable = false)
    public String username;
    @Column(nullable = false)
    public String password;
    @Column(nullable = false, unique = true)
    public String email;
    @Column(name = "image_url")
    public String imageUrl;

}
