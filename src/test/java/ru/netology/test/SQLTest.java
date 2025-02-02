package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.CardInfo;
import ru.netology.helper.DBHelper;
import ru.netology.helper.DataHelper;
import ru.netology.pages.HomePage;
import ru.netology.pages.PaymentPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SQLTest {
    private CardInfo cardInfo;
    private HomePage homePage;
    private PaymentPage paymentPage;

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080/");
        cardInfo = DataHelper.getApprovedCard();
        homePage = new HomePage();
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
    @DisplayName("APPROVED Кредит")
    public void shouldApproveCreditPayment() {
        processPayment(DataHelper.getApprovedCardNumber());
        verifyCreditStatus("APPROVED");
    }

    @Test
    @DisplayName("DECLINED Кредит")
    public void shouldDeclineCreditPayment() {
        processPayment(DataHelper.getDeclinedCardNumber());
        verifyCreditStatus("DECLINED");
    }

    @Test
    @DisplayName("APPROVED")
    public void shouldApprovePayment() {
        processPayment(DataHelper.getApprovedCardNumber());
        verifyPaymentStatus("APPROVED");
    }

    @Test
    @DisplayName("DECLINED")
    public void shouldDeclinePayment() {
        processPayment(DataHelper.getDeclinedCardNumber());
        verifyPaymentStatus("DECLINED");
    }

    private void processPayment(String cardNumber) {
        homePage.payment();
        paymentPage = new PaymentPage();
        paymentPage.enterValidCardDetails(cardNumber, cardInfo.getMonth(), cardInfo.getYear(), cardInfo.getName(), cardInfo.getCvc());
        paymentPage.verifyAcceptedCardDetails();
    }

    private void verifyPaymentStatus(String expectedStatus) {
        String id = DBHelper.getOrderEntityData().getPayment_id();
        String actualStatus = DBHelper.getStatus(id);
        assertEquals(expectedStatus, actualStatus);
    }

    private void verifyCreditStatus(String expectedStatus) {
        String id = DBHelper.getOrderEntityData().getCredit_id();
        String actualStatus = DBHelper.getCreditStatus(id);
        assertEquals(expectedStatus, actualStatus);
    }
}
