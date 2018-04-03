---
services: active-directory, Microsoft Graph API
platforms: java
author: austin
---

# Integrating the Microsoft Graph API into a Java command line using username and password

This sample demonstrates a command line application calling the Microsoft Graph API that is secured using Azure AD. 
The Java application uses the [ScribeJava Authentication Library](https://github.com/scribejava/scribejava)  to obtain a JWT access token through the OAuth 2.0 protocol. 
The access token is sent to the Microsoft Graph API to authenticate the user and get access to the service. 
This sample shows you how to use **ScribeJava** to authenticate users via raw credentials (username and password) 
using a text-only interface.


## Quick Start

Getting started with the sample is easy. It is configured to run out of the box with minimal setup.

### Step 1: Register an Azure AD Tenant

To use this sample you will need a Azure Active Directory Tenant. If you're not sure what a tenant is or how you would get one, read [What is an Azure AD tenant](http://technet.microsoft.com/library/jj573650.aspx)? or [Sign up for Azure as an organization](http://azure.microsoft.com/documentation/articles/sign-up-organization/). These docs should get you started on your way to using Azure AD.

### Step 2: Download Java (7 and above) for your platform 

To successfully use this sample, you need a working installation of [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Maven](https://maven.apache.org).

### Step 3: Download the Sample application and modules

Next, clone the sample repo and install the project's dependencies.

From your shell or command line:

* `$ git@github.com:microsoftgraph/console-java-connect-sample.git`
* `$ cd Java-Native-Console-Connect`

### Step 4: Register the GraphClient app


1. Sign in to the [Application Registration portal](https://apps.dev.microsoft.com).
3. Click on the blue **Add an app** button on the right side of the page.
4. Give your application a name.
4. _Un_-check **Let us help you get started** under the **Guided Setup** section
4. Click on the blue **Generate new password** under the **Application Secrets** section
4. Copy the generated password from the **New password generated** dialog to the system clipboard
4. Paste the password **.\Java-Native-Console-Connect\src\main\java\PublicClient.java**, line 17 to replace `ENTER_YOUR_CLIENT_SECRET`.
5. Click the **Add Platform** button under the **Platforms** section
6. While still in the Azure portal, choose your application, click on **Settings** and choose **Properties**.
7. Find the Application ID value and copy it to the clipboard.
8. Configure Permissions for your application - in the Settings menu, choose the 'Required permissions' section, click on **Add**, then **Select an API**, and select 'Microsoft Graph' (this is the Graph API). Then, click on  **Select Permissions** and select 'Sign in and read user profile'.

### Step 5: Configure your web app using `PublicClient.java`

Enter the client id value above at the top of the `PublicClient.java` file.

```java
public class PublicClient {

    private final static String AUTHORITY = "https://login.microsoftonline.com/common/";
    private final static String CLIENT_ID = "<your client id>";

//...
}
```

### Step 6: Package and then run the `public-client-adal4j-sample-jar-with-dependencies.jar ` file.

From your shell or command line:

* `$ mvn package`

This will generate a `public-client-adal4j-sample-jar-with-dependencies.jar` file in your /targets directory. Run this using your Java executable like below:

* `$ java -jar public-client-adal4j-sample-jar-with-dependencies.jar`


### You're done!

Your command line interface should prompt you for the username and password and then access the Graph API to retreive your user information.

### Acknowledgements

We would like to acknowledge the folks who own/contribute to the following projects for their support of Azure Active Directory and their libraries that were used to build this sample. In places where we forked these libraries to add additional functionality, we ensured that the chain of forking remains intact so you can navigate back to the original package. Working with such great partners in the open source community clearly illustrates what open collaboration can accomplish. Thank you!


