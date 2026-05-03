package part1.base;
import Sprint1.*; // Imports BasePage, LoginPage, SignUpPage, ProfilePage
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.log.Log;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    protected WebDriver driver;
    protected BasePage basePage;
    protected LoginPage loginPage;
    protected SignUpPage signUpPage;
    protected ProfilePage profilePage; // Declare ProfilePage

    private String url = "http://localhost:4200/login";

    @BeforeClass
    public void setup() {
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get(url);

        basePage = new BasePage();
        basePage.setDriver(driver);

        loginPage = new LoginPage();
        signUpPage = new SignUpPage();
        profilePage = new ProfilePage(); // Initialize ProfilePage
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}