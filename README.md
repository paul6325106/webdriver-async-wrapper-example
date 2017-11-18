# WebDriver Asynchronous Wrapper Example

APIs that rely on callbacks can be a little more verbose than plainer APIs.
It can be beneficial to move the JavaScript calls out of Java string processing and into native JavaScript, where it is
easier to write and maintain.

Generally speaking, there are two ways to do this.

The first is accomplished by injecting a namespace of simple wrappers to the JavaScript window scope.
This technically involves modifying the system under test, but not in a significant way.
All new JavaScript should be isolated in a namespace or module to avoid polluting the system under test.

The second is to execute specific JavaScript scripts as needed, as you would with simpler synchronous scripts.
The JavaScript file can be parsed and loaded into a Map to allow verbose async calls to be called with a key.
The trade-off is that while the system under test is not technically modified, the JavasScript file needs to be
formatted in particular ways to ensure it is parsable and still valid for linting.