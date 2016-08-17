/**
 * Personium
 * Copyright 2016 FUJITSU LIMITED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fujitsu.dc.gui.portal.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

import com.fujitsu.dc.gui.PCSEnvironment;
import com.fujitsu.dc.gui.portal.PCSConstants;
import com.fujitsu.dc.gui.portal.PCSCustomException;
import com.fujitsu.dc.gui.portal.PropertiesUtil;
import com.fujitsu.dc.gui.portal.SHAHashing;
import com.fujitsu.dc.gui.portal.model.User;
import com.fujitsu.dc.gui.portal.service.AdministratorManagementService;
import com.fujitsu.dc.gui.portal.service.LoginService;
import com.fujitsu.dc.gui.portal.service.UserEmailService;
/**
 *  Mail Notification class for mail process.
 */
public class MailNotifications {

    private static String emailSmtpServer = "";
    private static String smtpPort = "";
    private static String emailSender  = "";
    private static String languageType  = "";

    static {
        emailSmtpServer = PCSEnvironment.getInstance().getResourceBundle()
                .getString("EMAIL_SMTP_SERVER");
        smtpPort = PCSEnvironment.getInstance().getResourceBundle()
                .getString("SMTP_PORT");
        emailSender = PCSEnvironment.getInstance().getResourceBundle()
                .getString("EMAILSENDER");
        languageType = PCSEnvironment.getInstance().getResourceBundle().getString("LANGUAGE_TYPE");
    }
    private UserEmailService userEmailService = null;
    /**
     * Constructor for initializing objects.
     */
    public MailNotifications() {
        userEmailService = new UserEmailService();
    }
    /**
     * This method sends mail to user for email verification.
     * @param username user name
     * @param emailTo emailTo
     * @param request request
     * @param subscriberName subscriberName
     * @param firstName firstName
     * @param familyName familyName
     * @param actionPerformed actionPerformed
     * @return mailStatus
     */
    protected boolean sendMailForEmailVerification(String username,
            String emailTo, HttpServletRequest request, String subscriberName,
            String firstName, String familyName, String actionPerformed) {
        String[] logDetails = null;
        logDetails = new String[] {"UserName: " + username + " and Subscriber: " + subscriberName};
        PropertiesUtil.appendLog("MF-IN-001", logDetails, false);
        boolean mailStatus = false;
        int constMiliSeconds = PCSConstants.CONSTMILLISECONDS;
        try {
            File file = getMailTemplateFilePath("VERIFICATION_MAIL");
            if (file != null) {
                String text = getMailTemplateFileContents(file);
                String verificationMailSubject = PCSEnvironment.getInstance()
                        .getResourceBundle().getString("VERIFICTION_MAIL_SUBJECT");
                String servletRequestURL = request.getRequestURL().toString();
                servletRequestURL = servletRequestURL.replace("http:", "https:");
                String operation = "EMAIL_AUTHENTICATION_BY_USER";
                String encodededUsername = getEncodedString(username);
                String encodededSubscribername = getEncodedString(subscriberName);
                String encodededOperation = getEncodedString(operation);
                String encodedActionPerformed = getEncodedString(actionPerformed);
                String strVerificationMailExpirationTime = PCSEnvironment
                        .getInstance().getResourceBundle()
                        .getString("VERIFICATION_MAIL_DEFUALT_EXPIRATION_TIME");
                int expirationTimeInHours = Integer
                        .parseInt(strVerificationMailExpirationTime);
                long expirationTimeInSeconds = expirationTimeInHours
                        * constMiliSeconds;
                long verificationMailExpiredAtTime = System.currentTimeMillis()
                        + expirationTimeInSeconds;
                String encodedQueryString = "afgi=" + encodededUsername + "&adgo="
                        + encodededOperation + "&akls=" + encodededSubscribername
                        + "&ahgt=" + verificationMailExpiredAtTime
                        + "&abap=" + encodedActionPerformed;
                String emailValidationUrl = servletRequestURL + "?"
                        + encodedQueryString;
                MailTemplateParser mailTemplateParser = new MailTemplateParser();
                Map<String, String> mapText = new HashMap<String, String>();
                mapText.put("FirstName", firstName);
                mapText.put("FamilyName", familyName);
                mapText.put("EmailVerificationLink", emailValidationUrl);
                String mailBody = mailTemplateParser
                        .updateMailTemplateWithDynamicData(mapText, text);
                mailStatus = sendPlainTextMail(emailTo, verificationMailSubject, mailBody);
                if (mailStatus) {
                    Date currentDateTime = new Date(System.currentTimeMillis());
                    userEmailService.updateMailVerificationSendAt(username,
                            currentDateTime);
                }
                logDetails = new String[] {"Email verification mail status "
                        + mailStatus};
                PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
            }
        } catch (IOException exception) {
            logDetails = new String[] {
                    "Exception in reading VERIFICATION_MAIL file.",
                    exception.getMessage(), exception.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        }
        return mailStatus;
    }
    /**
     * This method sends mail to subscriber after email verification by
     * the user.
     * @param subscriberName subscriberName
     * @param userName userName
     * @return mailStatus
     */
    protected boolean sendMailToSubscriberAfterMailVerification(
            String subscriberName, String userName) {
        String[] logDetails = null;
        logDetails = new String[] {"Subscriber: " + subscriberName + " and UserName: " + userName};
        PropertiesUtil.appendLog("MF-IN-001", logDetails, false);
        boolean mailStatus = false;
        Properties props = new Properties();
        props.put("mail.smtp.host", emailSmtpServer);
        props.put("mail.smtp.port", smtpPort);
        Session session = Session.getDefaultInstance(props);
        String toEmail = null;
        try {
            File file = getMailTemplateFilePath("ACCOUNT_ACTIVATE_MAIL");
            if (file != null) {
                String text = getMailTemplateFileContents(file);
                List<User> subscriberInfo = userEmailService
                        .getUserInformation(subscriberName);
                List<User> userInfo = userEmailService
                        .getUserInformation(userName);
                if (subscriberInfo.size() > 0 && userInfo.size() > 0) {
                    String firstName = subscriberInfo.get(0).getFirstName();
                    String familyName = subscriberInfo.get(0).getFamilyName();
                    toEmail = subscriberInfo.get(0).getEmail();
                    String userFirstName = userInfo.get(0).getFirstName();
                    String userFamilyName = userInfo.get(0).getFamilyName();
                    String userEmail = userInfo.get(0).getEmail();
                    String userPrivilege = userInfo.get(0).getPrivilege();
                    Date userCreationDate = userInfo.get(0).getCreatedAt();
                    String strUserCreationDate = userCreationDate.toString();
                    strUserCreationDate = strUserCreationDate.substring(0, strUserCreationDate.length() - 2);
                    Date userVerifiedDate = userInfo.get(0).getVerifiedAt();
                    String userStatus = userInfo.get(0).getStatus();
                    LoginService loginService = new LoginService();
                    String orgID = loginService.getOrganizationID(userName);
                    String userOrgName = userEmailService.getUserOrganizationName(orgID);
                    MailTemplateParser mailTemplateParser = new MailTemplateParser();
                    Map<String, String> mapText = new HashMap<String, String>();
                    mapText.put("FirstName", firstName);
                    mapText.put("FamilyName", familyName);
                    mapText.put("Username", userName);
                    mapText.put("UserFirstName", userFirstName);
                    mapText.put("UserFamilyName", userFamilyName);
                    mapText.put("Email", userEmail);
                    mapText.put("Privilege", userPrivilege);
                    mapText.put("OrganizationName", userOrgName);
                    mapText.put("UserCreatedDate", strUserCreationDate);
                    mapText.put("EmailVerifiedDate", "");
                    if (userVerifiedDate != null) {
                        String strUserVerifiedDate = userVerifiedDate.toString();
                        strUserVerifiedDate = strUserVerifiedDate.substring(0, strUserVerifiedDate.length() - 2);
                        mapText.put("EmailVerifiedDate", strUserVerifiedDate);
                    }
                    mapText.put("CurrentAccountStatus", userStatus);
                    String mailBody = mailTemplateParser
                            .updateMailTemplateWithDynamicData(mapText, text);
                    String subscriberMailSubject = PCSEnvironment.getInstance()
                            .getResourceBundle().getString("SUBSCRIBER_MAIL_SUBJECT");
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(emailSender));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(toEmail));
                    message.setSubject(subscriberMailSubject);
                    //message.setContent(mailBody, "text/palin; charset=utf-8");
                    logDetails = new String[] {subscriberMailSubject, mailBody, toEmail};
                    PropertiesUtil.appendLog("MF-IN-011", logDetails, false);
                    message.setText(mailBody);
                    Transport.send(message);
                    mailStatus = true;
                    logDetails = new String[] {"User activation mail status "
                            + mailStatus};
                    PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
                } else {
                    logDetails = new String[] {subscriberName, userName};
                    PropertiesUtil.appendLog("MF-IN-012", logDetails, false);
                }
            }
        } catch (AddressException addrException) {
            logDetails = new String[] {
                    "Exception while sending mail to: " + toEmail,
                    addrException.getMessage(), addrException.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        } catch (SendFailedException sendFailException) {
            logDetails = new String[] {
                    "Exception while sending mail to: " + toEmail,
                    sendFailException.getMessage(),
                    sendFailException.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        } catch (MessagingException messagingException) {
            logDetails = new String[] {
                    "Exception while sending mail to: " + toEmail,
                    messagingException.getMessage(),
                    messagingException.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        } catch (Exception exception) {
            logDetails = new String[] {
                    "Exception while sending mail to: " + toEmail,
                    exception.getMessage(), exception.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        }
        return mailStatus;
    }
    /**
     * This method sends mail to user containing first time login credentials.
     * @param userName userName
     * @param password password
     * @param contextRoot contextRoot
     * @return mailStatus
     */
    protected boolean sendMailToUserHavingLoginCredentials(String userName,
            String password, String contextRoot) {
        String[] logDetails = null;
        logDetails = new String[] {"Sending login credential mail to: " + userName};
        PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
        boolean mailStatus = false;
        String toEmail = null;
        String mailTemplateType = "LOGIN_CREDENTIAL_MAIL";
        String mailSubject = "LOGIN_CREDENTIAL_MAIL_SUBJECT";
        try {
            List<User> userInfo = userEmailService.getUserInformation(userName);
            if (userInfo.size() > 0) {
                String firstName = userInfo.get(0).getFirstName();
                String familyName = userInfo.get(0).getFamilyName();
                toEmail = userInfo.get(0).getEmail();
                Date lastLoginDate = userInfo.get(0).getLastLoginAt();
                if (lastLoginDate != null) {
                    mailTemplateType = "PASSWORD_CHANGE_MAIL_BY_SUBSCRIBER";
                    mailSubject = "CHANGE_PASSWORD_MAIL_SUBJECT";
                }
                File file = getMailTemplateFilePath(mailTemplateType);
                if (file != null) {
                    String text = getMailTemplateFileContents(file);
                    MailTemplateParser mailTemplateParser = new MailTemplateParser();
                    Map<String, String> mapText = new HashMap<String, String>();
                    mapText.put("FirstName", firstName);
                    mapText.put("FamilyName", familyName);
                    mapText.put("Username", userName);
                    mapText.put("Password", password);
                    mapText.put("PortalLoginUrl", contextRoot);
                    String mailBody = mailTemplateParser
                            .updateMailTemplateWithDynamicData(mapText, text);
                    String loginCredentialMailSubject = PCSEnvironment.getInstance()
                            .getResourceBundle()
                            .getString(mailSubject);
                    mailStatus = sendPlainTextMail(toEmail, loginCredentialMailSubject, mailBody);
                    logDetails = new String[] {"Mail status "
                            + mailStatus};
                    PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
                }
            } else {
                logDetails = new String[] {"Mail - User not found "
                        + userName};
                PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
            }
        } catch (IOException exception) {
            logDetails = new String[] {
                    "Exception in reading template file.",
                    exception.getMessage(), exception.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        }
        return mailStatus;
    }
    /**
     * This method sends mail to user containing password on forget password operation.
     * @param userName userName
     * @param password password
     * @return mailStatus
     */
    protected boolean sendMailToUserHavingPassword(String userName,
            String password) {
        String[] logDetails = null;
        logDetails = new String[] {"Sending forget password mail to: " + userName};
        PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
        boolean mailStatus = false;
        String toEmail = null;
        try {
            List<User> userInfo = userEmailService.getUserInformation(userName);
            if (userInfo.size() > 0) {
                String firstName = userInfo.get(0).getFirstName();
                String familyName = userInfo.get(0).getFamilyName();
                toEmail = userInfo.get(0).getEmail();
                File file = getMailTemplateFilePath("FORGET_PASSWORD_MAIL");
                if (file != null) {
                    String text = getMailTemplateFileContents(file);
                    MailTemplateParser mailTemplateParser = new MailTemplateParser();
                    Map<String, String> mapText = new HashMap<String, String>();
                    mapText.put("FirstName", firstName);
                    mapText.put("FamilyName", familyName);
                    mapText.put("Password", password);
                    String mailBody = mailTemplateParser
                            .updateMailTemplateWithDynamicData(mapText, text);
                    String forgetPasswordMailSubject = PCSEnvironment.getInstance()
                            .getResourceBundle()
                            .getString("FORGET_PASSWORD_MAIL_SUBJECT");
                    mailStatus = sendPlainTextMail(toEmail, forgetPasswordMailSubject, mailBody);
                    logDetails = new String[] {"Forget password mail status "
                            + mailStatus};
                    PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
                }
            } else {
                logDetails = new String[] {"Forget password mail - User not found "
                        + userName};
                PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
            }
        } catch (IOException exception) {
            logDetails = new String[] {
                    "Exception in reading FORGET_PASSWORD_MAIL file.",
                    exception.getMessage(), exception.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        }
        return mailStatus;
    }
    /**
     * This method returns the encoded string.
     * @param stringToBeEncoded stringToBeEncoded
     * @return encodedString
     */
    protected String getEncodedString(String stringToBeEncoded) {
        String encodedString = null;
        byte[] encoded = Base64.encodeBase64(stringToBeEncoded.getBytes());
        encodedString = new String(encoded);
        return encodedString;
    }
    /**
     * This method returns the decoded string.
     * @param stringToBeDecoded stringToBeDecoded
     * @return decodedString
     */
    protected String getDecodedString(String stringToBeDecoded) {
        String decodedString = null;
        byte[] decoded = Base64.decodeBase64(stringToBeDecoded.getBytes());
        decodedString = new String(decoded);
        return decodedString;
    }
    /**
     * This method returns whether link is valid or not.
     * @param verificationSendAtDate verificationSendAtDate
     * @param expiredAtDate expiredAtDate
     * @return isLinkExpire
     */
    protected boolean isMailExpire(Date verificationSendAtDate, Date expiredAtDate) {
        boolean isLinkExpire = false;
        long expiredAtInMilliseconds = expiredAtDate.getTime();
        long currentTimeInMilliSeconds = System.currentTimeMillis();
        if (expiredAtInMilliseconds < currentTimeInMilliSeconds) {
            isLinkExpire = true;
        }
        return isLinkExpire;
    }

    /**
     * This method returns mail template file path.
     * @param mailType mailType
     * @return file
     */
    protected File getMailTemplateFilePath(String mailType) {
        File file = null;
        String fileName = null;
        String defaultMailTemplatePath = "mail_templates/EN";
        String[] logDetails = null;
        if (languageType.equals("JP")) {
            defaultMailTemplatePath = "mail_templates/JP";
        }
        if (mailType == "VERIFICATION_MAIL") {
            fileName = PCSConstants.VERIFICATION_MAIL_TEMPLATE_FILE_NAME;
        } else if (mailType == "ACCOUNT_ACTIVATE_MAIL") {
            fileName = PCSConstants.ACTIVATE_ACCOUNT_MAIL_TEMPLATE_FILE_NAME;
        } else if (mailType == "LOGIN_CREDENTIAL_MAIL") {
            fileName = PCSConstants.LOGIN_CREDENTIAL_MAIL_TEMPLATE_FILE_NAME;
        } else if (mailType == "ACCOUNT_LOCKED_MAIL") {
            fileName = PCSConstants.ACCOUNT_LOCKED_MAIL_TEMPLATE_FILE_NAME;
        } else if (mailType == "USER_DELETED_MAIL") {
            fileName = PCSConstants.USERS_DELETED_LOCKED_MAIL_TEMPLATE_FILE_NAME;
        } else if (mailType == "FORGET_PASSWORD_MAIL") {
            fileName = PCSConstants.USERS_FORGET_PASSWORD_MAIL_TEMPLATE_FILE_NAME;
        } else if (mailType == "PASSWORD_CHANGE_MAIL") {
            fileName = PCSConstants.CHANGE_PASSWORD_MAIL_TEMPLATE_FILE_NAME;
        } else if (mailType == "PASSWORD_CHANGE_MAIL_BY_SUBSCRIBER") {
            fileName = PCSConstants.CHANGE_PASSWORD_BY_SUBSCRIBER_MAIL_TEMPLATE_FILE_NAME;
        }
        ClassLoader classLoader = getClass().getClassLoader();
        file = new File(classLoader.getResource(
                defaultMailTemplatePath + File.separator + fileName)
                .getFile());
        logDetails = new String[] {file.toString()};
        PropertiesUtil.appendLog("MF-IN-003", logDetails, false);
        return file;
    }

    /**
     * This method returns mail template file contents.
     * @param file file
     * @return fileContents
     * @throws IOException exception
     */
    protected String getMailTemplateFileContents(File file) throws IOException {
        String fileContents = new String(Files.readAllBytes(Paths.get(file
                .getPath())), PCSConstants.UTF8);
        return fileContents;
    }
    /**
     * This method sends mail to user to notify account
     * locked status.
     * @param username user name
     * @param emailTo emailTo
     * @param firstName firstName
     * @param familyName familyName
     * @return mailStatus
     */
    protected boolean sendMailForAccountLocked(String username, String emailTo,
            String firstName, String familyName) {
        String[] logDetails = null;
        logDetails = new String[] {"UserName: " + username};
        PropertiesUtil.appendLog("MF-IN-001", logDetails, false);
        boolean mailStatus = false;
        try {
            File file = getMailTemplateFilePath("ACCOUNT_LOCKED_MAIL");
            if (file != null) {
                String text = getMailTemplateFileContents(file);
                String accountLockedMailSubject = PCSEnvironment.getInstance()
                        .getResourceBundle().getString("ACCOUNT_LOCKED_MAIL_SUBJECT");
                MailTemplateParser mailTemplateParser = new MailTemplateParser();
                Map<String, String> mapText = new HashMap<String, String>();
                mapText.put("FirstName", firstName);
                mapText.put("FamilyName", familyName);
                String mailBody = mailTemplateParser
                        .updateMailTemplateWithDynamicData(mapText, text);
                mailStatus = sendPlainTextMail(emailTo, accountLockedMailSubject, mailBody);
                logDetails = new String[] {"Email account locked mail status "
                        + mailStatus};
                PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
            }
        } catch (IOException exception) {
            logDetails = new String[] {
                    "Exception in reading VERIFICATION_MAIL file.",
                    exception.getMessage(), exception.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        }
        return mailStatus;
    }
    /**
     * This method sends mail to subscriber containing list of
     * deleted users.
     * @param username user name
     * @param deletedUserList deletedUserList
     * @return mailStatus
     */
    protected boolean sendMailToSubscriberForUsersDeleted(String username, String[] deletedUserList) {
        String[] logDetails = null;
        logDetails = new String[] {"UserName: " + username};
        PropertiesUtil.appendLog("MF-IN-001", logDetails, false);
        boolean mailStatus = false;
        String emailTo = null;
        try {
            File file = getMailTemplateFilePath("USER_DELETED_MAIL");
            if (file != null) {
                String text = getMailTemplateFileContents(file);
                String userDeletedMailSubject = PCSEnvironment.getInstance()
                        .getResourceBundle().getString("USERS_DELETED_MAIL_SUBJECT");
                List<User> userInfo = userEmailService.getUserInformation(username);
                if (userInfo.size() > 0) {
                    String firstName = userInfo.get(0).getFirstName();
                    String familyName = userInfo.get(0).getFamilyName();
                    emailTo = userInfo.get(0).getEmail();
                    MailTemplateParser mailTemplateParser = new MailTemplateParser();
                    Map<String, String> mapText = new HashMap<String, String>();
                    mapText.put("FirstName", firstName);
                    mapText.put("FamilyName", familyName);
                    mapText.put("DeletedUserList", Arrays.toString(deletedUserList));
                    String mailBody = mailTemplateParser
                            .updateMailTemplateWithDynamicData(mapText, text);
                    mailStatus = sendPlainTextMail(emailTo, userDeletedMailSubject, mailBody);
                    logDetails = new String[] {"Email for users deleted mail status "
                            + mailStatus};
                    PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
                } else {
                    logDetails = new String[] {"User deleted mail - User not found "
                            + username};
                    PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
                }
            }
        } catch (IOException exception) {
            logDetails = new String[] {
                    "Exception in reading USER_DELETED_MAIL file.",
                    exception.getMessage(), exception.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        }
        return mailStatus;
    }
    /**
     * This method sends mail.
     * @param toEmail toEmail
     * @param loginCredentialMailSubject loginCredentialMailSubject
     * @param mailBody mailBody
     * @return mailStatus
     */
    protected boolean sendPlainTextMail(String toEmail,
            String loginCredentialMailSubject, String mailBody) {
        boolean mailStatus = false;
        String[] logDetails = null;
        Properties props = new Properties();
        props.put("mail.smtp.host", emailSmtpServer);
        props.put("mail.smtp.port", smtpPort);
        Session session = Session.getDefaultInstance(props);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailSender));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject(loginCredentialMailSubject);
            message.setContent(mailBody, "text/plain; charset=utf-8");
            logDetails = new String[] {loginCredentialMailSubject, mailBody, toEmail};
            PropertiesUtil.appendLog("MF-IN-011", logDetails, false);
            Transport.send(message);
            mailStatus = true;
        } catch (AddressException addrException) {
            logDetails = new String[] {
                    "Exception while sending mail to: " + toEmail,
                    addrException.getMessage(), addrException.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        } catch (SendFailedException sendFailException) {
            logDetails = new String[] {
                    "Exception while sending mail to: " + toEmail,
                    sendFailException.getMessage(),
                    sendFailException.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        } catch (MessagingException messagingException) {
            logDetails = new String[] {
                    "Exception while sending mail to: " + toEmail,
                    messagingException.getMessage(),
                    messagingException.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        } catch (Exception exception) {
            logDetails = new String[] {
                    "Exception while sending mail to: " + toEmail,
                    exception.getMessage(), exception.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        }
        return mailStatus;
    }

    /**
     * This function performs forgot password operation.
     * @param request request
     * @param operation operation
     * @return result
     */
    protected String forgotPassword(HttpServletRequest request, String operation) {
        String result = null;
        String userName = request.getParameter("userName");
        String newPassword = request.getParameter("newPassword");
        String encryptedPass = null;
        SHAHashing shaHashing = new SHAHashing();
        String salt = null;
        String[] logDetails = null;
        try {
             salt = UUID.randomUUID().toString();
             encryptedPass = shaHashing.createHashedPassword(newPassword, salt).trim();
        } catch (PCSCustomException pcsExe) {
             pcsExe.printStackTrace();
        } catch (Exception exe) {
             exe.printStackTrace();
        }
        if (encryptedPass != null) {
            //The following regular expression finds string of white spaces.
            encryptedPass = encryptedPass.replaceAll("^ +| +$|( )+", " ");
            //The following regular expression finds a non-word character.
            encryptedPass = encryptedPass.replaceAll("\\W", "");
        }
        //String salt = SHAHashing.salt;
        java.sql.Date updatedAt = new java.sql.Date(System.currentTimeMillis());
        java.sql.Date passwordResetAt = new java.sql.Date(System.currentTimeMillis());
        AdministratorManagementService administratorManagementService =
                new AdministratorManagementService();
        logDetails = new String[] {userName};
        PropertiesUtil.appendLog("CP-DB-001", logDetails, false);
        boolean updatePassword = administratorManagementService
                .updatePassword(userName, encryptedPass, salt,
                        updatedAt, passwordResetAt);
        logDetails = new String[] {Boolean.toString(updatePassword)};
        PropertiesUtil.appendLog("CP-IN-004", logDetails, false);
        if (updatePassword) {
            MailNotifications mailNotifications = new MailNotifications();
            boolean mailStatus = mailNotifications
                    .sendMailToUserHavingPassword(userName, newPassword);
            logDetails = new String[] {operation, Boolean.toString(mailStatus)};
            PropertiesUtil.appendLog("MF-IN-009", logDetails, false);
            if (mailStatus) {
                result = "forgetPasswordMailSentSuccess";
            } else {
                result = "forgetPasswordMailSentFailure";
            }
        }
        return result;
    }

    /**
     * This method validated terms and condition is agreed
     * or not.
     * @param request request
     * @return isUpdateSuccess
     */
    protected boolean checkTermsAndCondition(HttpServletRequest request) {
        String userName = request.getParameter("userName");
        boolean isUpdateSuccess = false;
        try {
            LoginService loginService = new LoginService();
            Date currentDateTime = new Date(System.currentTimeMillis());
            isUpdateSuccess = loginService.updateUserTermsAndConditionAcceptedAt(userName, currentDateTime);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        return isUpdateSuccess;
    }

    /**
     * This method returns deleted users mail status.
     * @param session session
     * @param request request
     * @return mailStatus
     */
    protected String getDeletedUserMailStatus(HttpSession session, HttpServletRequest request) {
        String userName = (String) session.getAttribute("id");
        String mailStatus = "deletedUserMailSentFailure";
        String[] deletedUserList = request.getParameterValues("deletedUserList[]");
        MailNotifications mailNotifications = new MailNotifications();
        boolean isMailSentSuccess = mailNotifications
                .sendMailToSubscriberForUsersDeleted(userName,
                        deletedUserList);
        if (isMailSentSuccess) {
            mailStatus = "deletedUserMailSentSuccess";
        }
        return mailStatus;
    }

    /**
     * This method returns verification mail status.
     * @param request request
     * @param session session
     * @param username user name
     * @param operation operation
     * @return result
     */
    protected String getVerificattionMailStatu(HttpServletRequest request,
            HttpSession session, String username, String operation) {
        String result = null;
        try {
            String emailTo = URLDecoder.decode(
                    request.getParameter("encodedEAddress"), PCSConstants.UTF8);
            String firstName = URLDecoder.decode(
                    request.getParameter("firstName"), PCSConstants.UTF8);
            String familyName = URLDecoder.decode(
                    request.getParameter("familyName"), PCSConstants.UTF8);
            String subscriberName = (String) session.getAttribute("id");
            String actionPerformed = request.getParameter("actionPerformed");
            MailNotifications mailNotifications = new MailNotifications();
            boolean mailStatus = mailNotifications
                    .sendMailForEmailVerification(username, emailTo, request,
                            subscriberName, firstName, familyName, actionPerformed);
            String[] logDetails = new String[] {operation, Boolean.toString(mailStatus)};
            PropertiesUtil.appendLog("MF-IN-009", logDetails, false);
            if (mailStatus) {
                result = "verificationMailSentSuccess";
            } else {
                result = "verificationMailSentFailure";
            }
        } catch (Exception exe) {
            result = "verificationMailSentFailure";
            exe.printStackTrace();
        }
        return result;
    }

    /**
     * This method returns login credential mail status.
     * @param request request
     * @param username user name
     * @param operation operation
     * @return result
     */
    protected String getLoginCredentialMailStatu(HttpServletRequest request,
            String username, String operation) {
        String result = null;
        String[] logDetails = null;
        String password = request.getParameter("password");
        String servletRequestURL = request.getRequestURL().toString();
        int startIndex = 0;
        int lastIndexURL = servletRequestURL.lastIndexOf("/");
        servletRequestURL = servletRequestURL.substring(startIndex, lastIndexURL);
        servletRequestURL = servletRequestURL.replace("http:", "https:");
        MailNotifications mailNotifications = new MailNotifications();
        boolean mailStatus = mailNotifications
                .sendMailToUserHavingLoginCredentials(username, password, servletRequestURL);
        logDetails = new String[] {operation, Boolean.toString(mailStatus)};
        PropertiesUtil.appendLog("MF-IN-009", logDetails, false);
        if (mailStatus) {
            result = "loginCredentialsMailSentSuccess";
        } else {
            result = "loginCredentialsMailSentFaliure";
        }
        return result;
    }
    /**
     * This method returns password change mail status.
     * @param request request
     * @param username user name
     * @param operation operation
     * @return result
     */
    protected String getPasswordChangedMailStatus(HttpServletRequest request,
            String username, String operation) {
        String result = null;
        String[] logDetails = null;
        String servletRequestURL = request.getRequestURL().toString();
        int startIndex = 0;
        int lastIndexURL = servletRequestURL.lastIndexOf("/");
        servletRequestURL = servletRequestURL.substring(startIndex, lastIndexURL);
        servletRequestURL = servletRequestURL.replace("http:", "https:");
        MailNotifications mailNotifications = new MailNotifications();
        boolean mailStatus = mailNotifications
                .sendMailToUserForPasswordChanged(username, servletRequestURL);
        logDetails = new String[] {operation, Boolean.toString(mailStatus)};
        PropertiesUtil.appendLog("MF-IN-009", logDetails, false);
        if (mailStatus) {
            result = "passwordChangeMailSentSuccess";
        } else {
            result = "passwordChangeMailSentFaliure";
        }
        return result;
    }
    /**
     * This method sends mail to user when he/she changes his/her own
     * password.
     * @param userName userName
     * @param contextRoot contextRoot
     * @return mailStatus
     */
    protected boolean sendMailToUserForPasswordChanged(String userName,
            String contextRoot) {
        String[] logDetails = null;
        logDetails = new String[] {"Sending password change mail to: " + userName};
        PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
        boolean mailStatus = false;
        String toEmail = null;
        try {
            List<User> userInfo = userEmailService.getUserInformation(userName);
            if (userInfo.size() > 0) {
                String firstName = userInfo.get(0).getFirstName();
                String familyName = userInfo.get(0).getFamilyName();
                toEmail = userInfo.get(0).getEmail();
                File file = getMailTemplateFilePath("PASSWORD_CHANGE_MAIL");
                if (file != null) {
                    String text = getMailTemplateFileContents(file);
                    MailTemplateParser mailTemplateParser = new MailTemplateParser();
                    Map<String, String> mapText = new HashMap<String, String>();
                    mapText.put("FirstName", firstName);
                    mapText.put("FamilyName", familyName);
                    mapText.put("Username", userName);
                    mapText.put("PortalLoginUrl", contextRoot);
                    String mailBody = mailTemplateParser
                            .updateMailTemplateWithDynamicData(mapText, text);
                    String changePasswordMailSubject = PCSEnvironment.getInstance()
                            .getResourceBundle()
                            .getString("CHANGE_PASSWORD_MAIL_SUBJECT");
                    mailStatus = sendPlainTextMail(toEmail, changePasswordMailSubject, mailBody);
                    logDetails = new String[] {"Password change mail status "
                            + mailStatus};
                    PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
                }
            } else {
                logDetails = new String[] {"Password change mail - User not found "
                        + userName};
                PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
            }
        } catch (IOException exception) {
            logDetails = new String[] {
                    "Exception in reading PASSWORD_CHANGE_MAIL file.",
                    exception.getMessage(), exception.toString() };
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            mailStatus = false;
        }
        return mailStatus;
    }
}
