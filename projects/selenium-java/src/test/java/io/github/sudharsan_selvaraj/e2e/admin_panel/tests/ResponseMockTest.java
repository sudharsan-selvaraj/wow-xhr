package io.github.sudharsan_selvaraj.e2e.admin_panel.tests;

import io.github.sudharsan_selvaraj.e2e.admin_panel.BaseWebDriverTest;
import io.github.sudharsan_selvaraj.e2e.admin_panel.models.User;
import io.github.sudharsan_selvaraj.e2e.admin_panel.pages.DashBoard;
import io.github.sudharsan_selvaraj.e2e.admin_panel.pages.Table;
import io.github.sudharsan_selvaraj.wowxhr.WowXHR;
import io.github.sudharsan_selvaraj.wowxhr.mock.Mockable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class ResponseMockTest extends BaseWebDriverTest {

    @Test
    @Parameters({"url"})
    public void responseBodyMock(String url) {
        WowXHR wowXHR = wowXHRThreadLocal.get();
        WebDriver driver = wowXHR.getMockDriver();
        DashBoard dashBoard = new DashBoard(driver);

        List<User> mockedResponse = Arrays.asList(readListFromJson("/responses/users.json", User[].class));

        wowXHR.mock().add(
                Mockable.whenGET("/api/delay/users")
                        .respond(
                                Mockable.mockResponse()
                                        .withBody(mockedResponse)
                        )
        );

        driver.get(url + "/#/users/list");
        dashBoard.waitForLoading();
        dashBoard.clickLogo();

        Table userTable = dashBoard.getUserTable();
        assertEquals(userTable.getColumnValue("Name", 1), "Sudharsan Selvaraj");
        assertEquals(userTable.getColumnValue("Username", 1), "sudharsan");
    }

    @Test
    @Parameters({"url"})
    public void responseStatusMock(String url) {
        WowXHR wowXHR = wowXHRThreadLocal.get();
        WebDriver driver = wowXHR.getMockDriver();
        DashBoard dashBoard = new DashBoard(driver);

        driver.get(url+"#/users/list");
        wowXHR.mock().add(
                Mockable.whenGET("/api/delay")
                        .respond(
                                Mockable.mockResponse()
                                        .withStatus(500)
                        )
        );
        dashBoard.waitForLoading();
        dashBoard.clickPostsMenu();
        assertEquals(driver.findElements(By.cssSelector(".humane-flatty-error")).size(), 1);
        assertEquals(driver.findElement(By.cssSelector(".humane-flatty-error")).getText(), "State change error:");
    }

}
