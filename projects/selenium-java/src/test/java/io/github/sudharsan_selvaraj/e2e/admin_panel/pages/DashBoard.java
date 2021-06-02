package io.github.sudharsan_selvaraj.e2e.admin_panel.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashBoard extends BasePage {

    @FindBy(xpath = ".//*[contains(@class,'panel-default')][.//a[contains(.,'Users')]]")
    private WebElement userPanel;

    @FindBy(xpath = ".//*[contains(@class,'panel-default')][.//a[contains(.,'Users')]]")
    private WebElement postsPanel;

    @FindBy(css = ".navbar-header a")
    private WebElement navBarLogo;

    @Getter
    private Table userTable, postsTable;

    public DashBoard(WebDriver driver) {
        super(driver);
        userTable = new Table(userPanel);
        postsTable = new Table(postsPanel);
    }

    public DashBoard clickUserMenu() {
        return clickMenu("Users");
    }

    public DashBoard clickPostsMenu() {
        return clickMenu("Posts");
    }

    public DashBoard moveToDashboard() {
        driver.findElement(By.cssSelector(".navbar-brand")).click();
        waitForLoading();
        return this;
    }

    public DashBoard clickLogo() {
        navBarLogo.click();
        waitForLoading();
        return this;
    }

    private DashBoard clickMenu(String menu) {
        driver.findElement(By.cssSelector("#side-menu")).findElement(By.partialLinkText(menu)).click();
        waitForLoading();
        return this;
    }
}
