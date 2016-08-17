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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.fujitsu.dc.gui.portal.PropertiesUtil;
import com.fujitsu.dc.gui.portal.dao.LoginDao;
import com.fujitsu.dc.gui.portal.model.User;
import com.fujitsu.dc.gui.portal.service.LoginService;
import com.fujitsu.dc.gui.portal.service.UserEmailService;

/**
 * Servlet implementation class MailerServlet.
 */
@WebServlet("/MailerServlet")
public class MailerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * This method send mail to the user depending on the operation.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException exception
     * @throws IOException exception
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws
            ServletException, IOException {
        String username = request.getParameter("username");
        String result = null;
        JSONObject responseObject = new JSONObject();
        PrintWriter out = response.getWriter();
        String operation = request.getParameter("operation");
        String[] logDetails = null;
        logDetails = new String[] {operation};
        PropertiesUtil.appendLog("MF-IN-001", logDetails, false);
        HttpSession session = request.getSession(false);
        String sessionRequestId = (String) session.getAttribute("requestId");
        if (operation.equals("VERFICATION_MAIL")) {
            String formValueSendMailVerification = request.getParameter("CSRFTokenSendMailVerification");
            if (sessionRequestId.equals(formValueSendMailVerification)) {
                MailNotifications mailNotifications = new MailNotifications();
                result = mailNotifications.getVerificattionMailStatu(request, session, username, operation);
                responseObject.put("verificationMail", result);
                out.print(responseObject);
            } else {
                result = "sessionTimeOut";
                responseObject.put("sessionTimeOut", "sessionTimeOut");
                out.print(responseObject);
            }

        }
        if (operation.equals("SET_USER_STATUS")) {
            String formValueSetUSerStatus = request.getParameter("CSRFTokenActivateUser");
            if (sessionRequestId.equals(formValueSetUSerStatus)) {
                boolean isUpdateSuccess = setUserStatus(request, username);
                if (isUpdateSuccess) {
                    result = "userStatusUpdateSuccess";
                    responseObject.put("updateUserStatus", result);
                    out.print(responseObject);
                }
            } else {
                result = "sessionTimeOut";
                responseObject.put("sessionTimeOut", "sessionTimeOut");
                out.print(responseObject);
            }
        }
        if (operation.equals("LOGIN_CREDENTIALS")) {
            String formValueSendLoginCredential = request.getParameter("CSRFTokenSendMailLoginCredential");
            if (sessionRequestId.equals(formValueSendLoginCredential)) {
                MailNotifications mailNotifications = new MailNotifications();
                result = mailNotifications.getLoginCredentialMailStatu(request, username, operation);
                responseObject.put("loginCredentialsMail", result);
                out.print(responseObject);
            } else {
                result = "sessionTimeOut";
                responseObject.put("sessionTimeOut", "sessionTimeOut");
                out.print(responseObject);
            }
        }
        if (operation.equals("PASSWORD_CHANGE_MAIL")) {
            String formValueSendPasswordChangeMail = request.getParameter("CSRFTokenSendPasswordChangedMail");
            if (sessionRequestId.equals(formValueSendPasswordChangeMail)) {
                MailNotifications mailNotifications = new MailNotifications();
                result = mailNotifications.getPasswordChangedMailStatus(request, username, operation);
                responseObject.put("passwordChangeMail", result);
                out.print(responseObject);
            } else {
                result = "sessionTimeOut";
                responseObject.put("sessionTimeOut", "sessionTimeOut");
                out.print(responseObject);
            }
        }
        if (operation.equals("VALIDATE_TEMP_PASSWORD")) {
            String formValueFirstTimeResetPassword = request.getParameter("CSRFTokenFirstTimeResetPassword");
            if (sessionRequestId.equals(formValueFirstTimeResetPassword)) {
                boolean isPasswordExist = validateTempPassword(request);
                responseObject.put("isPasswordExist", isPasswordExist);
                out.print(responseObject);
            } else {
                result = "sessionTimeOut";
                responseObject.put("sessionTimeOut", "sessionTimeOut");
                out.print(responseObject);
            }
        }
        if (operation.equals("RESET_LASTLOGIN_AT")) {
            String formValueResetLastLogin = request.getParameter("CSRFTokenResetLastLogin");
            if (sessionRequestId.equals(formValueResetLastLogin)) {
                String userName = request.getParameter("userName");
                boolean isUpdateSuccess = false;
                try {
                    LoginService loginService = new LoginService();
                    Date currentDateTime = null;
                    isUpdateSuccess = loginService.updateLastLoggedIn(userName, currentDateTime);
                } catch (Exception exe) {
                    exe.printStackTrace();
                }
                responseObject.put("isLastLoginUpdateSuccess", isUpdateSuccess);
                out.print(responseObject);
            } else {
                result = "sessionTimeOut";
                responseObject.put("sessionTimeOut", "sessionTimeOut");
                out.print(responseObject);
            }
        }
        if (operation.equals("DELETED_USERS_LIST_MAIL")) {
            String formValueResetDeletedUserList = request.getParameter("CSRFTokenDeleteUser");
            if (sessionRequestId.equals(formValueResetDeletedUserList)) {
                MailNotifications mailNotifications = new MailNotifications();
                String mailStatus = mailNotifications.getDeletedUserMailStatus(session, request);
                responseObject.put("isDeletedUserMailSentSuccess", mailStatus);
                out.print(responseObject);
            } else {
                result = "sessionTimeOut";
                responseObject.put("sessionTimeOut", "sessionTimeOut");
                out.print(responseObject);
            }
        }
        if (operation.equals("FORGET_PASSWORD")) {
            ServletContext context = getServletContext();
            String sessionTokenForLogin = context.getInitParameter("TOKEN");
            String formValueForgetPassword = request.getParameter("CSRFTokenForgetPassword");
            if (sessionTokenForLogin.equals(formValueForgetPassword)) {
                String userName = request.getParameter("userName");
                LoginDao loginDao = new LoginDao();
                String dbUsername = loginDao.getDBUsername(userName);
                if (dbUsername != null) {
                    MailNotifications mailNotifications = new MailNotifications();
                    result = mailNotifications.forgotPassword(request, operation);
                } else {
                    result = "userNotFound";
                }
                responseObject.put("forgetPasswordMail", result);
                out.print(responseObject);
            } else {
                result = "sessionTimeOut";
                responseObject.put("sessionTimeOut", "sessionTimeOut");
                out.print(responseObject);
            }
        }
        if (operation.equals("SET_TERMS_AND_CONDITION")) {
            String formValueResetLastLogin = request.getParameter("CSRFTokenResetLastLogin");
            if (sessionRequestId.equals(formValueResetLastLogin)) {
                MailNotifications mailNotifications = new MailNotifications();
                boolean isUpdateSuccess = mailNotifications.checkTermsAndCondition(request);
                responseObject.put("isTermsAndConditionUpdateSuccess", isUpdateSuccess);
                out.print(responseObject);
            } else {
                result = "sessionTimeOut";
                responseObject.put("sessionTimeOut", "sessionTimeOut");
                out.print(responseObject);
            }
        }
    }

    /**
     * This method authenticates user email.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException exception
     * @throws IOException exception
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MailNotifications mailNotifications = new MailNotifications();
        String contextRoot = getServletContext().getContextPath();
        String decodedOperation = mailNotifications.getDecodedString(request
                .getParameter("adgo"));
        String[] logDetails = null;
        logDetails = new String[] {decodedOperation};
        PropertiesUtil.appendLog("MF-IN-008", logDetails, false);
        if (decodedOperation.equals("EMAIL_AUTHENTICATION_BY_USER")) {
            UserEmailService userEmailService = new UserEmailService();
            String decodedUserName = mailNotifications.getDecodedString(request
                    .getParameter("afgi"));
            String decodedSubscriberName = mailNotifications
                    .getDecodedString(request.getParameter("akls"));
            String decodedActionPerformed = mailNotifications
                    .getDecodedString(request.getParameter("abap"));
            logDetails = new String[] {decodedUserName, new Date().toString()};
            PropertiesUtil.appendLog("MF-IN-010", logDetails, false);
            Date verificationSendAtDate = null;
            Date verifiedAtDate = null;
            List<User> userInformationList = userEmailService
                    .getUserInformation(decodedUserName);
            if (userInformationList != null && userInformationList.size() > 0) {
                verificationSendAtDate = userInformationList.get(0)
                        .getVerificationSendAt();
                verifiedAtDate = userInformationList.get(0).getVerifiedAt();
                String expiredMiliSecondes = request.getParameter("ahgt");
                long expiredDateValue = Long.parseLong(expiredMiliSecondes);
                Date expiredDate = new Date(expiredDateValue);
                if (verifiedAtDate == null && verificationSendAtDate != null) {
                    boolean isLinkExpired = mailNotifications.isMailExpire(
                            verificationSendAtDate, expiredDate);
                    logDetails = new String[] {"Validity of link expire: "
                            + isLinkExpired };
                    PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
                    if (!isLinkExpired) {
                        Date currentDateTime = new Date(
                                System.currentTimeMillis());
                        boolean isUpdateSuccess = userEmailService
                                .updateUserVerifiedAt(decodedUserName,
                                        currentDateTime);
                        if (isUpdateSuccess && decodedActionPerformed.equals("CREATE_USER")) {
                            HttpSession session = request.getSession(true);
                            session.setAttribute("emailVerified",
                                    "VERIFICATION_MAIL_SUCCESS");
                            mailNotifications
                                    .sendMailToSubscriberAfterMailVerification(
                                            decodedSubscriberName,
                                            decodedUserName);
                        } else if (isUpdateSuccess && decodedActionPerformed.equals("EDIT_USER")) {
                            HttpSession session = request.getSession(true);
                            session.setAttribute("emailVerified",
                                    "VERIFICATION_MAIL_SUCCESS");
                        }
                    } else {
                        HttpSession session = request.getSession(true);
                        session.setAttribute("emailVerified",
                                "VERIFICATION_MAIL_LINK_EXPIRE");
                    }
                } else if (verifiedAtDate != null
                        && verificationSendAtDate != null) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("emailVerified",
                            "VERIFICATION_MAIL_ALREADY_VERIFIED");
                }
            }
            response.sendRedirect(contextRoot + "/emailVerification.jsp");
        }
    }
    /**
     * This method returns update status for user status insertion
     * operation.
     * @param request
     * @param username
     * @return isUpdateSuccess
     */
    private boolean setUserStatus(HttpServletRequest request, String username) {
        UserEmailService userEmailService = new UserEmailService();
        Date currentDateTime = new Date(System.currentTimeMillis());
        String status = request.getParameter("status");
        boolean isUpdateSuccess = userEmailService.updateUserStatus(
                username, currentDateTime, status);
        return isUpdateSuccess;
    }
    /**
     * This method validates temporary password.
     * @param request
     * @return isPasswordExist
     */
    private boolean validateTempPassword(HttpServletRequest request) {
        String password = request.getParameter("tempPassword");
        String userName = request.getParameter("username");
        UserEmailService userEmailService = new UserEmailService();
        boolean isPasswordExist = false;
        try {
            isPasswordExist = userEmailService.validatePassword(userName, password);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        return isPasswordExist;
    }
}
