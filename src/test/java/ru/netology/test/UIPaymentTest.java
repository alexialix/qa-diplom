package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.data.CardInfo;
import ru.netology.helper.DataHelper;
import ru.netology.pages.HomePage;
import ru.netology.pages.PaymentPage;

import java.text.ParseException;


import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static ru.netology.helper.DataHelper.getApprovedCard;


public class UIPaymentTest {

    CardInfo data;
    HomePage home;

    @BeforeEach
    public void prepare() throws ParseException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--allowed-ips=127.0.0.1");

        Configuration.browser = "chrome";
        Configuration.browserBinary = "D:/chrome-win64/chrome.exe";

        open("http://localhost:8080/");
        data = getApprovedCard();
        home = new HomePage();
    }

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().driverVersion("131.0.6778.85").setup();
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
}