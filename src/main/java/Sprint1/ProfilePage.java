package Sprint1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;

public class ProfilePage extends BasePage {

    // ── Read-only display fields
    private final By firstNameField = By.id("FirstName");
    private final By lastNameField  = By.id("LastName");
    private final By emailField     = By.id("Email");
    private final By passwordField  = By.id("Password");

    // ── Action links
    private final By updatePictureLink  = By.id("UpdatePicture");
    private final By changePasswordLink = By.id("ChangePassword");

    // ── Update-picture panel
    private final By pictureUrlInput = By.cssSelector(".picture-url-input");
    private final By pictureSaveBtn  = By.cssSelector(".picture-submit-btn");

    // ── Change-password panel
    private final By passwordPanelInputs = By.cssSelector(".password-panel .picture-url-input");
    private final By passwordSaveBtn     = By.cssSelector(".password-panel .picture-submit-btn");
    private final By passwordMismatch    = By.cssSelector(".password-mismatch");
    private final By passwordSuccess     = By.cssSelector(".password-success");
    private final By passwordErrorMsg    = By.cssSelector(".password-panel .picture-error");

    // ── Read helpers
    public String getFirstName() {
        return find(firstNameField).getAttribute("value");
    }

    public String getLastName() {
        return find(lastNameField).getAttribute("value");
    }

    public String getEmail() {
        return find(emailField).getAttribute("value");
    }

    public String getPasswordDomValue() {
        return find(passwordField).getAttribute("value");
    }

    // ── Update-picture panel
    public void openPicturePanel() {
        click(updatePictureLink);
    }

    public void submitNewProfilePicture(String imageUrl) {
        openPicturePanel();
        WebElement input = find(pictureUrlInput);
        input.clear();
        input.sendKeys(imageUrl);
        click(pictureSaveBtn);
    }

    // ── Change-password panel
    public void openPasswordPanel() {
        click(changePasswordLink);
    }

    public void submitPasswordChange(String current, String newPwd, String confirm) {
        openPasswordPanel();
        // Using driver.findElements() directly since BasePage only has find()
        List<WebElement> inputs = driver.findElements(passwordPanelInputs);
        inputs.get(0).sendKeys(current);
        inputs.get(1).sendKeys(newPwd);
        inputs.get(2).sendKeys(confirm);
        click(passwordSaveBtn);
    }

    public boolean isPasswordMismatchShown() {
        return !driver.findElements(passwordMismatch).isEmpty();
    }

    public boolean isPasswordSuccessShown() {
        return !driver.findElements(passwordSuccess).isEmpty();
    }

    public boolean isPasswordErrorShown() {
        return !driver.findElements(passwordErrorMsg).isEmpty();
    }

    public void clickChangePassword() {
        click(changePasswordLink);
    }
}