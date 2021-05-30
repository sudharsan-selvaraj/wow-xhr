package io.github.sudharsan_selvaraj;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.sudharsan_selvaraj.wowxhr.WowXHR;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static io.github.sudharsan_selvaraj.wowxhr.mock.Mockable.*;

public class TelehealerTest {
    public static void main(String[] args) throws Exception {
        WebDriverManager.firefoxdriver().setup();
        WebDriverManager.chromedriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.setLogLevel(FirefoxDriverLogLevel.TRACE);
        System.out.println(System.getProperty("webdriver.firefox.driver"));

       WowXHR wowXHR = new WowXHR(new FirefoxDriver(options));
        //WowXHR wowXHR = new WowXHR(new ChromeDriver());
        WebDriver driver = wowXHR.getMockDriver();

        wowXHR.mock().add(
                whenPOST("https://api.telehealer.com/login")
                        .respond(
                                mockResponse()
                                        .withBody("{\n" +
                                                "    \"success\": true,\n" +
                                                "    \"message\": \"Token active.\",\n" +
                                                "    \"token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InN1cGVydXNlckB0ZWxlaGVhbGVyLmNvbSIsIm5hbWUiOiJTdXBlciBVc2VyIiwidG9rZW5faWQiOiJhYmNlZmEyMC1jZmVjLTExZTgtYjIxNS1lMzUwODczYmYxMGYiLCJjbGFpbSI6eyJhY2Nlc3MiOiJXIn0sInVzZXJfZ3VpZCI6IjlhNGVhMmY1LTk4ZDEtNDE3Mi1iNjNiLWJlOTgzYThmYTRjNiIsInVzZXJfaWQiOjEsInBob25lIjoiKzE5OTk5OTk5OTk5IiwiaXNzIjoiRWVrc2hheSIsInZlcnNpb24iOiJkZWZhdWx0IiwidXNlcl9hY3RpdmF0ZWQiOiJBQ1RJVkFURUQiLCJyb2xlIjoiQVVTRVIiLCJpbnN0YWxsX3R5cGUiOiJ0ZWxlaGVhbGVyIiwiaWF0IjoxNjIyMzk3OTUzLCJleHAiOjE2MjI0MDE1NTN9.wx0tHevsznlA_ysCgPRcB6q51TqrDPubOdK0B5U-_Wo\",\n" +
                                                "    \"name\": \"Super User\"\n" +
                                                "}")
                                        .withStatus(200)
                        )
        );

        wowXHR.mock().add(
                whenGET("https://api.telehealer.com/api/whoami")
                        .respond(
                                mockResponse()
                                        .withBody("{\n" +
                                                "  \"first_name\": \"Sudharsan\",\n" +
                                                "  \"last_name\": \"Selvaraj\",\n" +
                                                "  \"user_avatar\": null,\n" +
                                                "  \"dob\": null,\n" +
                                                "  \"name\": \"Sudharsan Selvaraj\",\n" +
                                                "  \"user_id\": 1,\n" +
                                                "  \"user_guid\": \"9a4ea2f5-98d1-4172-b63b-be983a8fa4c6\",\n" +
                                                "  \"email\": \"sudharsan@telehealer.com\",\n" +
                                                "  \"role\": \"AUSER\",\n" +
                                                "  \"email_verified\": true,\n" +
                                                "  \"mirth_ccd_enabled\": false,\n" +
                                                "  \"vitals_ccd_enabled\": false,\n" +
                                                "  \"status\": \"AVAILABLE\",\n" +
                                                "  \"phone\": \"+919677730317\",\n" +
                                                "  \"gender\": \"male\",\n" +
                                                "  \"history\": null,\n" +
                                                "  \"questionnaire\": null,\n" +
                                                "  \"appt_length\": null,\n" +
                                                "  \"cc_payment_accepted\": false,\n" +
                                                "  \"appt_start_time\": null,\n" +
                                                "  \"appt_end_time\": null,\n" +
                                                "  \"secure_message\": false,\n" +
                                                "  \"connection_requests\": false,\n" +
                                                "  \"appt_requests\": false,\n" +
                                                "  \"install_type\": \"telehealer\",\n" +
                                                "  \"last_active\": \"2021-05-27T17:11:10.369Z\",\n" +
                                                "  \"recording_enabled\": true,\n" +
                                                "  \"transcription_enabled\": true,\n" +
                                                "  \"orders_enabled\": true,\n" +
                                                "  \"debug\": false,\n" +
                                                "  \"user_detail\": {\n" +
                                                "    \"signature\": null,\n" +
                                                "    \"data\": null\n" +
                                                "  },\n" +
                                                "  \"version\": \"default\",\n" +
                                                "  \"user_activated\": \"ACTIVATED\",\n" +
                                                "  \"payment_account_info\": {\n" +
                                                "    \"cc_status\": \"not_captured\",\n" +
                                                "    \"oauth_status\": \"not_connected\",\n" +
                                                "    \"is_cc_captured\": false,\n" +
                                                "    \"is_default_card_valid\": false,\n" +
                                                "    \"saved_cards_count\": 0\n" +
                                                "  }\n" +
                                                "}")
                                        .withStatus(200)
                                        .withDelay(20)
                        )
        );

        driver.get("https://api.telehealer.com/login");
        WebElement username = new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input")));
        username.sendKeys("superuserkjhkhjkhkh@telehealer.com");
        driver.findElement(By.cssSelector("[type='password']")).sendKeys("Telehealer@9876");
        driver.findElement(By.cssSelector("button.primary")).click();
        Thread.sleep(15000);
    }
}
