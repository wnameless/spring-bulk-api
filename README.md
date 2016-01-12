[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.wnameless.spring/spring-bulk-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wnameless.spring/spring-bulk-api)

spring-bulk-api
=============
Add bulk operations support to any Spring RESTful API by a single annotation @EnableBulkApi.

##Purpose
Allow user to use JSON to define a list of RESTful operations,<br />
and send to server-side for bulk actions.

##Note
For simplicity, spring-bulk-api doesn't deal with any of security issues.<br />
Server-side should set up the Spring security for safety concerns.<br />
Ex: enable basic authentication and SSL, disable CSRF protection... etc.

#Maven Repo
```xml
<dependency>
	<groupId>com.github.wnameless.spring</groupId>
	<artifactId>spring-bulk-api</artifactId>
	<version>0.5.0</version>
</dependency>
```


###Quick Start

Add @EnableBulkApi to enable bulk API
```java
@Configuration
@EnableBulkApi
public class WebConfig {
  ...
}
```

Since v0.4.0, @Bulkable is required to be annotated on the controller which accepts bulk operations
```java
@Bulkable
@RestController
public class HomeController {
  ...
}
```

Since v0.5.0, @AcceptBulk is added to provide more controls
```java
@Bulkable(autoApply=false)
@RestController
public class HomeController {

  // Not allow bulk operations
  @RequestMapping("/index")
  public void index() {
    ...
  }

  // Allow bulk operations
  @AcceptBulk
  @RequestMapping("/home")
  public void home() {
    ...
  }
  ...
}
```

By default path is /bulk and limit to 100 operations,<br />
it can be configured by Spring application.properties
```properties
spring.bulk.api.path=/batch # default is /bulk
spring.bulk.api.limit=200 # default is 100
```

Request JSON sample
```json
# POST /bulk
# Content-Type: application/json

{
  "operations": [
    {"method": "GET", "url": "/home"},
    {"method": "POST", "url": "/posts/new", "params": {"title": "My Dream"}},
    {"method": "DELETE", "url": "/posts/123", "headers": {"Authentication": "Basic ..."}}
  ]
}

```
+ url - the API endpoint. (required)
+ method - the HTTP method(GET, POST, DELETE ...etc.) Default is GET. (optional)
+ params - the HTTP parameters to the API. (optional)
+ headers - a hash of of headers which should be included in this operation. (optional)
+ silent - if it's set to true, there is no result created in the response for this operation. (optional)

Response JSON sample
```json
{
  "results": [
    {"status": 200, "body": "Welcome!", "headers": {}},
    {"status": 201, "body": {"id": 222, "title": "My Dream"}, "headers": {}}
  ]
}
```
+ status - the HTTP status.
+ body - the response body.
+ headers - the headers of single result.

BulkApiService
```java
@Autowired
@Bean
public BulkApiService bulkApiService(ApplicationContext appCtx) {
  return new DefaultBulkApiService(appCtx)
}
```
By default, the DefaultBulkApiService will be autowired automatically, you don't need to provide it.<br/>
However you can extend the DefaultBulkApiService to provide your custom implementation or implement the BulkApiService interface by your own.