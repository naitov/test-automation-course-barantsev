package online.addressbook.appmanager;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import online.addressbook.utils.TestDataReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ApplicationManager {
    private final String browser;
    private WebDriver driver;
    private NavigationHelper navigationHelper;
    @Getter
    private SessionHelper sessionHelper;
    private GroupHelper groupHelper;
    private ContactHelper contactHelper;

    public ApplicationManager(String browser) {
        this.browser = browser;
    }

    public GroupHelper group() {
        return groupHelper;
    }

    public ContactHelper contact() {
        return contactHelper;
    }

    public NavigationHelper goTo() {
        return navigationHelper;
    }

    public void initWebDriver() {
        switch (browser.toLowerCase()) {
            case "chrome" -> driver = new ChromeDriver();
            case "firefox" -> driver = new FirefoxDriver();
            case "safari" -> driver = new SafariDriver();
            default -> throw new IllegalArgumentException("Wrong browser");
        }
        driver.manage().timeouts().implicitlyWait(Duration.of(0, ChronoUnit.SECONDS));
        driver.get(TestDataReader.read("url"));
        groupHelper = new GroupHelper(driver);
        navigationHelper = new NavigationHelper(driver);
        sessionHelper = new SessionHelper(driver);
        contactHelper = new ContactHelper(driver);
        sessionHelper.login(TestDataReader.read("login"), TestDataReader.read("password"));
    }

    public void stop() {
        driver.quit();
    }

    public void initBrowser() {
        switch (browser.toLowerCase()) {
            case "chrome" -> WebDriverManager.chromedriver().setup();
            case "firefox" -> WebDriverManager.firefoxdriver().setup();
            case "safari" -> WebDriverManager.safaridriver().setup();
            default -> throw new IllegalArgumentException("Wrong browser");
        }
    }
}
