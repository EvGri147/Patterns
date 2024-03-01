package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataGenerator;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[placeholder='Город']").setValue(validUser.getCity());
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input.input__control").setValue(validUser.getName());
        $("[name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[type=button] .button__text").click();
        $(".notification__title").shouldBe(Condition.visible, Duration.ofSeconds(4));
        $("[data-test-id=success-notification] .notification__content").should(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(secondMeetingDate);
        $("[type=button] .button__text").click();
        $("[data-test-id=replan-notification] .notification__title").should(Condition.exactText("Необходимо подтверждение"));
        $("[data-test-id=replan-notification] span.button__text").click();
        $("[data-test-id=success-notification] .notification__content").should(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }



}
