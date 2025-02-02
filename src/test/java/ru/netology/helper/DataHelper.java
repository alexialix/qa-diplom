package ru.netology.helper;

import com.github.javafaker.Faker;
import ru.netology.data.CardInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class DataHelper {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private DataHelper() {
    }

    public static String getValidFullName() {
        return faker.name().fullName();
    }

    public static String getMonth() {
        return String.format("%02d", random.nextInt(12) + 1);
    }

    public static String getInvalidMonth() {
        return String.format("%02d", random.nextInt(12) + 13);
    }


    public static String getInvalidFormatMonth() {
        return String.format("%d", random.nextInt(9) + 1);
    }

    public static String getYear() {
        return LocalDate.now().plusYears(5).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getInvalidYear() {
        int currentYear = LocalDate.now().getYear() % 100;
        boolean isPastYear = random.nextBoolean();

        if (isPastYear) {
            return String.format("%02d", random.nextInt(currentYear - 1) + 1);
        } else {
            return String.format("%02d", random.nextInt(5) + 1);
        }
    }

    public static String getCVCCode() {
        return String.format("%03d", random.nextInt(1000));
    }

    public static String getInvalidFormatCVV() {
        return String.format("%03d", random.nextInt(1000));
    }

    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getShortCardNumber() {
        return "4444 4444 44";
    }

    public static CardInfo getApprovedCard() {
        return new CardInfo(getApprovedCardNumber(), getMonth(), getYear(), getValidFullName(), getCVCCode());
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo(getDeclinedCardNumber(), getMonth(), getYear(), getValidFullName(), getCVCCode());
    }
}
