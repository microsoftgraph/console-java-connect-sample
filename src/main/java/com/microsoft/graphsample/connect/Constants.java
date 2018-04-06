/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license.
 * See LICENSE in the project root for license information.
 */
package com.microsoft.graphsample.connect;

public class Constants {

    public static final String DEFAULT_IMAGE_FILENAME = "test.jpg";
    public static final String NETWORK_NAME = "Microsoft Azure Active Directory";
    public final static String CLIENT_ID = "e4724c3d-bc26-4826-872c-eaa7ca228090";
    public final static String REDIRECT_URL = "http://localhost";
    public final static String SCOPES = "Files.ReadWrite openid User.Read Mail.Send Mail.ReadWrite";
    public static final String PROTECTED_RESOURCE_URL = "https://graph.microsoft.com/v1.0/me";

    public static final String SUBJECT_TEXT = "Welcome to Microsoft Graph development for Java with the Connect sample";

    // The Microsoft Graph delegated permissions that you set in the application
    // registration portal must match these scope values.
    // Update this constant with the scope (permission) values for your application:
    public static final String MESSAGE_BODY = "<html><head>\n" +
                                               "<meta http-equiv=\'Content-Type\' content=\'text/html; charset=us-ascii\'>\n" +
                                               "<title></title>\n" +
                                               "</head>\n" +
                                               "<body style=\'font-family:calibri\'>\n" +
                                               "<h2>Congratulations!</h2>\n" +
                                               "<p>This is a message from the Microsoft Graph Connect Sample. You are well on your way to incorporating Microsoft Graph endpoints in your apps.</p><a href=%s>See the photo you just uploaded!</a>\n" +
                                               "<h3>What\'s next?</h3><ul>\n" +
                                               "<li>Check out <a href=\'https://developer.microsoft.com/graph\'>developer.microsoft.com/graph</a> to start building Microsoft Graph apps today with all the latest tools, templates, and guidance to get started quickly.</li>\n" +
                                               "<li>Use the <a href=\'https://developer.microsoft.com/graph/graph-explorer\'>Graph Explorer</a> to explore the rest of the APIs and start your testing.</li>\n" +
                                               "<li>Browse other <a href=\'https://github.com/search?p=5&amp;q=org%3Amicrosoftgraph+sample&amp;type=Repositories&amp;utf8=%E2%9C%93\'>samples on GitHub</a> to see more of the APIs in action.</li>\n" +
                                               "</ul>\n" +
                                               "<h3>Give us feedback</h3>\n" +
                                               "<p>If you have any trouble running this sample, please <a href=\'https://github.com/microsoftgraph/uwp-csharp-connect-sample/issues\'>\n" +
                                               "log an issue</a> on our repository.</p><p>For general questions about the Microsoft Graph API, post to <a href=\'https://stackoverflow.com/questions/tagged/microsoftgraph\'>Stack Overflow</a>. Make sure that your questions or comments are tagged with [microsoftgraph].</p>\n" +
                                               "<p>Thanks, and happy coding!<br>\n" +
                                               "&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Your Microsoft Graph samples development team </p>\n" +
                                               "<div style=\'text-align:center; font-family:calibri\'>\n" +
                                               "<table style=\'width:100%; font-family:calibri\'>\n" +
                                               "<tbody>\n" +
                                               "<tr>\n" +
                                               "<td><a href=\'https://github.com/microsoftgraph/uwp-csharp-connect-sample\'>See on GitHub</a>\n" +
                                               "</td>\n" +
                                               "<td><a href=\'https://office365.uservoice.com\'>Suggest on UserVoice</a>\n" +
                                               "</td>\n" +
                                               "<td><a href=\'https://twitter.com/share?text=I%20just%20started%20developing%20apps%20for%20%23ASP.NET%20using%20the%20%23MicrosoftGraph%20Connect%20app%20%40OfficeDev&amp;amp;url=https://github.com/microsoftgraph/uwp-csharp-connect-sample\'>Share on Twitter</a>\n" +
                                               "</td>\n" +
                                               "</tr>\n" +
                                               "</tbody>\n" +
                                               "</table>\n" +
                                               "</div>\n" +
                                               "</body>\n" +
                                               "</html>";

}
