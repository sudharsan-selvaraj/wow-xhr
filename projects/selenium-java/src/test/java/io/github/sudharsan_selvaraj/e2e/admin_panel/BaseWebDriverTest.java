package io.github.sudharsan_selvaraj.e2e.admin_panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.sudharsan_selvaraj.wowxhr.WowXHR;
import io.github.sudharsan_selvaraj.wowxhr.WowXHRScriptExecutor;
import io.github.sudharsan_selvaraj.wowxhr.exceptions.DriverNotSupportedException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.util.*;


public class BaseWebDriverTest {

    protected ThreadLocal<WowXHR> wowXHRThreadLocal = new ThreadLocal<>();

    @BeforeSuite
    public void initWebDriver() {
        WebDriverManager.chromedriver().setup();
        WebDriverManager.firefoxdriver().setup();
    }

    @BeforeMethod
    @Parameters({"browser"})
    public void setupDriver(String browser) throws DriverNotSupportedException {
        WebDriver driver;
        switch (browser) {
            case "firefox":
                FirefoxOptions ffoptions = new FirefoxOptions();
                ffoptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                driver = new FirefoxDriver(ffoptions);
                break;
            default:
                ChromeOptions options = new ChromeOptions();
                driver = new ChromeDriver(options);
        }
        wowXHRThreadLocal.set(new WowXHR(driver));
    }


    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (wowXHRThreadLocal.get().getMockDriver() != null) {
           wowXHRThreadLocal.get().getMockDriver().quit();
        }
    }

    protected <T> T readListFromJson(String filePath, Class<T> responseClass) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(readScriptFromResource(filePath), responseClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readScriptFromResource(String fileName) {
        return new Scanner(Objects.requireNonNull(WowXHRScriptExecutor.class.getResourceAsStream(fileName)), "UTF-8").useDelimiter("\\A").next();
    }

    protected void sleep(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (Exception e) {
            //
        }
    }
}
