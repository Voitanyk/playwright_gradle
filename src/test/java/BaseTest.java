import io.qameta.allure.Attachment;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.util.Locale;


public class BaseTest {
    Steps steps = new Steps();


    @BeforeClass
    public void setUp() throws Exception {
        steps.setUp();
        steps.setUpLanguage();
    }


    @AfterClass
    public void tearDown() {
        steps.tearDown();
    }

    @AfterMethod
    public void screenshotUponFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            saveScreenshot(steps.page.screenshot());
        }
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshot(byte[] screenShot) {
        return screenShot;
    }
}
