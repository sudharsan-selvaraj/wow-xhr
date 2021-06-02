package io.github.sudharsan_selvaraj.e2e.admin_panel.tests;

import io.github.sudharsan_selvaraj.e2e.admin_panel.BaseWebDriverTest;
import io.github.sudharsan_selvaraj.e2e.admin_panel.pages.DashBoard;
import io.github.sudharsan_selvaraj.e2e.admin_panel.pages.LoginPage;
import io.github.sudharsan_selvaraj.wowxhr.WowXHR;
import io.github.sudharsan_selvaraj.wowxhr.log.XHRLog;
import io.github.sudharsan_selvaraj.wowxhr.mock.Mockable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class RequestMockTests extends BaseWebDriverTest {

    @Test
    @Parameters({"url"})
    public void requestHeadPatchTest(String url) {
        WowXHR wowXHR = wowXHRThreadLocal.get();
        WebDriver driver = wowXHR.getMockDriver();
        DashBoard dashBoard = new DashBoard(driver);


        driver.get(url+ "#/posts/list");

        wowXHR.mock().add(
                Mockable.whenGET("/api/delay")
                        .modifyRequest(
                                Mockable.mockRequest()
                                        .setHeader("wow-xhr-token", "invalid")
                        )
        );
        dashBoard.waitForLoading();
        assertEquals(driver.findElements(By.cssSelector(".humane-flatty-error")).size(), 1);
        assertEquals(driver.findElement(By.cssSelector(".humane-flatty-error")).getText(), "State change error:");
    }


    @Test
    @Parameters({"url"})
    public void requestQueryParamTest(String url) {
        WowXHR wowXHR = wowXHRThreadLocal.get();
        WebDriver driver = wowXHR.getMockDriver();
        DashBoard dashBoard = new DashBoard(driver);

        wowXHR.mock().add(
                Mockable.whenGET("/api/delay/")
                        .modifyRequest(
                                Mockable.mockRequest()
                                        .setQueryParam("delay", 1000)
                        )
        );

        driver.get(url +"/#/users/list");
        dashBoard.waitForLoading();
        wowXHR.log().clearLogs();
        dashBoard.clickLogo();
        dashBoard.waitForLoading();
        List<XHRLog> logs = wowXHR.log().getXHRLogs();
        assertTrue(logs.size() > 0);
        logs.stream().filter(l -> l.getRequest().getUrl().contains("api/delay"))
                .forEach(l -> assertTrue(l.getRequest().getUrl().contains("delay=1000")));
    }

    @Test
    @Parameters({"url"})
    public void requestBodyPatchTest(String url) throws Exception {
        WowXHR wowXHR = wowXHRThreadLocal.get();
        WebDriver driver = wowXHR.getMockDriver();
        LoginPage loginPage = new LoginPage(driver);

        Mockable loginMock = Mockable.whenPOST("/api/login")
                .modifyRequest(
                        Mockable.mockRequest()
                                .setBody("{\"email\":\"invalidemail@emial.com\",\"password\":\"somepassword\"}")
                );
        driver.get(url + "/login");
        loginPage.waitForLoading();
        loginPage.login("eve.holt@reqres.in", "cityslicka");
        assertTrue(driver.findElement(By.cssSelector(".login-success")).isDisplayed());
        assertFalse(driver.findElement(By.cssSelector(".invalid-credentials")).isDisplayed());

        /* Adding mock */
        driver.navigate().refresh();
        wowXHR.mock().add(loginMock);
        loginPage.waitForLoading();
        loginPage.login("eve.holt@reqres.in", "cityslicka");
        Thread.sleep(2000);
        assertFalse(driver.findElement(By.cssSelector(".login-success")).isDisplayed());
        assertTrue(driver.findElement(By.cssSelector(".invalid-credentials")).isDisplayed());
    }

}
