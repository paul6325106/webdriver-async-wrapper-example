package paul6325106.selenium;

public interface WebDriverAsyncWrapper {

    /**
     * Initialises the Javascript wrappers.
     * As you'd expect, none of the wrapper methods will work unless this is called on the current window.
     */
    void initialiseJavascript();

    /**
     * Call to the getMessage wrapper method without an argument.
     */
    String getMessage();

    /**
     * Call to the getMessage wrapper method with an argument.
     */
    String getMessage(String name);
}
