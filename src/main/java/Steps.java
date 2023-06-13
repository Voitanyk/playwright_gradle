import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Steps {
    private Browser browser;
    protected Page page;
    private BrowserContext context;
    boolean isTraceEnabled = true;
    protected ResourceBundle resourceBundle;
    Properties properties;
    InputStream inputStream;


    @Step("Set up the browser")
    public void setUp() {
        browser = Playwright
                .create()
                .chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }
    /*context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(false));
        isTraceEnabled = true;*/

    @Step("Set up language")
    public void setUpLanguage() {
        properties = new Properties();
        try {
            inputStream = new FileInputStream("src/main/resources/application.properties");
            properties.load(inputStream);
            String propertyValue = properties.getProperty("language");
            switch (propertyValue) {
                case "english":
                    Locale localeEn = new Locale("en", "CA");
                    resourceBundle = ResourceBundle.getBundle("text", localeEn);
                    break;
                case "french":
                    Locale localeFr = new Locale("fr", "CA");
                    resourceBundle = ResourceBundle.getBundle("text", localeFr);
                default:
                    System.out.println("Chosen language is not supported");
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Close the browser")
    public void tearDown() {

        if (browser != null) {
            browser.close();
            browser = null;
        }
    }


    @Step("Open main page")
    public void openMainPage() {
        page.navigate("https://www.costco.ca/");
        page.click("#onetrust-accept-btn-handler");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @Step
    public void changeLanguage() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForSelector("#language-select");
        page.locator("#language-select").dispatchEvent("mouseover");
        page.getByText(resourceBundle.getString("language")).first().click();
        assertThat(page.locator("#language-select")).containsText(resourceBundle.getString("locate"));
    }

    @Step("Open shop list items")
    public void openShopList() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForSelector("#navigation-dropdown");
        page.locator("#navigation-dropdown").dispatchEvent("mouseover");
    }

    @Step("Select the category")
    public void openSelectedCategory() {
        page.getByText(resourceBundle.getString("category")).click();
    }

    @Step("Verify if the page title is correct")
    public void verifyTheTitle() {
        assertThat(page).hasTitle(resourceBundle.getString("electronicsPageTitle"));
    }

}

