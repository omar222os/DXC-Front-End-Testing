package Sprint1;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SignUpPage extends BasePage {

    // Input fields
    private final By firstNameField   = By.cssSelector("input[formControlName='firstName']");
    private final By lastNameField    = By.cssSelector("input[formControlName='lastName']");
    private final By emailField       = By.cssSelector("input[formControlName='email']");
    private final By passwordField    = By.cssSelector("input[formControlName='password']");
    private final By confirmPassField = By.cssSelector("input[formControlName='confirmPassword']");

    // Profile picture
    private final By pictureUrlInput  = By.cssSelector(".url-input-wrapper input[type='text']");
    private final By addPictureButton = By.cssSelector(".add-btn");

    // Submit button
    private final By signUpButton     = By.cssSelector("button.submit-btn[type='submit']");

    // Error locators — scoped to their parent field-group to avoid ambiguity
    // Angular only renders these divs when the field is touched+invalid
    private final By emailError       = By.cssSelector(".field-group:has(input[formControlName='email']) .error");
    private final By passwordError    = By.cssSelector(".field-group:has(input[formControlName='password']) .error");
    private final By confirmPassError = By.cssSelector(".field-group:has(input[formControlName='confirmPassword']) .error");

    // Submit error — the div.error that appears directly after the submit button
    private final By submitError      = By.cssSelector(".submit-btn ~ .error");

    // Actions
    public void setFirstName(String firstName) {
        set(firstNameField, firstName);
    }

    public void setLastName(String lastName) {
        set(lastNameField, lastName);
    }

    public void setEmail(String email) {
        set(emailField, email);
        // Tab away to mark field as touched so Angular shows validation
        find(emailField).sendKeys(Keys.TAB);
    }

    public void setPassword(String password) {
        set(passwordField, password);
        find(passwordField).sendKeys(Keys.TAB);
    }

    public void setConfirmPassword(String confirmPassword) {
        set(confirmPassField, confirmPassword);
        find(confirmPassField).sendKeys(Keys.TAB);
    }

    public void setProfilePictureUrl(String imageUrl) {
        set(pictureUrlInput, imageUrl);
        click(addPictureButton);
    }

    public void clickSignUp() {
        click(signUpButton);
    }

    // Waits for the submit error specifically
    public void waitForSubmitError() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(submitError));
    }

    // Waits for any .error to appear (for field-level validation)
    public void waitForFieldError(By errorLocator) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(errorLocator));
    }

    // Error retrieval
    public String getGeneralErrorMessage() {
        waitForSubmitError();
        return find(submitError).getText();
    }

    public String getEmailValidationError() {
        waitForFieldError(emailError);
        return find(emailError).getText();
    }

    public String getPasswordValidationError() {
        waitForFieldError(passwordError);
        return find(passwordError).getText();
    }

    public String getConfirmPasswordError() {
        waitForFieldError(confirmPassError);
        return find(confirmPassError).getText();
    }
}