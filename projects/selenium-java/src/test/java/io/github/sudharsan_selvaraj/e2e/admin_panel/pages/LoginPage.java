package io.github.sudharsan_selvaraj.e2e.admin_panel.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    @FindBy(id = "username")
    private WebElement usernameEle;

    @FindBy(id = "password")
    private WebElement passwordEle;

    @FindBy(css = "input[type='button']")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        usernameEle.clear();
        usernameEle.sendKeys(username);

        passwordEle.clear();
        passwordEle.sendKeys(password);
        loginButton.click();

        waitForLogin();
    }

    public void waitForLogin() {
        new WebDriverWait(driver, 30)
                .until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(".loading"))));
    }

    @Override
    public void waitForLoading() {
        new WebDriverWait(driver, 30)
                .until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
    }
}
