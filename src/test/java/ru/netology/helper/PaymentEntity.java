package ru.netology.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {
    private String id;
    private String amount;
    private String created;
    private String status;
    private String transaction_id;
}