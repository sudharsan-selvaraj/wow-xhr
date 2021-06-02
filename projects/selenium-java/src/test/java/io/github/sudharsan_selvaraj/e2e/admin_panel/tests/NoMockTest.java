package io.github.sudharsan_selvaraj.e2e.admin_panel.tests;

import io.github.sudharsan_selvaraj.e2e.admin_panel.BaseWebDriverTest;
import io.github.sudharsan_selvaraj.e2e.admin_panel.pages.DashBoard;
import io.github.sudharsan_selvaraj.e2e.admin_panel.pages.Table;
import io.github.sudharsan_selvaraj.wowxhr.WowXHR;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class NoMockTest extends BaseWebDriverTest {

    @Test(enabled = false)
    @Parameters({"url"})
    public void noMockTest(String url) {
        WowXHR wowXHR = wowXHRThreadLocal.get();
        WebDriver driver = wowXHR.getMockDriver();
        DashBoard dashBoard = new DashBoard(driver);
        driver.get(url);
        dashBoard.waitForLoading();

        Table userTable = dashBoard.getUserTable();
        assertNotEquals(userTable.getColumnValue("Name", 1), "Sudharsan Selvaraj");
        assertNotEquals(userTable.getColumnValue("Username", 1), "sudharsan");
    }

}
