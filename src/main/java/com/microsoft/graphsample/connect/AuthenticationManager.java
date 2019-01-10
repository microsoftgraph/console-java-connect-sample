/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license.
 * See LICENSE in the project root for license information.
 */
package com.microsoft.graphsample.connect;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.microsoft.graph.logger.LoggerLevel;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.Scanner;

import static java.awt.Desktop.getDesktop;
import static java.awt.Desktop.isDesktopSupported;

/**
 * Handles setup of OAuth library in API clients.
 */
public class AuthenticationManager {

    private static AuthenticationManager INSTANCE;
    private OAuth20Service mOAuthService = null;
    private OAuth2AccessToken mAccessToken;

    /**
     * Initialization block. Runs before constructor to get a logger and start up the ScribeJava OAuth2
     * authentication service
     */
    {
        if (Debug.DebugLevel == LoggerLevel.DEBUG) {
            DebugLogger.getInstance().writeLog(Level.INFO, "AuthenticationManager initialization block called");

            try (OAuth20Service service = new ServiceBuilder(Constants.CLIENT_ID)
                    .callback(Constants.REDIRECT_URL)
                    .scope(Constants.SCOPES)
                    .apiKey(Constants.CLIENT_ID)
                    .debugStream(System.out)
                    .debug()
                    .build(MicrosoftAzureAD20Api.instance())
            ) {
                mOAuthService = service;
            } catch (java.io.IOException | IllegalArgumentException ex) {
                try {
                    throw ex;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            try (OAuth20Service service = new ServiceBuilder(Constants.CLIENT_ID)
                    .callback(Constants.REDIRECT_URL)
                    .scope(Constants.SCOPES)
                    .apiKey(Constants.CLIENT_ID)
                    .build(MicrosoftAzureAD20Api.instance())
            ) {
                mOAuthService = service;
            } catch (java.io.IOException | IllegalArgumentException ex) {
                try {
                    throw ex;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private AuthenticationManager() throws IOException {
        DebugLogger.getInstance().writeLog(Level.INFO, "AuthenticationManager constructor called");
    }

    public static synchronized AuthenticationManager getInstance() throws java.io.IOException {
        if (INSTANCE == null) {
            INSTANCE = new AuthenticationManager();
        }
        return INSTANCE;
    }

    public OAuth20Service getOAuthService() {
        return mOAuthService;
    }

    public static synchronized void resetInstance() {
        INSTANCE = null;
    }

    public String getRefreshToken() {
        if (mAccessToken == null) {
            return "";
        }
        return mAccessToken.getRefreshToken();
    }

    public String getAccessToken() {
        if (mAccessToken == null) {
            return "";
        }
        return mAccessToken.getAccessToken();
    }

    /**
        This method implements the Authorization Grant flow for OAuth 2.0
        A user responds to the credentials challenge in the browser opened by this method. User
        types their credentials in the browser window and is redirected to an authorization page.
        User accepts the sample app's requests to access Microsoft Graph resources and an
        authorization token is returned to the callback url.
        In a breakpoint before the service.getAccessToken call, update the authorizationCode string variable
        with the authorization code from the POST to the callback url.

        @throws URISyntaxException
        @throws IOException
        @throws InterruptedException
        @throws ExecutionException

     */

    public void connect(Scanner inputScanner) throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        try {
            mAccessToken = mOAuthService.getAccessToken(getAuthorizationCode(inputScanner));
            showAuthTokenToUser();
            makeAuthenticatedMeRequest();
        } finally {
        }
    }


    /**
     * Connects the user to Microsoft Graph API asynchronously
     *
     * @param callback handles the authentication result.
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Future<OAuth2AccessToken> connectAsync(
            Scanner inputScanner,
            IConnectCallback callback) throws
            URISyntaxException,
            IOException,
            InterruptedException,
            ExecutionException
    {
        Future<OAuth2AccessToken> future = null;
        try {
            final String code = getAuthorizationCode(inputScanner);
            future = mOAuthService
                    .getAccessToken(code, new OAuthAsyncRequestCallback<OAuth2AccessToken>() {

                        @Override
                        public void onCompleted(OAuth2AccessToken oAuth2AccessToken) {
                            mAccessToken = oAuth2AccessToken;
                            showAuthTokenToUser();

                            try {
                                callback.onCompleted();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onThrowable(Throwable throwable) {
                            callback.onThrowable(throwable);
                        }
                    }, Constants.CLIENT_ID);
            return future;

        } finally {

        }
    }


    /**
     * Disconnects the app from Office 365 by clearing the token cache, setting the client objects
     * to null, and removing the user id from shred preferences.
     */
    public void disconnect() throws Exception {
        try {
            // Commented out - ScribeJava does not support revoking access tokens yet.
            //  mOAuthService.revokeToken(mAccessToken.getAccessToken());
        } finally {
        }
    }

    /**
     * Displays the raw token to the user on the system console
     */
    private void showAuthTokenToUser() {
        System.out.println("Got the Access Token!");
        System.out.println(
                "(if you're curious the raw answer looks like this: " + mAccessToken.getRawResponse() + "')");
        System.out.println();
        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
    }

    /**
     * Creates and runs REST authenticated request for Me resource synchronously. The response includes a
     * JSON payload with a representation of the me resource.
     *
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    private void makeAuthenticatedMeRequest() throws InterruptedException, ExecutionException, IOException {
        final OAuthRequest request = new OAuthRequest(Verb.GET, Constants.PROTECTED_RESOURCE_URL);
        mOAuthService.signRequest(mAccessToken, request);
        request.addHeader("Accept", "application/json, text/plain, */*");
        final Response response = mOAuthService.execute(request);
        System.out.println("Got it! Let's see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        System.out.println();
        System.out.println("Thats it! Go and build something awesome with Microsoft Graph! :)");
    }

    /**
     * Gets the user authorization grant flow URL, opens a browser tab with the resulting authorization token
     * embedded in the address URL. User copies the URL, extracts the token, and pastes it into the system console.
     * The Scanner reads the authorization code from the system console and returns the value to calling code.
     *
     * The authorization code is returned when the user accepts the sample app's requests to access Microsoft Graph
     * resources. The authorization code lists the Microsoft Graph resources that the user has given the sample
     * app permission to access.
     *
     * @return Authorization code
     * @throws IOException
     * @throws URISyntaxException
     */
    private String getAuthorizationCode(Scanner inputScanner) throws IOException, URISyntaxException {
        System.out.println("=== " + Constants.NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();
        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = mOAuthService.getAuthorizationUrl();
        System.out.println("Got the Authorization URL!");
        if (isDesktopSupported()) {
            getDesktop().browse(new URI(authorizationUrl));
        }
        else {
            System.out.println("Now go and authorize Java-Native-Console-Connect here:");
            System.out.println(authorizationUrl);
        }
        String code = "";
        System.out.println("And paste the authorization code here");
        try {
            code = inputScanner.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        return code;
    }
}
