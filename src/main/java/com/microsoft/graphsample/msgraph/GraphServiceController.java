/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license.
 * See LICENSE in the project root for license information.
 */
package com.microsoft.graphsample.msgraph;


import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.models.extensions.*;
import com.microsoft.graph.models.generated.BodyType;
import com.microsoft.graphsample.connect.Constants;
import com.microsoft.graphsample.connect.DebugLogger;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.*;
import java.io.File;
import java.util.Collections;
import java.util.logging.Level;

/**
 * Handles the creation and update of a mail message. Uses the GraphServiceClient to
 * send the message. The user must authenticate with the Microsoft Graph before sending mail
 * {@link #createDraftMail(String, String, String)}method.
 */
class GraphServiceController {

    private final IGraphServiceClient mGraphServiceClient;

    public GraphServiceController() {
        mGraphServiceClient = GraphServiceClientManager.getInstance().getGraphServiceClient();
    }

    /**
     * Creates a draft email message using the Microsoft Graph API on Office 365. The mail is sent
     * from the address of the signed in user.
     *
     * @param emailAddress The recipient email address.
     * @param subject      The subject to use in the mail message.
     * @param body         The body of the message.
     */
    public Message createDraftMail(
            final String emailAddress,
            final String subject,
            final String body
    ) throws SendMailException {
        Message message = null;
        try {
            // create the email message
            message = createMessage(subject, body, emailAddress);
            message = mGraphServiceClient
                    .me()
                    .messages()
                    .buildRequest()
                    .post(message);
        } catch (Exception ex) {
            throw new SendMailException("exception on send mail", ex);
        }

        return message;
    }


    /**
     * Creates a new email message with attachment and sends it to a specified recipient
     *
     * @param emailAddress Recipient email address
     * @param subject      Subject of the email message
     * @param body         Email body
     * @param attachment   Attachment object. Sent as not inline
     * @param callback     Callback invoked when send mail operation is complete
     * @throws SendMailException Exception thrown by Graph SDK with custom exception description
     */
    public void sendNewMessageAsync(
            final String emailAddress,
            final String subject,
            final String body,
            final Attachment attachment,
            ICallback<Void> callback
    ) throws SendMailException {
        try {
            Message message = createMessage(subject, body, emailAddress);
            message.attachments.getCurrentPage().add(attachment);
            mGraphServiceClient
                    .me()
                    .sendMail(message, true)
                    .buildRequest()
                    .post(callback);

        } catch (Exception ex) {
            try {
                DebugLogger.getInstance().writeLog(Level.SEVERE, "exception on send new message", ex);

            } catch (Exception e) {
                throw new SendMailException("SendNewMessageAsync failed ", e);
            }

        }
    }

    /**
     * Posts a file attachment in a draft message by message Id
     *
     * @param messageId   String. The id of the draft message to add an attachment to
     * @param picture     Byte[]. The picture in bytes
     * @param sharingLink String. The sharing link to the uploaded picture
     */
    public Attachment addPictureToDraftMessage(
            String messageId,
            byte[] picture,
            String sharingLink) throws SendMailException {
        Attachment attachment = null;
        try {
            byte[] attachementBytes = new byte[picture.length];
            if (picture.length > 0) {
                attachementBytes = picture;
            }
            else {
                attachementBytes = getDefaultPicture();
            }

            FileAttachment fileAttachment = new FileAttachment();
            fileAttachment.oDataType = "#microsoft.graph.fileAttachment";
            fileAttachment.contentBytes = attachementBytes;
            fileAttachment.name = "me.png";
            fileAttachment.size = attachementBytes.length;
            fileAttachment.isInline = false;
            fileAttachment.id = "blabla";

            DebugLogger.getInstance().writeLog(Level.INFO, "attachement id " + fileAttachment.id);
            attachment = mGraphServiceClient
                    .me()
                    .messages(messageId)
                    .attachments()
                    .buildRequest()
                    .post(fileAttachment);
        } catch (Exception ex) {
            throw new SendMailException("Exception on add picture to draft message", ex);
        }

        return attachment;
    }

    /**
     * Sends a draft message to the specified recipients
     *
     * @param messageId String. The id of the message to send
     * @param callback  Method invoked when operation is complete
     */
    public void addAttachmentToDraftAsync(
            String messageId,
            Attachment attachment,
            ICallback<Attachment> callback) throws SendMailException {
        try {

            mGraphServiceClient
                    .me()
                    .messages(messageId).attachments(messageId).buildRequest()
                    .post(attachment, callback);

        } catch (Exception ex) {
            throw new SendMailException("exception on send draft message ", ex);
        }
    }

    /**
     * Sends a draft message to the specified recipients
     *
     * @param messageId String. The id of the message to send
     */
    public void sendDraftMessage(
            String messageId,
            int content_length) throws SendMailException {
        try {

            mGraphServiceClient
                    .me()
                    .mailFolders("Drafts")
                    .messages(messageId)
                    .send()
                    .buildRequest()
                    .post();

        } catch (Exception ex) {
            throw new SendMailException("Exception on send draft mail", ex);
        }
    }

    /**
     * Gets a draft message by message id
     *
     * @param messageId
     */
    public Message getDraftMessage(String messageId) throws SendMailException {
        Message draftMessage = null;
        try {
            draftMessage = mGraphServiceClient.me()
                                              .messages(messageId)
                                              .buildRequest()
                                              .get();
        } catch (Exception ex) {
            throw new SendMailException("exception on get draft message ", ex);
        }

        return draftMessage;
    }


    /**
     * Gets the local user who is authenticated with the Microsoft Graph API endpoint
     *
     * @return
     * @throws SendMailException
     */
    public User getUser() throws SendMailException {
        User user = null;
        try {
            user = mGraphServiceClient
                    .me()
                    .buildRequest()
                    .get();
        } catch (Exception ex) {
            throw new SendMailException("Exception on get me", ex);
        }

        return user;
    }

    /**
     * Gets the signed in user's profile picture from the Microsoft Graph
     *
     * @return Byte array of the user's profile picture
     * @throws SendMailException
     */
    public byte[] getUserProfilePicture() throws SendMailException {
        InputStream photoStream  = null;
        byte[]      pictureBytes = new byte[1024];
        try {
            photoStream = mGraphServiceClient
                    .me()
                    .photo()
                    .content()
                    .buildRequest()
                    .get();
            if (photoStream == null) {
                DebugLogger.getInstance().writeLog(Level.INFO, "no picture found ");
                pictureBytes = getDefaultPicture();
            }
            else {
                pictureBytes = inputStreamToByteArray(photoStream);
                if (pictureBytes.length <= 0) {
                    pictureBytes = getDefaultPicture();
                }
            }
        } catch (Exception ex) {
            throw new SendMailException("Exception on get user profile photo", ex);
        }

        return pictureBytes;
    }


    /**
     * Converts an inputStream to a byte array. The input stream should be a stream of profile
     * picture bytes that comes in the response to a GET request on the  Microsoft Graph API
     *
     * @param inputStream
     * @return
     */
    private byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        byte[] pictureBytes = null;
        try {
            BufferedInputStream bufferedInputStream = (BufferedInputStream) inputStream;
            byte[]              buff                = new byte[8000];


            ByteArrayOutputStream bao       = new ByteArrayOutputStream();
            int                   bytesRead = 0;

            //This seems to be executing on the main thread!!!
            while ((bytesRead = bufferedInputStream.read(buff)) != -1) {
                bao.write(buff, 0, bytesRead);
            }
            pictureBytes = bao.toByteArray();

            bao.close();
        } catch (IOException ex) {
            DebugLogger.getInstance().writeLog(Level.SEVERE,
                                               "Attempting to read buffered network resource",
                                               ex);
        }

        return pictureBytes;
    }

    /**
     * Uploads a user picture as byte array to the user's OneDrive root folder
     *
     * @param picture byte[] picture byte array
     */
    public DriveItem uploadPictureToOneDrive(byte[] picture) throws SendMailException {

        DriveItem driveItem = null;
        try {
            driveItem = mGraphServiceClient
                    .me()
                    .drive()
                    .root()
                    .itemWithPath("me2.png")
                    .content()
                    .buildRequest()
                    .put(picture);
        } catch (Exception ex) {
            throw new SendMailException("exception on upload picture to OneDrive ", ex);
        }

        return driveItem;
    }

    /**
     * Requests OneDrive to create a public sharing link to a picture stored in OneDrive.
     *
     * @param id
     * @return Permission. The Permission object that exposes the requested sharing link
     * @throws SendMailException
     */
    public Permission getPermissionSharingLink(String id) throws SendMailException {

        Permission permission = null;
        try {
            permission = mGraphServiceClient
                    .me()
                    .drive()
                    .items(id)
                    .createLink("view", "organization")
                    .buildRequest()
                    .post();
        } catch (Exception ex) {
            throw new SendMailException("exception on get OneDrive sharing link ", ex);
        }

        return permission;
    }

    /**
     * Gets a picture from the device external storage root folder
     *
     * @return byte[] the default picture in a byte array
     */
    private byte[] getDefaultPicture() throws SendMailException{


        int    bytesRead;
        byte[] bytes = new byte[1024];

        File            file = new File("/", Constants.DEFAULT_IMAGE_FILENAME);
        FileInputStream buf  = null;
        if (file.exists() && file.canRead()) {
            int size = (int) file.length();
            bytes = new byte[size];
            try {
                buf = new FileInputStream(file);
                bytesRead = buf.read(bytes, 0, size);
                DebugLogger.getInstance().writeLog(Level.INFO, "Bytes read " + String.valueOf(bytesRead));
            } catch (FileNotFoundException e) {
                throw new SendMailException("Could not find default picture file", e);

            } catch (IOException e) {
                throw new SendMailException("Could not open default picture file", e);
            }
        }
        return bytes;
    }


    @VisibleForTesting

    /**
     * Creates a new Message object
     */
    private Message createMessage(
            String subject,
            String body,
            String address) {

        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("The address parameter can't be null or empty.");
        }
        else {
            // perform a simple validation of the email address
            String addressParts[] = address.split("@");
            if (addressParts.length != 2 || addressParts[0].length() == 0 || addressParts[1].indexOf('.') == -1) {
                throw new IllegalArgumentException(
                        String.format("The address parameter must be a valid email address {0}", address)
                );
            }
        }
        Message      message      = new Message();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.address = address;
        Recipient recipient = new Recipient();
        recipient.emailAddress = emailAddress;
        message.toRecipients = Collections.singletonList(recipient);
        ItemBody itemBody = new ItemBody();
        itemBody.content = body;
        itemBody.contentType = BodyType.HTML;
        message.body = itemBody;
        message.subject = subject;
        return message;
    }
}