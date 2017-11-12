package paul6325106.selenium;

import com.jayway.jsonpath.JsonPath;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class WebDriverAsyncWrapperExample {

    private final static String RESOURCE = "js/async-wrapper.js";
    private final static String NAMESPACE = "webDriverAsyncWrapperExample";

    private final WebDriver webDriver;
    private final JavascriptExecutor javascriptExecutor;
    private final long defaultTimeoutMs;

    public WebDriverAsyncWrapperExample(final WebDriver webDriver, final long defaultTimeoutMs) {
        this.webDriver = webDriver;
        this.javascriptExecutor = (JavascriptExecutor) webDriver;
        this.defaultTimeoutMs = defaultTimeoutMs;
    }

    /**
     * Initialises the Javascript wrappers.
     * As you'd expect, none of the wrapper methods will work unless this is called on the current window.
     */
    public void initialiseJavascript() {
        try {
            final URL resource = getClass().getClassLoader().getResource(RESOURCE);
            if (resource == null) {
                throw new RuntimeException("Unable to access resource: " + RESOURCE);
            }

            final String script = new String(Files.readAllBytes(Paths.get(resource.toURI())));
            javascriptExecutor.executeScript(script);

        } catch (final URISyntaxException | IOException e) {
            throw new RuntimeException("Unable to access resource: " + RESOURCE, e);
        }
    }

    /**
     * Convenience method for setting script timeout.
     * @see WebDriver.Timeouts#setScriptTimeout(long, TimeUnit)
     */
    private void setScriptTimeout(final long time, final TimeUnit unit) {
        webDriver.manage().timeouts().setScriptTimeout(time, unit);
    }

    /**
     * Convenience method for executing wrapper methods.
     * Constructs the script from a method name.
     * @see JavascriptExecutor#executeAsyncScript(String, Object...)
     */
    private Object executeWrapperMethod(final String method, final Object ... args) {
        final String script = String.format("%s.%s(arguments);", NAMESPACE, method);
        return javascriptExecutor.executeAsyncScript(script, args);
    }

    /**
     * Call to the getMessage wrapper method without an argument.
     */
    public String getMessage() {
        setScriptTimeout(defaultTimeoutMs, TimeUnit.MILLISECONDS);

        final String result = (String) executeWrapperMethod("getMessage");

        final String status = JsonPath.read(result, "$.status");

        if (status.equals("OK")) {
            return JsonPath.read(result, "$.message");
        } else {
            return null;
        }
    }

    /**
     * Call to the getMessage wrapper method with an argument.
     */
    public String getMessage(final String name) {
        setScriptTimeout(defaultTimeoutMs, TimeUnit.MILLISECONDS);

        final String result = (String) executeWrapperMethod("getMessage", name);

        final String status = JsonPath.read(result, "$.status");

        if (status.equals("OK")) {
            return JsonPath.read(result, "$.message");
        } else {
            return null;
        }
    }

}
