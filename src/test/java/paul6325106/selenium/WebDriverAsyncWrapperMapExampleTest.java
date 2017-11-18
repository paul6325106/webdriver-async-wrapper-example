package paul6325106.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WebDriverAsyncWrapperMapExampleTest {

    private WebDriver webDriver;

    @Before
    public void setUp() throws Exception {
        // assumes webdriver.firefox.bin and webdriver.gecko.driver have been set correctly
        webDriver = new FirefoxDriver();
    }

    @After
    public void tearDown() throws Exception {
        webDriver.quit();
    }

    @Test
    public void testWithoutInitialiseJavascript() throws Exception {
        try {
            new WebDriverAsyncWrapperMapExample(webDriver, 5000).getMessage();
            fail("Expected NullPointerException");
        } catch (final NullPointerException e) {}
    }

    @Test
    public void testTimeout() throws Exception {
        try {
            final WebDriverAsyncWrapper wrapper = new WebDriverAsyncWrapperMapExample(webDriver, 500);
            wrapper.initialiseJavascript();
            wrapper.getMessage();
            fail("Expected timeout");
        } catch (final WebDriverException e) {
            assertTrue(e.getMessage().contains("Timed out"));
        }
    }

    @Test
    public void testJavaSideErrorCatching() throws Exception {
        try {
            final WebDriverAsyncWrapper wrapper = new WebDriverAsyncWrapperMapExample(webDriver, 500);
            wrapper.initialiseJavascript();
            wrapper.getMessage(null);
            fail("Expected error");
        } catch (final JavascriptException e) {
            assertTrue(e.getMessage().contains("Name is defined, but blank"));
        }
    }

    @Test
    public void testWithoutArgument() throws Exception {
        final WebDriverAsyncWrapper wrapper = new WebDriverAsyncWrapperMapExample(webDriver, 5000);
        wrapper.initialiseJavascript();
        assertEquals("Hello stranger!", wrapper.getMessage());
    }

    @Test
    public void testWithArgument() throws Exception {
        final WebDriverAsyncWrapper wrapper = new WebDriverAsyncWrapperMapExample(webDriver, 5000);
        wrapper.initialiseJavascript();
        assertEquals("Hello Charlie!", wrapper.getMessage("Charlie"));
    }
}
