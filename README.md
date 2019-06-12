# Integrate the Microsoft Graph API into a Java command line app using username and password

This sample shows a command line app that calls the Azure AD-secured Microsoft Graph API. The app uses the [ScribeJava Authentication Library](https://github.com/scribejava/scribejava) to obtain a JSON Web Token (JWT) through the OAuth 2.0 protocol. The returned JWT is known as an **access token**. The access token is added to all requests on the Microsoft Graph API as an HTTP header. It authenticates the user and gets access to the service.

This sample shows you how to use **ScribeJava** to authenticate users via simple credentials (username and password) using a text-only interface.

## Quick Start

Getting started with the sample is easy. It is configured to run out of the box with minimal setup.

### Step 1: Register an Azure AD Tenant

To use this sample you need a Azure Active Directory Tenant. If you're not sure what a tenant is or how you would get one, read [What is an Azure AD tenant](http://technet.microsoft.com/library/jj573650.aspx)? or [Sign up for Azure as an organization](http://azure.microsoft.com/documentation/articles/sign-up-organization/). These docs should get you started on your way to using Azure AD.

### Step 2: Download Java (7 and above) for your platform

To use this sample, you need a working installation of [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Gradle](https://gradle.org/).

The sample was built using the Gradle plugin for IntelliJ IDEA IDE. The project build.gradle file is configured to let you run the sample from within IntelliJ. The run build configuration lets you run or debug the sample.

### Step 3: Download the Sample application and modules

Next, clone the sample repository and install the project's dependencies.

From your shell or command line:

* `$ git clone git@github.com:microsoftgraph/console-java-connect-sample.git`
* `$ cd console-java-connect-sample`

### Step 4: Register the Console Java Connect app

1. Sign into [Azure Portal - App Registrations](https://go.microsoft.com/fwlink/?linkid=2083908) using either your personal or work or school account.

2. Choose **New registration**.

3. In the **Name** section, enter a meaningful application name that will be displayed to users of the app

1. In the **Supported account types** section, select **Accounts in any organizational directory and personal Microsoft accounts (e.g. Skype, Xbox, Outlook.com)**  

1. Select **Register** to create the application. 
	
   The application's Overview page shows the properties of your app.

4. Copy the **Application (client) Id**. This is the unique identifier for your app. 

1. In the application's list of pages, select **Authentication**.

1. Under **Redirect URIs** in the **Suggested Redirect URIs for public clients (mobile, desktop)** section, check the box next to **https://login.microsoftonline.com/common/oauth2/nativeclient**

8. Choose **Save**.

> **Note:** The Azure Active Directory v2.0 authorization endpoint uses [incremental and dynamic consent](https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-v2-compare#incremental-and-dynamic-consent) which means that you don't need to set permissions (now called **scopes**) when you register your application. Scopes are requested by the your app at run-time.

### Step 5: Configure your app using Constants.java

Using your favorite code editor, open **..\console-java-connect-sample\src\main\java\com\microsoft\graphsample\connect\Constants.java**. Paste the Application Id from the clipboard into Constants.java, line 11 to replace `ENTER_YOUR_CLIENT_ID`.

```java
public class Constants {

    public final static String CLIENT_ID = "ENTER_YOUR_CLIENT_ID";

//...
}
```

### Step 6: Install Gradle

If you do not have the Gradle build system installed, [install Gradle](https://docs.gradle.org/4.6/userguide/installation.html).

### Step 7: Add the Gradle wrapper to the project

From your shell or command line at the project root:

```Shell
gradle wrapper
```

### Step 8: Build and run the sample

From your shell or command line at the project root:

```Shell
gradle run
```

This will run the sample.

### Running the sample

The command line interface opens a browser window on the Azure Active Directory authorization endpoint. Enter your user name and password to authenticate. When you are authenticated, you're taken to an authorization window for the sample app. Review and accept the scopes requested by the sample app. Click the Ok button on the authorization window. The browser navigates to `https://login.microsoftonline.com`. Copy the full redirect URL address and paste it into a text editor.  It should look like the following url:

```http
https://login.microsoftonline.com/common/oauth2/nativeclient?code={IAQABAAIAAABHh4kmS_aKT5XrjzxRAtHz5S...p7OoAFPmGPqIq-1_bMCAA}&session_state=dd64ce71-4424-494b-8818-be9a99ca0798
```

> **Note:** URL example is truncated for clarity. `{` and `}` were added to the example to delimit the authorization code for illustration purposes.

The authorization code starts after `?code=` and ends before `&session_state`.

Copy the authorization code to the system clipboard and paste it into the console under `And paste the authorization code here`.

You are prompted with:

```Shell
Hello, {your name}. Would you like to send an email to yourself or someone else?
Enter the address to which you'd like to send a message. If you enter nothing, the message will go to your address
```

After you enter an email address or leave the prompt blank, an email is sent to you or the address you type into the console.

You can send email to other addresses by responding with a "y" to the prompt `Want to send another message? Type 'y' for yes and any other key to exit.`
