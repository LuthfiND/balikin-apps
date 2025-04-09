package org.balikin.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.balikin.entity.Auth;

import java.util.Optional;

@ApplicationScoped
public class AuthRepository implements PanacheRepositoryBase<Auth,String> {
 public Object register (Auth user) {
     persist(user);
     return user;
 }
 public boolean isEmailExist (String email) {
     return find("email", email).firstResultOptional().isPresent();
 }

   public Optional<Auth> FindByEmail(String email) {
    return find("email", email).firstResultOptional();
   }

}
