package org.balikin.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import org.balikin.dto.UtangRequestDto;
import org.balikin.entity.Utang;

import java.util.Optional;

@ApplicationScoped
public class UtangPiutangRepository implements PanacheRepositoryBase<Utang, Long> {
    @Transactional
    public Optional<Utang> saveDataUtang(Utang utang) {
        persist(utang);
        return Optional.of(utang);
    }
    @Transactional
    public Optional<Utang> balikanUtang(Long userId, Long receiverId, Double paymentAmount) {
        Optional<Utang> utangOpt = find(
                "user.id = ?1 AND receiver.id = ?2 AND transactionType = ?3",
                userId, receiverId, Utang.TransactionType.UTANG
        ).firstResultOptional();

        Optional<Utang> piutangOpt = find(
                "user.id = ?1 AND receiver.id = ?2 AND transactionType = ?3",
                receiverId, userId, Utang.TransactionType.PIUTANG
        ).firstResultOptional();

        if (utangOpt.isEmpty() || piutangOpt.isEmpty()) {
            return Optional.empty();
        }

        Utang utang = utangOpt.get();
        Utang piutang = piutangOpt.get();

        if (paymentAmount > utang.getAmount()) {
            throw new BadRequestException("Pembayaran Melebihi Hutang Anda!");
        }

        double sisa = utang.getAmount() - paymentAmount;

        if (sisa <= 0) {
            delete(utang);
            delete(piutang);
            return Optional.empty();
        } else {
            utang.setAmount(sisa);
            piutang.setAmount(sisa);

            persist(utang);
            persist(piutang);

            return Optional.of(utang);
        }
    }

}
