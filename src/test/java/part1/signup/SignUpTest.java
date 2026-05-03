package part1.signup;

import Sprint1.ProfilePage;
import Sprint1.SignUpPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import part1.base.BaseTest;
import java.time.Duration;

public class SignUpTest extends BaseTest {

    @BeforeMethod
    public void navigateToSignUp() {
        driver.get("http://localhost:4200/signup");
        driver.manage().deleteAllCookies();
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
        ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
        driver.get("http://localhost:4200/signup");

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("input[formControlName='email']")
                ));
    }

    // TC01 – Successful registration
    @Test(priority = 1)
    public void TC01_verifySuccessfulRegistration() {
        // Use timestamp to guarantee a unique email on every run
        String uniqueEmail = "intern.test." + System.currentTimeMillis() + "@dxc.com";

        signUpPage.setProfilePictureUrl("https://www.w3schools.com/howto/img_avatar2.png");
        signUpPage.setFirstName("Omar");
        signUpPage.setLastName("Osama");
        signUpPage.setEmail(uniqueEmail);
        signUpPage.setPassword("Str0ngP@ss6!");
        signUpPage.setConfirmPassword("Str0ngP@ss6!");
        signUpPage.clickSignUp();

        // Wait for navigation away from signup
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.not(
                        ExpectedConditions.urlContains("signup")
                ));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("login") || currentUrl.contains("home") || currentUrl.contains("profile"),
                "User was not redirected after successful registration. Current URL: " + currentUrl
        );
    }

    // TC02 – Existing email
    @Test(priority = 2)
    public void TC02_verifyRegistrationFailureExistingEmail() {
        signUpPage.setFirstName("Omar");
        signUpPage.setLastName("Osama");
        signUpPage.setEmail("omar@gmail.com");
        signUpPage.setPassword("Str0ngP@ss6!");
        signUpPage.setConfirmPassword("Str0ngP@ss6!");
        signUpPage.clickSignUp();

        Assert.assertTrue(
                signUpPage.getGeneralErrorMessage().length() > 0,
                "System did not show an error for an already registered email."
        );
    }

    // TC03 – Invalid email format
    @Test(priority = 3)
    public void TC03_verifyValidationForInvalidEmail() {
        signUpPage.setFirstName("Omar");
        signUpPage.setLastName("Osama");
        signUpPage.setEmail("omar.osama.dxc.com"); // missing @
        signUpPage.setPassword("ValidPass6!");
        signUpPage.setConfirmPassword("ValidPass6!");
        signUpPage.clickSignUp();

        Assert.assertTrue(
                signUpPage.getEmailValidationError().length() > 0,
                "Email format validation error was not shown."
        );
    }

    // TC04 – Password too short
    @Test(priority = 4)
    public void TC04_verifyPasswordComplexityEnforcement() {
        signUpPage.setFirstName("Jane");
        signUpPage.setLastName("Smith");
        signUpPage.setEmail("testuser99@email.com");
        signUpPage.setPassword("123"); // too short — min is 6
        signUpPage.setConfirmPassword("123");
        signUpPage.clickSignUp();

        Assert.assertTrue(
                signUpPage.getPasswordValidationError().length() > 0,
                "Password complexity validation was not enforced."
        );
    }

    // TC05 – Passwords do not match
    @Test(priority = 5)
    public void TC05_verifyPasswordMismatchValidation() {
        signUpPage.setFirstName("Jane");
        signUpPage.setLastName("Smith");
        signUpPage.setEmail("testuser99@email.com");
        signUpPage.setPassword("ValidPass6!");
        signUpPage.setConfirmPassword("DifferentPass6!"); // mismatch
        signUpPage.clickSignUp();

        Assert.assertTrue(
                signUpPage.getConfirmPasswordError().contains("match"),
                "Password mismatch validation was not shown."
        );
    }

    // TC06 – XSS sanitization
//    @Test(priority = 6)
//    public void TC06_verifySanitizationAgainstXSS() {
//        String xssPayload  = "<script>alert('Hacked')</script>";
//        String htmlPayload = "<h1>Test</h1>";
//
//        signUpPage.setFirstName(xssPayload);
//        signUpPage.setLastName(htmlPayload);
//        signUpPage.setEmail("security.test@email.com");
//        signUpPage.setPassword("SecurePass6!");
//        signUpPage.setConfirmPassword("SecurePass6!");
//        signUpPage.clickSignUp();
//
//        // If rejected on front-end, a submit error appears
//        if (!driver.findElements(By.cssSelector(".submit-btn ~ .error")).isEmpty()) {
//            Assert.assertTrue(
//                    signUpPage.getGeneralErrorMessage().length() > 0,
//                    "System did not show an error for XSS input."
//            );
//        } else {
//            // If accepted, verify it renders as literal text on the profile page
//            new WebDriverWait(driver, Duration.ofSeconds(10))
//                    .until(ExpectedConditions.not(
//                            ExpectedConditions.urlContains("signup")
//                    ));
//
//            String currentUrl = driver.getCurrentUrl();
//            String profileUrl = currentUrl.replace("/home", "/profile");
//            driver.get(profileUrl);
//
//            ProfilePage profilePage = new ProfilePage();
//            Assert.assertEquals(profilePage.getFirstName(), xssPayload,
//                    "XSS payload was not rendered as a literal string.");
//            Assert.assertEquals(profilePage.getLastName(), htmlPayload,
//                    "HTML payload was not rendered as a literal string.");
//        }
//    }
}