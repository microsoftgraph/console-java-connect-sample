package com.microsoft.graphsample;

import com.gilecode.reflection.ReflectionAccessUtils;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graphsample.connect.AuthenticationManager;
import com.microsoft.graphsample.connect.DebugLogger;
import com.microsoft.graphsample.msgraph.GraphSendMail;
import com.microsoft.graphsample.msgraph.SendMailException;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PublicClient {
    protected static AuthenticationManager authenticationManager = null;
    DebugLogger mLogger;

    public PublicClient() throws IOException {
        mLogger = DebugLogger.getInstance();
    }

    public static void main(String args[]) throws Exception {
        try {
            PublicClient publicClient = new PublicClient();
            publicClient.startConnect();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());

        } finally {
        }
    }

    private void startConnect() throws Exception {
        System.out.println("Welcome to the Java Console Connect Sample!");

        //If this call is removed, functionality of the sample is not
        //affected. The call simply suppresses warnings generated from json deserialization
        //in the Graph SDK
        ReflectionAccessUtils.suppressIllegalReflectiveAccessWarnings();
        authenticationManager = AuthenticationManager.getInstance();
        authenticationManager.connect();
        startSendMail();
    }

    /**
     * Prompts user for email address to send mail and prompts
     * for additional email address to send mail. Continues until
     * user declines to send a mail.
     *
     * @throws Exception
     */
    private void startSendMail() throws Exception {
        GraphSendMail graphSendMail = new GraphSendMail();
        User          meUser        = null;
        try {
            meUser = graphSendMail.getMeUser();

            String  preferredName   = meUser.displayName;
            Boolean sendAnotherMail = true;
            while (sendAnotherMail) {
                System.out.println(
                        "Hello, " + preferredName + ". Would you like to send an email to yourself or someone else?");
                String sendMailAddress = "";
                sendMailAddress = PublicClient
                        .getUserInput("Enter the address to which you'd like to send a message. " +
                                      "If you enter nothing, the message will go to your address");
                sendMailAddress = sendMailAddress.isEmpty() ? meUser.mail : sendMailAddress;
                graphSendMail.sendMail(sendMailAddress, preferredName);

                String sendAnotherYN = PublicClient
                        .getUserInput("\nEmail sent! \n Want to send another message? Type 'y' for yes and any other key to exit.");
                if (sendAnotherYN.isEmpty()) {
                    sendAnotherMail = false;
                }
                else {
                    sendAnotherMail = (sendAnotherYN.charAt(0) == 'y') ? true : false;
                }
            }
            if (PublicClient.getAuthenticationManager() != null) {
                PublicClient.getAuthenticationManager().disconnect();
            }

        } catch (SendMailException ex) {
            mLogger.writeLog(Level.SEVERE, ex.getMessage(), ex);
            return;
        } catch (Exception e) {
            mLogger.writeLog(Level.SEVERE, "Exception in startSendMail ", e);
        }

    }

    public static AuthenticationManager getAuthenticationManager() throws IOException {

        try {
            return AuthenticationManager.getInstance();
        } finally {

        }
    }

    public static String getUserInput(String prompt) throws Exception {
        Scanner in = new Scanner(System.in, "UTF-8");

        System.out.println(prompt);
        final String code = in.nextLine();
        return code;

    }


}
