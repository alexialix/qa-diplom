//package ru.netology.test;
//
//import com.codeborne.selenide.Configuration;
//import com.codeborne.selenide.logevents.SelenideLogger;
//import io.qameta.allure.selenide.AllureSelenide;
//import org.junit.jupiter.api.*;
//import ru.netology.data.CardInfo;
//import ru.netology.helper.DBHelper;
//import ru.netology.helper.DataHelper;
//import ru.netology.helper.OrderEntity;
//import ru.netology.pages.HomePage;
//import ru.netology.pages.PaymentPage;
//
//import static com.codeborne.selenide.Selenide.closeWebDriver;
//import static com.codeborne.selenide.Selenide.open;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class SQLTest {
//    private CardInfo data;
//    private HomePage home;
//
//    @BeforeEach
//    public void connect() {
//
//        Configuration.browser = "chrome";
//
//        open("http://localhost:8080/");
//
//        data = DataHelper.getApprovedCard();
//        home = new HomePage();
//    }
//
//    @BeforeAll
//    static void setUpAll() {
//        SelenideLogger.addListener("allure", new AllureSelenide());
//    }
//
//    @AfterEach
//    public void cleanUp() {
//        closeWebDriver();
//    }
//
//    @AfterAll
//    static void tearDownAll() {
//        SelenideLogger.removeListener("allure");
//    }
//
//    @Test
//    @DisplayName("Покупка в кредит ОДОБРЕНА")
//    public void verifyCreditPaymentApprovedStatus() {
//        home.payment();
//        PaymentPage payment = new PaymentPage();
//        payment.enterValidCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
//        payment.verifyApprovedCardDetails();
//        verifyStatusCreditApproved();
//        verifyCreditOrderIntegrity();
//    }
//
//    @Test
//    @DisplayName("Покупка в кредит ОТКЛОНЕНА")
//    public void verifyCreditPaymentDeclinedStatus() {
//        home.payment();
//        PaymentPage payment = new PaymentPage();
//        payment.enterValidCardDetails(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
//        payment.verifyApprovedCardDetails();
//        verifyStatusCreditDeclined();
//    }
//
//    @Test
//    @DisplayName("Покупка ОДОБРЕНА")
//    public void verifyPaymentApprovedStatus() {
//        home.payment();
//        PaymentPage payment = new PaymentPage();
//        payment.enterValidCardDetails(DataHelper.getApprovedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
//        payment.verifyApprovedCardDetails();
//        verifyStatusPaymentApproved();
//        verifyPaymentOrderIntegrity();
//    }
//
//    @Test
//    @DisplayName("Покупка ОТКЛОНЕНА")
//    public void verifyPaymentDeclinedStatus() {
//        home.payment();
//        PaymentPage payment = new PaymentPage();
//        payment.enterValidCardDetails(DataHelper.getDeclinedCardNumber(), data.getMonth(), data.getYear(), data.getName(), data.getCvc());
//        payment.verifyApprovedCardDetails();
//        verifyStatusPaymentDeclined();
//    }
//
//    // Методы подтверждения статуса данных
//
//    private void verifyStatusPaymentApproved() {
//        String id = DBHelper.getOrderEntityData().getPayment_id();
//        String actual = DBHelper.getStatus(id);
//        assertEquals("APPROVED", actual);
//    }
//
//    private void verifyStatusPaymentDeclined() {
//        String id = DBHelper.getOrderEntityData().getPayment_id();
//        String actual = DBHelper.getStatus(id);
//        assertEquals("DECLINED", actual);
//    }
//
//    private void verifyStatusCreditApproved() {
//        String id = DBHelper.getOrderEntityData().getCredit_id();
//        String actual = DBHelper.getCreditStatus(id);
//        assertEquals("APPROVED", actual);
//    }
//
//    private void verifyStatusCreditDeclined() {
//        String id = DBHelper.getOrderEntityData().getCredit_id();
//        String actual = DBHelper.getCreditStatus(id);
//        assertEquals("DECLINED", actual);
//    }
//
//    // Методы обработки данных
//
//    private void verifyCreditOrderIntegrity() {
//        OrderEntity order = DBHelper.getOrderEntityData();
//        assertNotNull(order.getId(), "order_id не должен быть null");
//        assertNotNull(order.getCredit_id(), "credit_id не должен быть null");
//
//        String bank_id = DBHelper.getCreditId(order.getCredit_id());
//        assertNotNull(bank_id, "bank_id не найден в credit_request_entity");
//        assertEquals(order.getCredit_id(), bank_id, "bank_id должен быть равен credit_id из order_entity");
//
//        String orderCreated = order.getCreated();
//        String creditCreated = DBHelper.getCreatedDateFromCredit(order.getCredit_id());
//
//        assertEquals(orderCreated, creditCreated, "Дата created в credit_request_entity не совпадает с order_entity");
//    }
//
//    private void verifyPaymentOrderIntegrity() {
//        OrderEntity order = DBHelper.getOrderEntityData();
//        assertNotNull(order.getId(), "order_id не должен быть null");
//        assertNotNull(order.getPayment_id(), "payment_id не должен быть null");
//
//        String transactionId = DBHelper.getPaymentId(order.getPayment_id());
//        assertNotNull(transactionId, "transaction_id не найден в payment_entity");
//        assertEquals(order.getPayment_id(), transactionId, "bank_id должен быть равен credit_id из order_entity");
//
//        String orderCreated = order.getCreated();
//        String paymentCreated = DBHelper.getCreatedDateFromPayment(order.getPayment_id());
//
//        assertEquals(orderCreated, paymentCreated, "Дата created в payment_entity не совпадает с order_entity");
//    }
//}
