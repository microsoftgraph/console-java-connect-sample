/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license.
 * See LICENSE in the project root for license information.
 */
package com.microsoft.graphsample.msgraph;

import com.microsoft.graph.models.extensions.DriveItem;
import com.microsoft.graph.models.extensions.Message;
import com.microsoft.graph.models.extensions.Permission;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graphsample.connect.Constants;
import com.microsoft.graphsample.connect.DebugLogger;

import java.util.logging.Level;

/**
 * This class handles the send mail operation of the app.
 * The app must be connected to Office 365 before this activity can send an email.
 * It also uses the GraphServiceController to send the message.
 */
public class GraphSendMail {
    final private GraphServiceController mGraphServiceController;
    DebugLogger mLogger;
    private String mRecipientEmailAddress;

    public GraphSendMail() throws SendMailException {
        try {
            mGraphServiceController = new GraphServiceController();
            mLogger = DebugLogger.getInstance();

        } catch (Exception e) {
            throw new SendMailException("Exception in GraphSendMail constructor", e);
        }
    }

    /**
     * Handler for the onclick event of the send mail button. It uses the GraphServiceController to
     * send an email. When the call is completed, the call will return to either the success()
     * or failure() methods in this class which will then take the next steps on the UI.
     * This method sends the email using the address stored in the mEmailEditText view.
     * The subject and body of the message is stored in the strings.xml file.
     * <p>
     * The following calls are made asynchronously in a chain of callback invocations.
     * 1. Get the user's profile picture from Microsoft Graph
     * 2. Upload the profile picture to the user's OneDrive root folder
     * 3. Get a sharing link to the picture from OneDrive
     * 4. Create and post a draft email
     * 5. Get the draft message
     * 6. Attach the profile picture to the draft mail as a byte array
     * 7. Send the draft email
     */

    public void sendMail(String sendAddress) {
        mRecipientEmailAddress = sendAddress;
        byte[] photoBytes = null;
        try {
            //1. Get the signed in user's profile picture
            photoBytes = mGraphServiceController.getUserProfilePicture();
            if (photoBytes != null) {

                //2. Upload the profile picture to OneDrive
                DriveItem driveItem = mGraphServiceController.uploadPictureToOneDrive(photoBytes);
                if (driveItem != null) {
                    //3. Get the sharing link and if success, call step 4 helper
                    mLogger.writeLog(Level.INFO, "Getting the sharing link ");
                    getPermissionSharingLink(driveItem, photoBytes);
                }
            }
        } catch (SendMailException ex) {
            mLogger.writeLog(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            mLogger.writeLog(Level.INFO, "Started send mail operation ");
        }
    }


    public User getMeUser() throws SendMailException {
        return mGraphServiceController.getUser();
    }

    /**
     * Gets the picture sharing link from OneDrive and calls the step 4 helper
     *
     * @param driveItem
     * @param bytes
     */
    private void getPermissionSharingLink(DriveItem driveItem, final byte[] bytes) {
        //3. Get a sharing link to the picture uploaded to OneDrive
        try {
            createDraftMail(mGraphServiceController.getPermissionSharingLink(driveItem.id), bytes);
        } catch (SendMailException ex) {
            mLogger.writeLog(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Creates a draft mail and calls the step 5 helper
     *
     * @param permission
     * @param bytes
     */
    private void createDraftMail(final Permission permission, final byte[] bytes) {
        //Prepare body message and insert name of sender
        String body = Constants.MESSAGE_BODY;
        try {
            //replace() is used instead of format() because the mail body string contains several
            //'%' characters, most of which are not string place holders. When format() is used,
            //format exception is thrown. Place holders do not match replacement parameters.
            body = body.replace("a href=%s", "a href=" + permission.link.webUrl.toString());
            final String mailBody = body;
            //4. Create a draft mail message
            Message message = mGraphServiceController.createDraftMail(
                    mRecipientEmailAddress,
                    Constants.SUBJECT_TEXT,
                    mailBody);
            if (message != null) {
                mLogger.writeLog(Level.INFO, "Create draft mail  ");
                Message draftMessage = mGraphServiceController.getDraftMessage(message.id);
                addPictureToDraftMessage(draftMessage, permission, bytes);
            }
            else {
                mLogger.writeLog(Level.INFO, "Create draft mail failed ");
            }

        } catch (SendMailException ex) {
            mLogger.writeLog(Level.SEVERE, ex.getMessage(), ex);
        }
    }


    /**
     * Adds the picture bytes as attachment to the draft message and calls the step 7 helper
     *
     * @param aMessage
     * @param permission
     * @param bytes
     */
    private void addPictureToDraftMessage(final Message aMessage, final Permission permission, final byte[] bytes) {
        //6. Add the profile picture to the draft mail
        try {
            mGraphServiceController.addPictureToDraftMessage(aMessage.id, bytes, permission.link.webUrl);
            mLogger.writeLog(Level.INFO, "Sending draft message ");
            sendDraftMessage(aMessage);
        } catch (SendMailException ex) {
            mLogger.writeLog(Level.SEVERE, ex.getMessage(), ex);
        }
    }


    /**
     * Sends the draft message
     *
     * @param aMessage
     */
    private void sendDraftMessage(final Message aMessage) {
        //7. Send the draft message to the recipient
        try {
            mGraphServiceController.sendDraftMessage(aMessage.id, 0);
            mLogger.writeLog(Level.INFO, "Draft message sent ");

        } catch (SendMailException ex) {
            mLogger.writeLog(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
