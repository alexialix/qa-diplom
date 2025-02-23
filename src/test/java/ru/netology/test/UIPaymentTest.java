package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.netology.data.CardInfo;
import ru.netology.helper.DataHelper;
import ru.netology.pages.HomePage;
import ru.netology.pages.PaymentPage;


import static com.codeborne.selenide.Selenide.open;
import static ru.netology.helper.DataHelper.*;


public class UIPaymentTest {

    CardInfo data;
    HomePage home;

    @BeforeEach
    public void prepare() {
        open("http://localhost:8080/");
        data = getApprovedCard();
        home = new HomePage();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Отправка пустой формы")
    public void testEmptyInfo() {
        PaymentPage payment = home.payment();
        payment.verifyAllFieldsEmpty();
    }

    @Test
    @DisplayName("Отправка формы с валидными данными")
    public void testValidInfo() {
        PaymentPage payment = home.payment();
        payment.enterValidCardDetails(getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.verifyApprovedCardDetails();
    }

    // поле Номер карты

    @Test
    @DisplayName("Отправка формы с картой со статусом DECLINED")
    public void testDeclinedCard() {
        PaymentPage payment = home.payment();
        payment.enterValidCardDetails(getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.verifyDeclinedCardDetails();
    }


    @Test
    @DisplayName("Отправка формы с коротким номером карты")
    public void testShortCardNumber() {
        PaymentPage payment = home.payment();
        payment.enterValidCardDetails(getInvalidCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.verifyInvalidCardNumber();
    }

    // поле Месяц

    @Test
    @DisplayName("Отправка формы с невалидным месяцем 00")
    public void testMonthInvalidValueZero() {
        PaymentPage payment = home.payment();
        payment.enterValidCardDetails(data.getNumber(), "00", data.getYear(), data.getName(), data.getCvc());
        payment.verifyInvalidFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы с невалидным месяцем 5")
    public void testMonthInvalidValueFive() {
        PaymentPage payment = home.payment();
        payment.enterValidCardDetails(data.getNumber(), "5", data.getYear(), data.getName(), data.getCvc());
        payment.verifyInvalidFieldMonth();
    }

    @Test
    @DisplayName("Отправка формы с невалидным месяцем 13")
    public void testMonthInvalidValueThirteen() {
        PaymentPage payment = home.payment();
        payment.enterValidCardDetails(data.getNumber(), "13", data.getYear(), data.getName(), data.getCvc());
        payment.verifyInvalidFieldMonthFormat();
    }

    @Test
    @DisplayName("Отправка формы с невалидным месяцем 20")
    public void testMonthInvalidValueTwenty() {
        PaymentPage payment = home.payment();
        payment.enterValidCardDetails(data.getNumber(), "20", data.getYear(), data.getName(), data.getCvc());
        payment.verifyInvalidFieldMonthFormat();
    }

    // поле Год

    @Test
    @DisplayName("Отправка формы с невалидным полем Год")
    public void testWrongYear() {
        PaymentPage payment = home.payment();
        payment.enterValidCardDetails(getApprovedCardNumber(), data.getMonth(), getInvalidYear(), data.getName(), data.getCvc());
        payment.verifyInvalidFieldYear();
    }

    // поле Владелец
    @Test
    @DisplayName("Отправка формы с длинным значением в поле Владелец")
    public void testWithLongOwnerInCredit() {
        PaymentPage payment = home.payment();
        payment.enterValidCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), DataHelper.getLongOwnerName(), data.getCvc());
        payment.verifyInvalidOwnerField();
    }

    @Test
    @DisplayName("Отправка формы с коротким значением в поле Владелец")
    public void testWithShortOwnerInCredit() {
        PaymentPage payment = home.payment();
        payment.enterValidCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), "J", data.getCvc());
        payment.verifyInvalidOwnerField();
    }
    @Test
    @DisplayName("Отправка формы с тире в поле Владелец")
    public void testOwnerWithDash() {
        PaymentPage payment = home.payment();
        String name = "John Doe-Doe";
        payment.enterValidCardDetails(getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        payment.verifyInvalidOwnerField();
    }

    @Test
    @DisplayName("Отправка формы с полем Владелец на кириллице")
    public void testOwnerCyrillicSymbol() {
        PaymentPage payment = home.payment();
        String name = "Джон Доу";
        payment.enterValidCardDetails(getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        payment.verifyInvalidOwnerField();
    }

    @Test
    @DisplayName("Отправка формы с цифрами в поле Владелец")
    public void testOwnerWithNumbers() {
        PaymentPage payment = home.payment();
        String name = "John333";
        payment.enterValidCardDetails(getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        payment.verifyInvalidOwnerField();
    }

    @Test
    @DisplayName("Отправка формы со спец.символами в поле Владелец")
    public void testOwnerWithSpecSymbols() {
        PaymentPage payment = home.payment();
        String name = "№;%:?*()";
        payment.enterValidCardDetails(getApprovedCardNumber(), data.getMonth(), data.getYear(), name, data.getCvc());
        payment.verifyInvalidOwnerField();
    }

    @Test
    @DisplayName("Отправка формы с невалидным CVC")
    public void testInvalidCVCField() {
        PaymentPage payment = home.payment();
        payment.enterValidCardDetails(data.getNumber(), data.getMonth(), data.getYear(), data.getName(), DataHelper.getInvalidFormatCVV());
        payment.verifyInvalidСVCField();
    }

    @ParameterizedTest
    @ValueSource(strings = {"cardNumber", "month", "year", "owner", "cvc"})
    @DisplayName("Отправка формы с пустым полем {0}")
    public void testEmptyFields(String field) {
        PaymentPage payment = home.payment();
        switch (field) {
            case "cardNumber":
                payment.enterValidCardDetails(null, data.getMonth(), data.getYear(), data.getName(), data.getCvc());
                payment.verifyEmptyCardNumber();
                break;
            case "month":
                payment.enterValidCardDetails(getApprovedCardNumber(), null, data.getYear(), data.getName(), data.getCvc());
                payment.verifyEmptyFieldMonth();
                break;
            case "year":
                payment.enterValidCardDetails(getApprovedCardNumber(), data.getMonth(), null, data.getName(), data.getCvc());
                payment.verifyEmptyFieldYear();
                break;
            case "owner":
                payment.enterValidCardDetails(getApprovedCardNumber(), data.getMonth(), data.getYear(), null, data.getCvc());
                payment.verifyEmptyOwnerField();
                break;
            case "cvc":
                payment.enterValidCardDetails(getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), null);
                payment.verifyEmptyCVC();
                break;
        }
    }


}