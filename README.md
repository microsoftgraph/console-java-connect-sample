---
services: active-directory, Microsoft Graph API
platforms: java
author: austin
---

# Integrate the Microsoft Graph API into a Java command line app using username and password

This sample shows a command line app that calls the Azure AD-secured Microsoft Graph API.
The Java application uses the [ScribeJava Authentication Library](https://github.com/scribejava/scribejava) to obtain a
base 64 encoded JSON Web Token (JWT) through the OAuth 2.0 protocol. The returned JWT is known as an **access token**.
The access token is added to all requests on the Microsoft Graph API as an HTTP header. It authenticates the user and gets access to the service.
This sample shows you how to use **ScribeJava** to authenticate users via simple credentials (username and password)
using a text-only interface.


## Quick Start

Getting started with the sample is easy. It is configured to run out of the box with minimal setup.

### Step 1: Register an Azure AD Tenant

To use this sample you need a Azure Active Directory Tenant. If you're not sure what a tenant is or how you would get one, read [What is an Azure AD tenant](http://technet.microsoft.com/library/jj573650.aspx)? or [Sign up for Azure as an organization](http://azure.microsoft.com/documentation/articles/sign-up-organization/). These docs should get you started on your way to using Azure AD.

### Step 2: Download Java (7 and above) for your platform 

To use this sample, you need a working installation of [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Maven](https://maven.apache.org).

### Step 3: Download the Sample application and modules

Next, clone the sample repo and install the project's dependencies.

From your shell or command line:

* `$ git@github.com:microsoftgraph/console-java-connect-sample.git`
* `$ cd console-java-connect-sample`

### Step 4: Register the Console Java Connect app


1. Sign in to the [Application Registration portal](https://apps.dev.microsoft.com).
3. Click on the blue **Add an app** button on the right side of the page.
4. Give your application a name.
4. _Un_-check **Let us help you get started** under the **Guided Setup** section
4. Copy the **Application Id** of the new app registration to the clipboard
5. Click the **Add Platform** button under the **Platforms** section and an **Add Platform** window opens.
6. Choose **Native Application** in the Add Platform window.

> **Note:** The Azure Active Directory v2.0 authorization endpoint uses [incremental and dynamic consent](https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-v2-compare#incremental-and-dynamic-consent)
> which means that you don't need to set permissions (now called **scopes**) when you register your application. Scopes are requested by the
> your app at run-time.

### Step 5: Configure your web app using `Constants.java`

Paste the Application Id from the clipboard into **com/microsoft/graphsample/connect/Constants.java**, line 11 to replace `ENTER_YOUR_CLIENT_ID`.

```java
public class Constants {

    public final static String CLIENT_ID = "ENTER_YOUR_CLIENT_ID";

//...
}
```

### Step 6: Package and then run the `Java-Native-Console-Connect.jar ` file.

From your shell or command line:

* `$ mvn package`

This will generate a `Java-Native-Console-Connect.jar` file in your /out directory. Run this using your Java executable like below:

* `$ java -jar Java-Native-Console-Connect.jar`


### You're done!

Your command line interface should prompt you for the username and password and then access the Graph API to retreive your user information.

### Acknowledgements

We would like to acknowledge the folks who own/contribute to the following projects for their support of Azure Active Directory and their libraries that were used to build this sample. In places where we forked these libraries to add additional functionality, we ensured that the chain of forking remains intact so you can navigate back to the original package. Working with such great partners in the open source community clearly illustrates what open collaboration can accomplish. Thank you!


