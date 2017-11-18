// you'll want to put the wrapper methods into a namespace to avoid polluting the global scope
window.webDriverAsyncWrapperExample = window.webDriverAsyncWrapperExample || {};

// each wrapper method should take the arguments object injected by Selenium
window.webDriverAsyncWrapperExample.getMessage = function(arguments) {

    // the last element of arguments will be the callback to indicate return, even if no arguments were provided
    var name = (arguments.length == 1) ? 'stranger' : arguments[0];
    var onComplete = arguments[arguments.length - 1];

    // exceptions can be caught Java side as JavascriptException
    if (!name) {
        throw new Error("Name is defined, but blank");
    }

    // this is a trivial asynchronous call, in practice this would be a call to some API
    // but note the use of setTimeout over setInterval in this example
    // setInterval would call the Selenium callback repeatedly
    // which would interfere with later executions of executeAsyncScript
    setTimeout(function() {
        response = { 'message': 'Hello ' + name + '!', 'status': 'OK'};

        // inside the asynchronous callback, we can call the Selenium callback
        // complex API responses could be parsed here or Java-side, depending on the data actually required
        onComplete(JSON.stringify(response));
    }, 2500);
}
