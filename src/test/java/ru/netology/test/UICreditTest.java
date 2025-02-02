package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.CardInfo;
import ru.netology.pages.CreditPage;
import ru.netology.pages.HomePage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.helper.DataHelper.*;

public class UICreditTest {

    private CardInfo data;
    private HomePage home;

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
    @DisplayName("Отправка пустой формы Кредит")
    public void testEmptyCreditData() {
        CreditPage creditRequest = home.creditPayment();
        creditRequest.verifyAllFieldsAreEmpty();
    }
}