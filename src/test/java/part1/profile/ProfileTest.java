package part1.profile;

import Sprint1.LoginPage;
import Sprint1.ProfilePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import part1.base.BaseTest;
import java.time.Duration;

public class ProfileTest extends BaseTest {

    @BeforeMethod
    public void navigateToProfile() {
        // Clear session so login page is always shown
        driver.get("http://localhost:4200/login");
        driver.manage().deleteAllCookies();
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
        ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
        driver.get("http://localhost:4200/login");

        // Wait for login form to be visible
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("input[type='email']")
                ));

        LoginPage loginPage = new LoginPage();
        loginPage.setUsername("omar1@gmail.com");
        loginPage.setPassword("Abc@123");
        loginPage.clickLoginButton();

        // Wait until browser navigates away from /login
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.not(
                        ExpectedConditions.urlContains("login")
                ));

        // Login redirects to /home, manually navigate to /profile
        String currentUrl = driver.getCurrentUrl();
        String profileUrl = currentUrl.replace("/home", "/profile");
        driver.get(profileUrl);

        // Wait for the profile card to fully load — *ngIf="!isLoading && userProfile"
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("FirstName")
                ));
    }

    @Test(priority = 1)
    public void TC01_verifyUserDataPreFilled() {
        ProfilePage profilePage = new ProfilePage();

        Assert.assertEquals(profilePage.getFirstName(), "Omar",
                "First Name did not match the database record.");
        Assert.assertEquals(profilePage.getLastName(), "Osama",
                "Last Name did not match the database record.");
        Assert.assertEquals(profilePage.getEmail(), "omar1@gmail.com",
                "Email did not match the database record.");
    }

    @Test(priority = 2)
    public void TC02_verifyUpdatePicturePanelAcceptsUrl() {
        ProfilePage profilePage = new ProfilePage();

        String testImageUrl = "https://www.w3schools.com/howto/img_avatar2.png";
        profilePage.submitNewProfilePicture(testImageUrl);

        Assert.assertFalse(
                profilePage.isPasswordErrorShown(),
                "An unexpected error was shown after submitting a valid picture URL."
        );
    }

    @Test(priority = 3)
    public void TC03_verifyChangePasswordOpensInlinePanel() {
        ProfilePage profilePage = new ProfilePage();
        profilePage.openPasswordPanel();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("profile"),
                "Clicking Change Password unexpectedly navigated away from the profile page."
        );

        Assert.assertFalse(
                profilePage.isPasswordErrorShown(),
                "Password error was shown before the user interacted with the panel."
        );
    }


    @Test(priority = 4)
    public void TC05_verifyActualPasswordNotExposedInDOM() {
        ProfilePage profilePage = new ProfilePage();

        String domValue = profilePage.getPasswordDomValue();
        String realPwd  = "Abc@123";

        Assert.assertNotEquals(domValue, realPwd,
                "SECURITY: The plain-text password is exposed in the HTML value attribute!");

        Assert.assertEquals(domValue, "••••••••",
                "Expected the password field DOM value to be '••••••••' but found: " + domValue);
    }
}