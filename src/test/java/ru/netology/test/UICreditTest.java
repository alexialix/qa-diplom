package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.netology.data.CardInfo;
import ru.netology.helper.DataHelper;
import ru.netology.pages.CreditPage;
import ru.netology.pages.HomePage;

import static com.codeborne.selenide.Selenide.*;

public class UICreditTest {
    private CardInfo data;
    private HomePage home;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void prepare() {
        open("http://localhost:8080/");
        data = DataHelper.getApprovedCard();
        home = new HomePage();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Отправка пустой формы Кредит")
    public void testEmptyCreditData() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.verifyAllFieldsAreEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"cardNumber", "month", "year", "owner", "cvc"})
    @DisplayName("Отправка формы с пустым полем")
    public void testEmptyFields(String field) {
        CreditPage creditRequest = home.creditPayment();

        switch (field) {
            case "cardNumber":
                creditRequest.enterCardDetails(null, data.getMonth(), data.getYear(), data.getName(), data.getCvc());
                creditRequest.verifyErrorVisibility("cardNumber");
                break;
            case "month":
                creditRequest.enterCardDetails(data.getNumber(), "", data.getYear(), data.getName(), data.getCvc());
                creditRequest.verifyErrorVisibility("month");
                break;
            case "year":
                creditRequest.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), null, data.getName(), data.getCvc());
                creditRequest.verifyErrorVisibility("year");
                break;
            case "owner":
                creditRequest.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), null, data.getCvc());
                creditRequest.verifyErrorVisibility("owner");
                break;
            case "cvc":
                creditRequest.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), null);
                creditRequest.verifyErrorVisibility("cvc");
                break;
        }
    }

    @Test
    @DisplayName("Отправка формы Кредит с валидными данными")
    public void testValidCreditData() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.verifyApprovedCardDetails();
    }

    // поле Номер карты

    @Test
    @DisplayName("Отправка формы Кредит с картой со статусом DECLINED")
    public void testInvalidCreditData() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.enterCardDetails(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.verifyDeclinedCardDetails();
    }

    @Test
    @DisplayName("Отправка формы Кредит с коротким номером карты")
    public void testShortCardNumberInCredit() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.enterCardDetails(DataHelper.getInvalidCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        creditRequest.verifyInvalidCardNumber();
    }

    // поле Месяц

    @Test
    @DisplayName("Отправка формы с невалидным месяцем 00")
    public void testMonthInvalidValueZero() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.enterCardDetails(data.getNumber(), "00", data.getYear(), data.getName(), data.getCvc());
        creditRequest.verifyInvalidFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы с невалидным месяцем 5")
    public void testMonthInvalidValueFive() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.enterCardDetails(data.getNumber(), "5", data.getYear(), data.getName(), data.getCvc());
        creditRequest.verifyInvalidFieldMonthFormat();
    }

    @Test
    @DisplayName("Отправка формы с невалидным месяцем 13")
    public void testMonthInvalidValueThirteen() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.enterCardDetails(data.getNumber(), "13", data.getYear(), data.getName(), data.getCvc());
        creditRequest.verifyInvalidFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы с невалидным месяцем 20")
    public void testMonthInvalidValueTwenty() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.enterCardDetails(data.getNumber(), "20", data.getYear(), data.getName(), data.getCvc());
        creditRequest.verifyInvalidFieldMonth();
    }

    // поле Год

    @Test
    @DisplayName("Отправка формы Кредит с невалидным полем Год")
    public void testWrongYearInCredit() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), DataHelper.getInvalidYear(), data.getName(), data.getCvc());
        creditRequest.verifyInvalidFieldYear();
    }

    // поле Владелец

    @Test
    @DisplayName("Отправка формы Кредит с тире в поле Владелец")
    public void testOwnerWithDashInCredit() {
        CreditPage creditRequest = home.creditPayment();
        String name = "John Doe-Doe";
        creditRequest.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        creditRequest.verifyInvalidOwnerFormat();
    }

    @Test
    @DisplayName("Отправка формы Кредит с кириллицей в поле Владелец")
    public void testOwnerCyrillikNameInCredit() {
        CreditPage creditRequest = home.creditPayment();
        String name = "Джон Доу";
        creditRequest.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        creditRequest.verifyInvalidOwnerFormat();
    }

    @Test
    @DisplayName("Отправка формы Кредит с цифрами в поле Владелец")
    public void testOwnerWithNumberInCredit() {
        CreditPage creditRequest = home.creditPayment();
        String name = "John333";
        creditRequest.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        creditRequest.verifyInvalidOwnerFormat();
    }

    @Test
    @DisplayName("Отправка формы Кредит со спец.символами в поле Владелец")
    public void testOwnerWithSpecSymbolsInCredit() {
        CreditPage creditRequest = home.creditPayment();
        String name = "№;%:?*()";
        creditRequest.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        creditRequest.verifyInvalidOwnerFormat();
    }

    @Test
    @DisplayName("Отправка формы Кредит с длинным значением в поле Владелец")
    public void testWithLongOwnerInCredit() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), DataHelper.getLongOwnerName(), data.getCvc());
        creditRequest.verifyInvalidOwnerFormat();
    }

    @Test
    @DisplayName("Отправка формы Кредит с коротким значением в поле Владелец")
    public void testWithShortOwnerInCredit() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), "J", data.getCvc());
        creditRequest.verifyInvalidOwnerFormat();
    }

    // поле CVC
    @Test
    @DisplayName("Отправка формы Кредит с невалидным CVC")
    public void testInvalidCVCFieldInCredit() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.enterCardDetails(data.getNumber(), data.getMonth(), data.getYear(), data.getName(), DataHelper.getInvalidFormatCVV());
        creditRequest.verifyInvalidCvcFormat();
    }
}
