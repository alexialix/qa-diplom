package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CreditPage {
    private SelenideElement cardNumberInput = $("input[type=\"text\"][placeholder=\"0000 0000 0000 0000\"]");
    private SelenideElement expirationMonthInput = $("input[type=\"text\"][placeholder=\"08\"]");
    private SelenideElement expirationYearInput = $("input[type=\"text\"][placeholder=\"22\"]");
    private SelenideElement ownerInput = $$(".input").find(exactText("Владелец")).$(".input__control");
    private SelenideElement cvcInput = $("input[type=\"text\"][placeholder=\"999\"]");
    private SelenideElement submitButton = $$(".button").find(exactText("Продолжить"));
    private SelenideElement errorCardNumber = $x("//*[@id=\"root\"]/div/form/fieldset/div[1]/span/span/span[3]");
    private SelenideElement errorExpirationMonth = $x("//*[@id=\"root\"]/div/form/fieldset/div[2]/span/span[1]/span/span/span[3]");
    private SelenideElement errorExpirationYear = $x("//*[@id=\"root\"]/div/form/fieldset/div[2]/span/span[2]/span/span/span[3]");
    private SelenideElement errorOwner = $x("//*[@id=\"root\"]/div/form/fieldset/div[3]/span/span[1]/span/span/span[3]");
    private SelenideElement errorCvc = $x("//*[@id=\"root\"]/div/form/fieldset/div[3]/span/span[2]/span/span/span[3]");
    private SelenideElement sendButton = $x("//*[@id=\"root\"]/div/form/fieldset/div[4]/button/span/span/span");
    private SelenideElement notificationPending = $x("//*[text()=\"Отправляем запрос в Банк...\"]");
    private SelenideElement notificationSuccess = $x("//div[@class = \"notification notification_visible notification_status_ok notification_has-closer notification_stick-to_right notification_theme_alfa-on-white\"]//div[@class = \"notification__content\"]");
    private SelenideElement closeApprovedNotificationButton = $x("//*[@id=\"root\"]/div/div[2]/button/span/span");
    private SelenideElement notificationFailure = $x("//*[@id=\"root\"]/div/div[3]/div[3]");
    private SelenideElement closeDeclinedNotificationButton = $x("//*[@id=\"root\"]/div/div[3]/button");

    public void enterCardDetails(String cardNumber, String month, String year, String ownerName, String cvc) {
        cardNumberInput.val(cardNumber);
        expirationMonthInput.val(month);
        expirationYearInput.val(year);
        ownerInput.val(ownerName);
        cvcInput.val(cvc);
        submitButton.click();
    }

    public void verifyAcceptedCardDetails() {
        validateNoErrors();
        sendButton.should(visible);
        notificationPending.should(text("Отправляем запрос в Банк..."));
        notificationSuccess.should(text("Операция одобрена Банком."), Duration.ofSeconds(15));
        closeApprovedNotificationButton.click();
    }

    public void verifyDeclinedCardDetails() {
        validateNoErrors();
        sendButton.should(visible);
        notificationPending.should(text("Отправляем запрос в Банк..."));
        notificationFailure.should(text("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(15));
        closeDeclinedNotificationButton.click();
    }

    public void verifyAllFieldsAreEmpty() {
        submitButton.click();
        errorCardNumber.should(visible);
        errorExpirationMonth.should(visible);
        errorExpirationYear.should(visible);
        errorOwner.should(visible);
        errorCvc.should(visible);
    }

    public void verifyInvalidExpirationMonth() {
        validateNoErrors();
        errorExpirationMonth.should(visible).should(text("Неверно указан срок действия карты"));
    }

    public void verifyInvalidExpirationYear() {
        validateNoErrors();
        errorExpirationYear.should(visible).should(text("Истёк срок действия карты"));
    }

    public void verifyEmptyExpirationMonth() {
        validateNoErrors();
        errorExpirationMonth.should(visible).should(text("Неверный формат"));
    }

    public void verifyEmptyExpirationYear() {
        validateNoErrors();
        errorExpirationYear.should(visible).should(text("Неверный формат"));
    }

    public void verifyEmptyOwnerField() {
        validateNoErrors();
        errorOwner.should(visible).should(text("Поле обязательно для заполнения"));
    }

    public void verifyInvalidCvcFormat() {
        validateNoErrors();
        errorCvc.should(visible).should(text("Неверный формат"));
    }

    public void verifyEmptyCardNumber() {
        errorCardNumber.should(visible).should(text("Неверный формат"));
    }

    public void verifyInvalidCardNumber() {
        errorCardNumber.should(visible).should(text("Неверный формат"));
    }

    public void verifyInvalidOwnerFormat() {
        validateNoErrors();
        errorOwner.should(visible).should(text("Неверный формат данных"));
    }

    private void validateNoErrors() {
        errorCardNumber.should(hidden);
        errorExpirationMonth.should(hidden);
        errorExpirationYear.should(hidden);
        errorOwner.should(hidden);
        errorCvc.should(hidden);
    }
}
