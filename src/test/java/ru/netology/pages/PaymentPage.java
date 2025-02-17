package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class PaymentPage {
    // Поля для ввода
    private SelenideElement cardNumberField = $("input[type=\"text\"][placeholder=\"0000 0000 0000 0000\"]");
    private SelenideElement expiryMonthField = $("input[type=\"text\"][placeholder=\"08\"]");
    private SelenideElement expiryYearField = $("input[type=\"text\"][placeholder=\"22\"]");
    private SelenideElement cardOwnerField = $$(".input").find(exactText("Владелец")).$(".input__control");
    private SelenideElement cvcField = $("input[type=\"text\"][placeholder=\"999\"]");
    // Кнопки
    private SelenideElement continueButton = $$(".button").find(exactText("Продолжить"));
    private SelenideElement sendButton = $x("//*[@id=\"root\"]/div/form/fieldset/div[4]/button/span/span/span");
    private SelenideElement closeApprovedReportButton = $x("//*[@id=\"root\"]/div/div[2]/button/span/span");
    private SelenideElement closeDeclinedButton = $x("//*[@id=\"root\"]/div/div[3]/button");
    // Сообщения об ошибках
    private SelenideElement cardNumberError = $x("//*[@id=\"root\"]/div/form/fieldset/div[1]/span/span/span[3]");
    private SelenideElement expiryMonthError = $x("//*[@id=\"root\"]/div/form/fieldset/div[2]/span/span[1]/span/span/span[3]");
    private SelenideElement expiryYearError = $x("//*[@id=\"root\"]/div/form/fieldset/div[2]/span/span[2]/span/span/span[3]");
    private SelenideElement ownerError = $x("//*[@id=\"root\"]/div/form/fieldset/div[3]/span/span[1]/span/span/span[3]");
    private SelenideElement cvcError = $x("//*[@id=\"root\"]/div/form/fieldset/div[3]/span/span[2]/span/span/span[3]");
    private SelenideElement inputInvalidMessage = $(".input__sub");
    // Уведомления
    private SelenideElement sendingNotification = $x("//*[text()=\"Отправляем запрос в Банк...\"]");
    private SelenideElement approvalNotification = $x("//div[@class = \"notification notification_visible notification_status_ok notification_has-closer notification_stick-to_right notification_theme_alfa-on-white\"]//div[@class = \"notification__content\"]");
    private SelenideElement declinedNotification = $x("//*[@id=\"root\"]/div/div[3]/div[3]");

    public void enterValidCardDetails(String cardNumber, String month, String year, String ownerName, String cvc) {
        cardNumberField.shouldBe(visible, Duration.ofSeconds(5));
        expiryMonthField.shouldBe(visible, Duration.ofSeconds(5));
        expiryYearField.shouldBe(visible, Duration.ofSeconds(5));
        cardOwnerField.shouldBe(visible, Duration.ofSeconds(5));
        cvcField.shouldBe(visible, Duration.ofSeconds(5));

        cardNumberField.val(cardNumber);
        expiryMonthField.val(month);
        expiryYearField.val(year);
        cardOwnerField.val(ownerName);
        cvcField.val(cvc);

        continueButton.shouldBe(visible, Duration.ofSeconds(5));
        continueButton.click();
    }

    public void verifyAllFieldsEmpty() {
        cardNumberError.shouldBe(visible);
        expiryMonthError.shouldBe(visible);
        expiryYearError.shouldBe(visible);
        ownerError.shouldBe(visible);
        cvcError.shouldBe(visible);
    }

    public void verifyApprovedCardDetails() {
        cardNumberError.shouldBe(hidden);
        expiryMonthError.shouldBe(hidden);
        expiryYearError.shouldBe(hidden);
        ownerError.shouldBe(hidden);
        cvcError.shouldBe(hidden);
        sendingNotification.shouldBe(visible, Duration.ofSeconds(15));
        approvalNotification.shouldHave(text("Операция одобрена Банком."));
    }

    public void verifyDeclinedCardDetails() {
        cardNumberError.shouldBe(hidden);
        expiryMonthError.shouldBe(hidden);
        expiryYearError.shouldBe(hidden);
        ownerError.shouldBe(hidden);
        cvcError.shouldBe(hidden);
        sendingNotification.shouldBe(visible, Duration.ofSeconds(15));
        declinedNotification.shouldHave(text("Ошибка! Банк отказал в проведении операции."));
    }

    public void verifyEmptyCardNumber() {
        cardNumberError.shouldBe(visible);
        cardNumberError.shouldHave(text("Неверный формат"));
        expiryMonthError.shouldBe(hidden);
        expiryYearError.shouldBe(hidden);
        ownerError.shouldBe(hidden);
        cvcError.shouldBe(hidden);
    }

    public void verifyEmptyFieldMonth() {
        cardNumberError.shouldBe(hidden);
        expiryMonthError.shouldBe(visible);
        expiryMonthError.shouldHave(text("Неверный формат"));
        expiryYearError.shouldBe(hidden);
        ownerError.shouldBe(hidden);
        cvcError.shouldBe(hidden);
    }

    public void verifyEmptyFieldYear() {
        cardNumberError.shouldBe(hidden);
        expiryMonthError.shouldBe(hidden);
        expiryYearError.shouldBe(visible);
        expiryYearError.shouldHave(text("Неверный формат"));
        ownerError.shouldBe(hidden);
        cvcError.shouldBe(hidden);
    }

    public void verifyEmptyOwnerField() {
        cardNumberError.shouldBe(hidden);
        expiryMonthError.shouldBe(hidden);
        expiryYearError.shouldBe(hidden);
        ownerError.shouldBe(visible);
        ownerError.shouldHave(text("Поле обязательно для заполнения"));
        cvcError.shouldBe(hidden);
    }

    public void verifyEmptyCVC() {
        cardNumberError.shouldBe(hidden);
        expiryMonthError.shouldBe(hidden);
        expiryYearError.shouldBe(hidden);
        ownerError.shouldBe(hidden);
        cvcError.shouldBe(visible);
        cvcError.shouldHave(text("Неверный формат"));
    }

    public void verifyInvalidCardNumber() {
        cardNumberError.shouldBe(visible);
        cardNumberError.shouldHave(text("Неверный формат"));
        expiryMonthError.shouldBe(hidden);
        expiryYearError.shouldBe(hidden);
        ownerError.shouldBe(hidden);
        cvcError.shouldBe(hidden);
    }

    public void verifyInvalidFieldMonth() {
        cardNumberError.shouldBe(hidden);
        expiryMonthError.shouldBe(visible);
        expiryMonthError.shouldHave(text("Неверный формат"));
        expiryYearError.shouldBe(hidden);
        ownerError.shouldBe(hidden);
        cvcError.shouldBe(hidden);
    }

    public void verifyInvalidFieldMonthFormat() {
        cardNumberError.shouldBe(hidden);
        expiryMonthError.shouldBe(visible);
        expiryMonthError.shouldHave(text("Неверно указан срок действия карты"));
        expiryYearError.shouldBe(hidden);
        ownerError.shouldBe(hidden);
        cvcError.shouldBe(hidden);
    }

    public void verifyInvalidFieldYear() {
        cardNumberError.shouldBe(hidden);
        expiryMonthError.shouldBe(hidden);
        expiryYearError.shouldBe(visible);
        expiryYearError.shouldHave(text("Истёк срок действия карты"));
        ownerError.shouldBe(hidden);
        cvcError.shouldBe(hidden);
    }

    public void verifyInvalidOwnerField() {
        cardNumberError.shouldBe(hidden);
        expiryMonthError.shouldBe(hidden);
        expiryYearError.shouldBe(hidden);
        ownerError.shouldBe(visible);
        ownerError.shouldHave(text("Неверный формат данных"));
        cvcError.shouldBe(hidden);
    }
}
