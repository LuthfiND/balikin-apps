package org.balikin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.balikin.entity.Utang;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UtangRequestDto {
    private String nameUtang;
    private Utang.TransactionType transactionType;
    private Double amount;
    private String description;
    private Date transactionDate;
    private String receivedEmail;

}
