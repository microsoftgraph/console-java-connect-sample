package com.microsoft.graphsample.connect;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

public class MicrosoftAzureAD20Api extends DefaultApi20 {

    private MicrosoftAzureAD20Api() {
    }

    private static class InstanceHolder {
        static MicrosoftAzureAD20Api INSTANCE = new MicrosoftAzureAD20Api();
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://login.microsoftonline.com/common/oauth2/v2.0/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://login.microsoftonline.com/common/oauth2/v2.0/authorize";
    }

    public static DefaultApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }
}
