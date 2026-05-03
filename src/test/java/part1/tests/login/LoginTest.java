package part1.tests.login;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import part1.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @BeforeMethod
    public void navigateToLogin() {
        driver.get("http://localhost:4200/login");

        // Clear cookies AND localStorage to fully reset the session
        driver.manage().deleteAllCookies();
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
        ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");

        // Navigate again after clearing so Angular sees no session
        driver.get("http://localhost:4200/login");

        // Wait for the login form to be visible before any test runs
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("input[type='email']")
                ));
    }

    @Test(priority = 1)
    public void TC01_verifySuccessfulLogin() {
        // Wait for the form to be fully rendered before interacting
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("input[type='email']")
                ));

        loginPage.setUsername("omar@gmail.com");
        loginPage.setPassword("Abc@123");
        loginPage.clickLoginButton();

        loginPage.waitForLoginToComplete();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("home"),
                "User was not redirected to the home page after successful login."
        );
    }

    @Test(priority = 2)
    public void TC02_verifyLoginFailureInvalidPassword() {

        loginPage.setUsername("omar@gmail.com");
        loginPage.setPassword("WrongPassword999");
        loginPage.clickLoginButton();

        // Wait for error message to appear
        loginPage.waitForErrorMessage();

        Assert.assertTrue(
                loginPage.getErrorMessage().length() > 0,
                "Expected an error message for invalid password but none was shown."
        );
    }

    @Test(priority = 3)
    public void TC03_verifyLoginFailureUnregisteredUser() {

        loginPage.setUsername("ghost_user_123@gmail.com");
        loginPage.setPassword("TestPassword1!");
        loginPage.clickLoginButton();

        // Wait for error message to appear
        loginPage.waitForErrorMessage();

        Assert.assertTrue(
                loginPage.getErrorMessage().length() > 0,
                "System did not reject the unregistered user properly."
        );
    }

    @Test(priority = 4)
    public void TC04_verifyEmptyFieldsPreventSubmission() {
        loginPage.clickLoginButton();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("login"),
                "Empty form submission should not navigate away from the login page."
        );
    }

    @Test(priority = 5)
    public void TC05_verifyPasswordFieldCaseSensitivity() {
        loginPage.setUsername("omar@gmail.com");
        loginPage.setPassword("abc@123");
        loginPage.clickLoginButton();

        // Wait for error message to appear
        loginPage.waitForErrorMessage();

        Assert.assertTrue(
                loginPage.getErrorMessage().length() > 0,
                "System accepted an incorrectly cased password."
        );
    }

//    @Test(priority = 6)
//    public void TC06_verifyResistanceToBasicSQLInjection() {
//        loginPage.setUsername("' OR '1'='1");
//        loginPage.setPassword("RandomPass123");
//        loginPage.clickLoginButton();
//
//        // Wait for error message to appear
//        loginPage.waitForErrorMessage();
//
//        Assert.assertFalse(
//                driver.getCurrentUrl().contains("home"),
//                "CRITICAL: SQL Injection bypassed authentication!"
//        );
//
//        Assert.assertTrue(
//                loginPage.getErrorMessage().length() > 0,
//                "System did not return an error after SQL injection attempt."
//        );
//    }
}