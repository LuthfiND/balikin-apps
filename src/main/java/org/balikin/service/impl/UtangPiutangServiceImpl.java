package org.balikin.service.impl;

import io.quarkus.security.UnauthorizedException;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Request;
import lombok.extern.slf4j.Slf4j;
import org.balikin.dto.TotalUtangPiutangDto;
import org.balikin.dto.UtangDto;
import org.balikin.dto.UtangRequestDto;
import org.balikin.entity.Auth;
import org.balikin.entity.FriendRequest;
import org.balikin.entity.Utang;
import org.balikin.repository.AuthRepository;
import org.balikin.repository.FriendRequestRepository;
import org.balikin.repository.UtangPiutangRepository;
import org.balikin.service.utang.UtangPiutangService;
import org.balikin.util.FormatRupiahUtil;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@ApplicationScoped
public class UtangPiutangServiceImpl implements UtangPiutangService {
    @Inject
    UtangPiutangRepository utangPiutangRepository;
    @Inject
    AuthRepository authRepository;
    @Inject
    FriendRequestRepository friendRequestRepository;
    @Inject
    SecurityIdentity securityIdentity;
    @Inject
    FormatRupiahUtil formatRupiahUtil;

    @Transactional
    public Optional<Utang> saveDataUtang(UtangRequestDto utangRequestDto, String id) throws Exception {
        try {
            Optional<Auth> authOpt = authRepository.find("id", Integer.parseInt(id)).firstResultOptional();
            if (authOpt.isEmpty()) {
                throw new Exception("User Tidak Ditemukan");
            }
            Optional<Auth> receivedEmailOpt = authRepository.find("email", utangRequestDto.getReceivedEmail()).firstResultOptional();
            if (receivedEmailOpt.isEmpty()) {
                throw new Exception("User Receiver Tidak Ditemukan");
            }
            Optional<FriendRequest> requestFriend = friendRequestRepository.find("to.id",receivedEmailOpt.get().getId()).firstResultOptional();
            Auth auth = authOpt.get();
            Auth receiver = receivedEmailOpt.get();

             if (!friendRequestRepository.isFriendShip(auth, receiver)) {
                 throw new Exception("Anda Belum Berteman Dengan Si " +  requestFriend.get().getTo().getEmail());
             }
            if (auth.getEmail().equals(receiver.getEmail())) {
                throw new Exception("Mohon Maaf, Tidak Bisa Mengirim ke Email Anda Sendiri");
            }

            Utang.TransactionType transactionType;
            try {
                transactionType = Utang.TransactionType.valueOf(String.valueOf(utangRequestDto.getTransactionType()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Transaction Type tidak valid. Harus 'UTANG' atau 'PIUTANG'.");
            }

            Optional<Utang> utangSenderOpt = utangPiutangRepository.find(
                    "user.id = ?1 AND receiver.id = ?2 AND transactionType = ?3",
                    auth.getId(), receiver.getId(), transactionType
            ).firstResultOptional();

            Optional<Utang> utangReceiverOpt = utangPiutangRepository.find(
                    "user.id = ?1 AND receiver.id = ?2 AND transactionType = ?3",
                    receiver.getId(), auth.getId(),
                    transactionType == Utang.TransactionType.UTANG ? Utang.TransactionType.PIUTANG : Utang.TransactionType.UTANG
            ).firstResultOptional();


            if (utangSenderOpt.isPresent() && utangReceiverOpt.isPresent()) {
                Utang utangSender = utangSenderOpt.get();
                Utang utangReceiver = utangReceiverOpt.get();
                utangSender.setAmount(utangSender.getAmount() + utangRequestDto.getAmount());
                utangReceiver.setAmount(utangReceiver.getAmount() + utangRequestDto.getAmount());
                utangPiutangRepository.persist(utangSender);
                utangPiutangRepository.persist(utangReceiver);
                return Optional.of(utangSender);
            } else {
                Utang utangSender = new Utang();
                utangSender.setUser(auth);
                utangSender.setReceiver(receiver);
                utangSender.setName_utang(utangRequestDto.getNameUtang());
                utangSender.setAmount(utangRequestDto.getAmount());
                utangSender.setTransactionType(transactionType);
                utangSender.setDescription(utangRequestDto.getDescription());
                utangSender.setTransactionDate(utangRequestDto.getTransactionDate());

                Utang utangReceiver = new Utang();
                utangReceiver.setUser(receiver);
                utangReceiver.setReceiver(auth);
                utangReceiver.setName_utang(utangRequestDto.getNameUtang());
                utangReceiver.setAmount(utangRequestDto.getAmount());
                utangReceiver.setTransactionType(transactionType == Utang.TransactionType.UTANG ? Utang.TransactionType.PIUTANG : Utang.TransactionType.UTANG);
                utangReceiver.setDescription(utangRequestDto.getDescription());
                utangReceiver.setTransactionDate(utangRequestDto.getTransactionDate());
                utangPiutangRepository.persist(utangSender);
                utangPiutangRepository.persist(utangReceiver);

                return Optional.of(utangSender);
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    @Override
    public Optional<List<UtangDto>> getAllUtang() {
        String userId = securityIdentity.getPrincipal().getName();
        List<Utang> allUtang = utangPiutangRepository.find(
                "user.id = ?1",
                Integer.parseInt(userId)
        ).list();

        List<UtangDto> utangDtoList = allUtang.stream().map(utang -> new UtangDto(
                utang.getId(),
                utang.getName_utang(),
                utang.getTransactionType(),
                utang.getTransactionDate(),
                utang.getDescription(),
                utang.getAmount(),
                utang.getReceiver().getImageUrl(),
                utang.getReceiver().getEmail()
        )).toList();

        return allUtang.isEmpty() ? Optional.empty() : Optional.of(utangDtoList);
    }

    @Override
    public String balikinUtang(String email, Double paymentAmount, Long id) throws Exception {
        String userId = securityIdentity.getPrincipal().getName();
        Long currentUserId = Long.parseLong(userId);

        Optional<Utang> userLogin = utangPiutangRepository.find("user.id", currentUserId).firstResultOptional();

        Optional<Utang> emailUser = utangPiutangRepository.find("receiver.email", email).firstResultOptional();

        if (emailUser.isEmpty()) {
            return "Tidak Ada Utang Yang Ditemukan Dengan Penerima email " + email;
        }

        Optional<Utang> data = utangPiutangRepository.find("id", id).firstResultOptional();
        if (data.isEmpty()) {
            return "Data utang tidak ditemukan!";
        }

        if (data.get().getTransactionType().equals(Utang.TransactionType.PIUTANG)) {
            return "Transaksi Anda Tidak dapat dibayar karena tipe transaksi anda adalah " + userLogin.get().getTransactionType();
        }
        Optional<Utang> result = utangPiutangRepository.balikanUtang(currentUserId, emailUser.get().getId(), paymentAmount);

        if (result.isEmpty()) {
            return "Uang Berhasil Dilunasi Kepada " + email;
        } else {
            Utang utang = result.get();
            return "Pembayaran Berhasil, Sisa Hutang Anda kepada " + emailUser.get().getReceiver().getUsername()
                    + ": " + formatRupiahUtil.formatRupiah(utang.getAmount());
        }
    }

    @Override
    public UtangDto getUtangById(String id) throws Exception {
        Optional<Utang> userById = utangPiutangRepository.find("id", Integer.parseInt(id)).firstResultOptional();
        if (userById.isEmpty()) {
            throw new BadRequestException("Id Tidak Ditemukan!");
        }
        UtangDto utang = new UtangDto();
        utang.setId(userById.get().getId());
        utang.setName_utang(userById.get().getName_utang());
        utang.setAmount(userById.get().getAmount());
        utang.setDescription(userById.get().getDescription());
        utang.setReceivedEmail(userById.get().getReceiver().getEmail());
        utang.setTransactionType(userById.get().getTransactionType());
        utang.setTransactionDate(userById.get().getTransactionDate());
        return utang;
    }

    @Override
    public TotalUtangPiutangDto getTotalUtangPiutang() throws Exception {
        String userId = securityIdentity.getPrincipal().getName();
        Double totalUtang = utangPiutangRepository
                .find("SELECT SUM(amount) FROM Utang WHERE user.id = ?1 AND transactionType = ?2",
                        Integer.parseInt(userId),
                        Utang.TransactionType.UTANG)
                .project(Double.class)
                .firstResult();
    Double totalPiutang = utangPiutangRepository.find("SELECT SUM(amount) FROM Utang WHERE user.id = ?1 AND transactionType = ?2",
            Integer.parseInt(userId),
            Utang.TransactionType.PIUTANG)
            .project(Double.class)
            .firstResult();
    int totalPiutangInt = totalPiutang != null ? totalPiutang.intValue() : 0;
    int totalUtangInt = totalUtang != null ? totalUtang.intValue() : 0;
    TotalUtangPiutangDto totalUtangPiutangDto = new TotalUtangPiutangDto();
    totalUtangPiutangDto.setTotalUtang(totalUtangInt);
    totalUtangPiutangDto.setTotalPiutang(totalPiutangInt);
    return totalUtangPiutangDto;
    }
}
