package ru.netology.data;

import lombok.Value;

@Value
public class CardInfo {
    String number;
    String month;
    String year;
    String name;
    String cvc;
}
