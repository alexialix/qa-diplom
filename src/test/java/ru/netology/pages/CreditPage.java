package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.Map;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CreditPage {
    private static final Duration NOTIFICATION_WAIT_TIME = Duration.ofSeconds(15);
    // Поля для ввода
    private SelenideElement cardNumberInput = $("input[type=\"text\"][placeholder=\"0000 0000 0000 0000\"]");
    private SelenideElement expirationMonthInput = $("input[type=\"text\"][placeholder=\"08\"]");
    private SelenideElement expirationYearInput = $("input[type=\"text\"][placeholder=\"22\"]");
    private SelenideElement ownerInput = $$(".input").find(exactText("Владелец")).$(".input__control");
    private SelenideElement cvcInput = $("input[type=\"text\"][placeholder=\"999\"]");
    // Кнопки
    private SelenideElement submitButton = $$(".button").find(exactText("Продолжить"));
    private SelenideElement sendButton = $x("//*[@id=\"root\"]/div/form/fieldset/div[4]/button/span/span/span");
    private SelenideElement closeApprovedNotificationButton = $x("//*[@id=\"root\"]/div/div[2]/button/span/span");
    private SelenideElement closeDeclinedNotificationButton = $x("//*[@id=\"root\"]/div/div[3]/button");
    // Ошибки
    private SelenideElement errorCardNumber = $x("//*[@id=\"root\"]/div/form/fieldset/div[1]/span/span/span[3]");
    private SelenideElement errorExpirationMonth = $x("//*[@id=\"root\"]/div/form/fieldset/div[2]/span/span[1]/span/span/span[3]");
    private SelenideElement errorExpirationYear = $x("//*[@id=\"root\"]/div/form/fieldset/div[2]/span/span[2]/span/span/span[3]");
    private SelenideElement errorOwner = $x("//*[@id=\"root\"]/div/form/fieldset/div[3]/span/span[1]/span/span/span[3]");
    private SelenideElement errorCvc = $x("//*[@id=\"root\"]/div/form/fieldset/div[3]/span/span[2]/span/span/span[3]");
    // Уведомления
    private SelenideElement notificationPending = $x("//*[text()=\"Отправляем запрос в Банк...\"]");
    private SelenideElement notificationSuccess = $x("//div[@class = \"notification notification_visible notification_status_ok notification_has-closer notification_stick-to_right notification_theme_alfa-on-white\"]//div[@class = \"notification__content\"]");
    private SelenideElement notificationFailure = $x("//*[@id=\"root\"]/div/div[3]/div[3]");

    private void verifyError(SelenideElement errorElement, String expectedErrorText) {
        submitButton.shouldBe(visible, Duration.ofSeconds(15));
        submitButton.click();

        if (errorElement.exists() && errorElement.isDisplayed()) {
            errorElement.shouldBe(Condition.visible).shouldHave(Condition.text(expectedErrorText));
        } else {
            Assertions.fail("Ожидалось уведомление: '" + expectedErrorText + "', но оно не было отображено.");
        }

        notificationSuccess.shouldNotBe(visible);
    }


    public void verifyErrorVisibility(String fieldWithError) {
        submitButton.shouldBe(visible, Duration.ofSeconds(15));
        submitButton.click();

        Map<String, SelenideElement> errorFields = Map.of(
                "cardNumber", errorCardNumber,
                "month", errorExpirationMonth,
                "year", errorExpirationYear,
                "owner", errorOwner,
                "cvc", errorCvc
        );

        errorFields.forEach((field, element) ->
                element.shouldBe(field.equals(fieldWithError) ? visible : hidden)
        );

        notificationSuccess.shouldNotBe(visible);
    }

    public void enterCardDetails(String number, String month, String year, String owner, String cvc) {
        cardNumberInput.setValue(number);
        expirationMonthInput.setValue(month);
        expirationYearInput.setValue(year);
        ownerInput.setValue(owner);
        cvcInput.setValue(cvc);
        submitButton.click();
    }

    public void verifyApprovedCardDetails() {
        sendButton.should(Condition.visible);
        notificationPending.shouldHave(text("Отправляем запрос в Банк..."), NOTIFICATION_WAIT_TIME);
        notificationSuccess.shouldHave(text("Операция одобрена Банком."), NOTIFICATION_WAIT_TIME);
        closeNotification(closeApprovedNotificationButton);
    }


    public void verifyDeclinedCardDetails() {
        sendButton.should(Condition.visible);
        notificationPending.should(text("Отправляем запрос в Банк..."), NOTIFICATION_WAIT_TIME);
        notificationFailure.should(text("Ошибка! Банк отказал в проведении операции."));
        closeNotification(closeDeclinedNotificationButton);
    }

    public void verifyAllFieldsAreEmpty() {
        verifyError(errorCardNumber, "Неверный формат");
        verifyError(errorExpirationMonth, "Неверный формат");
        verifyError(errorExpirationYear, "Неверный формат");
        verifyError(errorOwner, "Поле обязательно для заполнения");
        verifyError(errorCvc, "Неверный формат");

        notificationSuccess.shouldNotBe(visible);
    }

    public void verifyInvalidCardNumber() {
        verifyError(errorCardNumber, "Неверный формат");
    }

    public void verifyInvalidOwnerFormat() {
        verifyError(errorOwner, "Неверный формат данных");
    }

    public void verifyInvalidFieldMonth() {
        verifyError(errorExpirationMonth, "Неверно указан срок действия карты");
    }

    public void verifyInvalidFieldMonthFormat() {
        verifyError(errorExpirationMonth, "Неверный формат");
    }

    public void verifyInvalidFieldYear() {
        verifyError(errorExpirationYear, "Истёк срок действия карты");
    }

    public void verifyInvalidCvcFormat() {
        verifyError(errorCvc, "Неверный формат");
    }

    private void closeNotification(SelenideElement closeButton) {
        closeButton.click();
    }
}
