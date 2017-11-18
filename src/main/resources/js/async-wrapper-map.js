// you'll want to split your scripts with something obvious, easy to parse, and identifying a key

// MAP - getMessage

// wrapping your code in an IIFE will make linting this file easier
(function(arguments) {

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

})(arguments);

// MAP - someOtherScript

(function(arguments) {
    throw new Error("This should not be executed");
})(arguments);
