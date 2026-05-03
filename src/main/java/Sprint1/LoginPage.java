package Sprint1;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage extends BasePage {

    private final By emailField    = By.cssSelector("input[type='email']");
    private final By passwordField = By.cssSelector("input[type='password']");
    private final By loginButton   = By.cssSelector("button[type='submit']");
    private final By loginError    = By.cssSelector(".login-error");

    public void setUsername(String username) {
        set(emailField, username);
    }

    public void setPassword(String password) {
        set(passwordField, password);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    public void waitForLoginToComplete() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.not(
                        ExpectedConditions.urlContains("login")
                ));
    }

    public void waitForErrorMessage() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(loginError));
    }

    public String getErrorMessage() {
        return find(loginError).getText();
    }
}