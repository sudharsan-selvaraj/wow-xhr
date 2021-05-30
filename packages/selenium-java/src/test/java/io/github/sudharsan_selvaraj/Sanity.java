package io.github.sudharsan_selvaraj;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.sudharsan_selvaraj.wowxhr.WowXHR;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static io.github.sudharsan_selvaraj.wowxhr.mock.Mockable.*;

public class Sanity {
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
                whenGET("https://jsonplaceholder.typicode.com/posts")
                        .respond(
                                mockResponse()
                                        .withBody("[\n" +
                                                "  {\n" +
                                                "    \"userId\": 1,\n" +
                                                "    \"id\": 1,\n" +
                                                "    \"title\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\n" +
                                                "    \"body\": \"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\"\n" +
                                                "  }\n" +
                                                "]")
                                        .withStatus(200)
                        )
        );
        Path sampleFile = Paths.get("/Users/sselvaraj/Documents/git/personal/mockium/index.html");
        driver.get(sampleFile.toUri().toString());
        driver.findElement(By.tagName("button")).click();
        Thread.sleep(10000);
    }
}
