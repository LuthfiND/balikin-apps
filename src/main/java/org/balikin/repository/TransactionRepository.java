package org.balikin.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.balikin.model.Transaction;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class TransactionRepository implements PanacheRepository<Transaction> {

    public List<Transaction> findByTypeAndDateBetween(String type, LocalDate startDate, LocalDate endDate) {
        return find("type = ?1 and date between ?2 and ?3", type, startDate, endDate).list();
    }

    public List<Transaction> findByType(String type) {
        return find("type", type).list();
    }

    public List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return find("date between ?1 and ?2", startDate, endDate).list();
    }
}