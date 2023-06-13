import org.testng.annotations.Test;

public class TestCostco extends BaseTest {

    @Test
    public void testLanguage() {
        steps.openMainPage();
        steps.changeLanguage();
        steps.openShopList();
        steps.openSelectedCategory();
        steps.verifyTheTitle();
    }

}
