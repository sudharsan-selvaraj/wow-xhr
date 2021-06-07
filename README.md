<p align="center">
<img src="./assets/logo.png" height=150/>
</p>

<p align="center">
   <i><strong>Simple library to manipulate HTTP requests and responses, capture the network logs made by the browser using selenium tests without using any proxies </strong></i>
<p>

Although selenium is used for e2e and especially UI testing, you might want to mock certain HTTP response to test few
error scenarios and to assess HTTP requests done by your client code (e.g. when you don't have immediate UI feedback,
like in metrics or tracking calls).

There's one catch though: you can't intercept HTTP calls that are initiated on page load (like in most SPAs), as it
requires some setup work that can only be done after the page is loaded (due to limitations in selenium). That means you
can just capture requests that were initiated inside a test. If you're fine with that, this plugin might be for you, so
read on.

## Installation

### Maven

```xml

<dependency>
    <groupId>io.github.sudharsan-selvaraj</groupId>
    <artifactId>wow-xhr</artifactId>
    <version>1.0.0</version>
</dependency> 
```

### Gradle

```groovy
implementation group: 'io.github.sudharsan-selvaraj', name: 'wow-xhr', version: '1.0.0'
```

Also while downloading selenium, make sure to exclude `net.bytebuddy:byte-buddy` library by using

### Maven
```xml
<dependency>
   <groupId>org.seleniumhq.selenium</groupId>
   <artifactId>selenium-java</artifactId>
   <version>3.141.59</version>
   <exclusions>
      <exclusion>
         <groupId>net.bytebuddy</groupId>
         <artifactId>byte-buddy</artifactId>
      </exclusion>
   </exclusions>
</dependency>
```

### Gradle
```groovy
implementation (group: 'org.seleniumhq.selenium', name: 'selenium-java', version: '3.141.59') {
   exclude group: 'net.bytebuddy', module: 'byte-buddy'
 }
```

## Features:

1. Intercept any HTTP request and modify the header, query param, body of the request.
2. Modify the header, body, status of the HTTP response.
3. Get the complete details of all API calls made by the browser along with the header, body, initiated time and
   completed time.
4. Add a delay to any API call to simulate network speed for specific API call.

## Quickstart

Create wowXhr object,

```java
WowXHR wowXhr = new WowXHR(new ChromeDriver()); //any selenium webdriver or RemoteWebDriver
WebDriver driver = wowXhr.getMockDriver()
```

That's it. Now the driver object can be used in the test.
No extra configurations required for browser running on remote machine or browserstack or saucelabs.

## Mock

### Modify HTTP request

#### Add or update header
```java 
import static io.github.sudharsan_selvaraj.wowxhr.mock.Mockable.*;

driver.get("http://localhost:3000");

wowXHR.mock().add(
        whenGET("/api")
        .modifyRequest(
                mockRequest()
                .setHeader("Authorization", "invalid-auth-token")
        )
);
```

Above mock will update the `Authorization` header with `invalid-auth-token` for all HTTP request that has `/api` regular expression in its url.

#### Add or update query param
```java 
import static io.github.sudharsan_selvaraj.wowxhr.mock.Mockable.*;

driver.get("http://localhost:3000");

wowXHR.mock().add(
        whenGET("/api/users")
        .modifyRequest(
                mockRequest()
                .setQueryParam("filter_fname", "sudharsan")
        )
);
```
Above mock will add `?filter_fname=sudharsan` or update the value of `filter_fname` query param for all `/api/users` api requests.

#### Update the request body
```java 
import static io.github.sudharsan_selvaraj.wowxhr.mock.Mockable.*;

driver.get("http://localhost:3000");

wowXHR.mock().add(
        whenPOST("/api/login")
        .modifyRequest(
                mockRequest()
                .setBody("{\"email\":\"invalidemail@emial.com\",\"password\":\"somepassword\"}")
        )
);
```
Above mock will update the request body with `{\"email\":\"invalidemail@emial.com\",\"password\":\"somepassword\"}` for all subsequent `/api/login` requests. 


### Modify HTTP response

#### Add or update header
```java 
import static io.github.sudharsan_selvaraj.wowxhr.mock.Mockable.*;

driver.get("http://localhost:3000");

wowXHR.mock().add(
        whenPOST("/api/login")
        .respond(
            mockResponse()
            .withHeader("Set-Cookie", "refresh-token=invalid-refresh-token; expires=Mon, 17-Jul-2021 16:06:00 GMT; Max-Age=31449600; Path=/; secure")
         )
);
```
Above mock will update the response header and add's the new cookie to the browser.

#### Update the response body
```java 
import static io.github.sudharsan_selvaraj.wowxhr.mock.Mockable.*;

driver.get("http://localhost:3000");

wowXHR.mock().add(
        whenPOST("/api/login")
        .respond(
            mockResponse()
            .withBody("{\"error\": \"true\" ,\"message\" : \"User account is locked\"}")
         )
);
```
Above mock will update the response body with `"{\"error\": \"true\" ,\"message\" : \"User account is locked\"}"` for all login api calls.

#### update the response status
```java 
import static io.github.sudharsan_selvaraj.wowxhr.mock.Mockable.*;

driver.get("http://localhost:3000");

wowXHR.mock().add(
        whenPOST("/api/login")
        .respond(
            mockResponse()
            .withStatus(500) //internal server error
         )
);
```
Above mock will change the response status code to `500` and makes the application to assume that the API call has been failed.

#### update the response status
```java 
import static io.github.sudharsan_selvaraj.wowxhr.mock.Mockable.*;

driver.get("http://localhost:3000");

wowXHR.mock().add(
        whenPOST("/api/login")
        .respond(
            mockResponse()
            .withDelay(10) //in seconds
         )
);
```
Above mock will add a delay of 10 seconds before the response is returned to the application. 
This will be very useful in testing any loading UI elements that is displayed when API calls are made.

### Pausing and Resume the mock

At any point in the test if you decide to pause the mocking, then it can be achieved using
```java
 wowhXhr.mock().pause();
```

and you can resume using,

```java
wowhXhr.mock().resume();
```

## Logs

### Get XHR logs

```java
List<XHRLog> logs = wowXHR.log().getXHRLogs();
logs.forEach(log -> {
        Date initiatedTime =  log.getInitiatedTime();
        Date completedTime =  log.getCompletedTime();

        String method = log.getRequest().getMethod().name(); // GET or POST or PUT etc
        String url = log.getRequest().getUrl();
        Integer status = log.getResponse().getStatus();
        String requestBody = (String) log.getRequest().getBody();
        String responseBody = (String) log.getResponse().getBody();
        Map<String, String> requestHeaders = log.getRequest().getHeaders();
        Map<String, String> responseHeaders = log.getResponse().getHeaders();
});
```

Note: By default `wowXHR.log().getXHRLogs()` will clear the previously fetched logs. If you want the logs to be preserved, then you can use the overloaded method 
`wowXHR.log().getXHRLogs(false)`

### Manually clear the logs

```java
 wowXHR.log().clear();
```

## Upcoming features

1. Support for making the tests to wait for all api calls to get completed (similar to
   protractor's `waitForAngular`)
2. Ability to mock the HTTP requests that are triggered during initial page load.
3. Currently, the request can be mocked based on its URL only(Regex). In the future, we will also support for finding requests using query params(partial and exact) and body(partial and exact). 
4. TBD