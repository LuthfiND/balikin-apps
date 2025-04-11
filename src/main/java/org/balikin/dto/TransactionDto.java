package org.balikin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TransactionDto {
    private int id;
    private String type; // "utang" or "piutang"
    private int amount;
    private LocalDate date;
}