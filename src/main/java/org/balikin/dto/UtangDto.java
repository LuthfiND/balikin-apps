package org.balikin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.balikin.entity.Utang;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UtangDto {
    private Long id;
    private String name_utang;
    private Utang.TransactionType transactionType;
    private Date transactionDate;
    private String description;
    private Double amount;
    private String imageUrlUser;
    private String receivedEmail;

}
