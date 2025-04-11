package org.balikin.service.transaction;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.balikin.dto.TransactionDto;
import org.balikin.model.Transaction;
import org.balikin.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransactionService {

    @Inject
    TransactionRepository transactionRepository;

    public List<TransactionDto> filterTransactions(String type, String sort, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions;

        // Fetch data based on filters
        if (type != null && startDate != null && endDate != null) {
            transactions = transactionRepository.findByTypeAndDateBetween(type, startDate, endDate);
        } else if (type != null) {
            transactions = transactionRepository.findByType(type);
        } else if (startDate != null && endDate != null) {
            transactions = transactionRepository.findByDateBetween(startDate, endDate);
        } else {
            transactions = transactionRepository.listAll();
        }

        // Map to DTO
        List<TransactionDto> transactionDtos = transactions.stream()
                .map(t -> new TransactionDto(t.getId(), t.getType(), t.getAmount(), t.getDate()))
                .collect(Collectors.toList());

        // Sort by amount
        Comparator<TransactionDto> comparator = Comparator.comparing(TransactionDto::getAmount);
        if ("desc".equalsIgnoreCase(sort)) {
            comparator = comparator.reversed();
        }
        return transactionDtos.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}