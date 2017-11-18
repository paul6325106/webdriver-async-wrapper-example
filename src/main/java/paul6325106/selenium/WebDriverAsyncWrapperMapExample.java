package paul6325106.selenium;

import com.jayway.jsonpath.JsonPath;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebDriverAsyncWrapperMapExample implements WebDriverAsyncWrapper {

    private final static String RESOURCE = "js/async-wrapper-map.js";

    private final WebDriver webDriver;
    private final JavascriptExecutor javascriptExecutor;
    private final long defaultTimeoutMs;

    // there will need to be some consistent pattern separating each script and identifying it with a key
    private final Pattern pattern = Pattern.compile("// MAP - ([A-Za-z]+)\\s*((?:(?!// MAP).)*)", Pattern.DOTALL);

    private Map<String, String> scripts;

    public WebDriverAsyncWrapperMapExample(final WebDriver webDriver, final long defaultTimeoutMs) {
        this.webDriver = webDriver;
        this.javascriptExecutor = (JavascriptExecutor) webDriver;
        this.defaultTimeoutMs = defaultTimeoutMs;
    }

    @Override
    public void initialiseJavascript() {
        scripts = new HashMap<>();

        try {
            final URL resource = getClass().getClassLoader().getResource(RESOURCE);
            if (resource == null) {
                throw new RuntimeException("Unable to access resource: " + RESOURCE);
            }

            final String allScripts = new String(Files.readAllBytes(Paths.get(resource.toURI())));

            final Matcher matcher = pattern.matcher(allScripts);

            while (matcher.find()) {
                scripts.put(matcher.group(1), matcher.group(2));
            }
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
     * Retrieves the script from the map by method name.
     * @see JavascriptExecutor#executeAsyncScript(String, Object...)
     */
    private Object executeWrapperMethod(final String method, final Object ... args) {
        return javascriptExecutor.executeAsyncScript(scripts.get(method), args);
    }

    @Override
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

    @Override
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
