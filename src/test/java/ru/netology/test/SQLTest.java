package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.CardInfo;
import ru.netology.helper.DBHelper;
import ru.netology.helper.DataHelper;
import ru.netology.helper.OrderEntity;
import ru.netology.pages.CreditPage;
import ru.netology.pages.HomePage;
import ru.netology.pages.PaymentPage;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class SQLTest {
    private CardInfo data;
    private HomePage home;

    @BeforeEach
    public void connect() {
        open("http://localhost:8080/");

        data = DataHelper.getApprovedCard();
        home = new HomePage();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    public void cleanUp() {
        closeWebDriver();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Покупка в кредит ОДОБРЕНА")
    public void verifyCreditPaymentApprovedStatus() {
        home.payment();
        CreditPage payment = new CreditPage();
        payment.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.verifyApprovedCardDetails();
        verifyStatusCreditApproved();
    }

    @Test
    @DisplayName("Данные о покупке в кредит есть в таблице")
    public void verifyCreditPaymentNotNull() {
        home.payment();
        CreditPage payment = new CreditPage();
        payment.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.verifyApprovedCardDetails();
        CPOShouldNotBeNull();
    }

    @Test
    @DisplayName("Интеграция credit_request_entity с order_entity")
    public void verifyCreditPaymentIntegrity() {
        home.payment();
        CreditPage payment = new CreditPage();
        payment.enterCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.verifyApprovedCardDetails();
        verifyCOIntegrity();
    }

    @Test
    @DisplayName("Покупка в кредит ОТКЛОНЕНА")
    public void verifyCreditPaymentDeclinedStatus() {
        home.payment();
        CreditPage payment = new CreditPage();
        payment.enterCardDetails(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.verifyDeclinedCardDetails();
        verifyStatusCreditDeclined();
    }

    @Test
    @DisplayName("Покупка ОДОБРЕНА")
    public void verifyPaymentApprovedStatus() {
        home.payment();
        PaymentPage payment = new PaymentPage();
        payment.enterValidCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.verifyApprovedCardDetails();
        verifyStatusPaymentApproved();
    }

    @Test
    @DisplayName("Данные о покупке есть в таблице")
    public void verifyPaymentNotNull() {
        home.payment();
        PaymentPage payment = new PaymentPage();
        payment.enterValidCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.verifyApprovedCardDetails();
        POShouldNotBeNull();
    }


    @Test
    @DisplayName("Интеграция payment_entity с order_entity")
    public void verifyPaymentIntegrity() {
        home.payment();
        PaymentPage payment = new PaymentPage();
        payment.enterValidCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.verifyApprovedCardDetails();
        verifyPOIntegrity();
    }


    @Test
    @DisplayName("Покупка ОТКЛОНЕНА")
    public void verifyPaymentDeclinedStatus() {
        home.payment();
        PaymentPage payment = new PaymentPage();
        payment.enterValidCardDetails(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
        payment.verifyDeclinedCardDetails();
        verifyStatusPaymentDeclined();
    }

    // Методы подтверждения статуса данных

    private void verifyStatusPaymentApproved() {
        String id = DBHelper.getOrderEntityData().getPayment_id();
        String actual = DBHelper.getStatus(id);
        assertEquals("APPROVED", actual);
    }

    private void verifyStatusPaymentDeclined() {
        String id = DBHelper.getOrderEntityData().getPayment_id();
        String actual = DBHelper.getStatus(id);
        assertEquals("DECLINED", actual);
    }

    private void verifyStatusCreditApproved() {
        String id = DBHelper.getOrderEntityData().getCredit_id();
        String actual = DBHelper.getCreditStatus(id);
        assertEquals("APPROVED", actual);
    }

    private void verifyStatusCreditDeclined() {
        String id = DBHelper.getOrderEntityData().getCredit_id();
        String actual = DBHelper.getCreditStatus(id);
        assertEquals("DECLINED", actual);
    }

    // Методы обработки данных

    private void CPOShouldNotBeNull() {
        OrderEntity order = DBHelper.getOrderEntityData();
        assertNotNull(order.getId(), "order_id не должен быть null");
        assertNotNull(order.getCredit_id(), "credit_id не должен быть null");
    }

    private void verifyCOIntegrity() {
        OrderEntity order = DBHelper.getOrderEntityData();
        String bank_id = DBHelper.getCreditId(order.getCredit_id());
        assertNotNull(bank_id, "bank_id не найден в credit_request_entity");
        assertEquals(order.getCredit_id(), bank_id, "bank_id должен быть равен credit_id из order_entity");
    }

    private void POShouldNotBeNull() {
        OrderEntity order = DBHelper.getOrderEntityData();
        assertNotNull(order.getId(), "order_id не должен быть null");
        assertNotNull(order.getPayment_id(), "payment_id не должен быть null");
    }

    private void verifyPOIntegrity() {
        OrderEntity order = DBHelper.getOrderEntityData();
        String transactionId = DBHelper.getPaymentId(order.getPayment_id());
        assertNotNull(transactionId, "transaction_id не найден в payment_entity");
        assertEquals(order.getPayment_id(), transactionId, "transactionId должен быть равен payment_id из order_entity");
    }
}
