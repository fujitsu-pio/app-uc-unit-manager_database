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
package io.personium.gui.portal.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONObject;

import io.personium.gui.portal.PersoniumConstants;
import io.personium.gui.portal.PropertiesUtil;
import io.personium.gui.portal.model.User;
import io.personium.gui.portal.service.CreateEnvironmentService;
import io.personium.gui.portal.service.LoginService;
import io.personium.gui.portal.service.UserEmailService;

/**
 * The purpose of this class is to validate user against the database.
 */
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    /**
     * This method validates the username and password and performs login operation.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException exception
     * @throws IOException exception
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        session = request.getSession(true);
        if (session != null) {
            String[] logDetails = new String[]{request.getHeader("User-Agent")};
            PropertiesUtil.appendLog("LG-IN-003", logDetails, false);
            session.invalidate();
            Cookie[] cookies = request.getCookies();
            response.setHeader("Content-Type", "application/json; charset=UTF-8");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    cookie.setValue("-");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        Boolean isUserExist = false;
        int organizationStatus = 0;
        String organizationID = null;
        Date currentDateTime = new Date(System.currentTimeMillis());
        String name = request.getParameter("userId");
        String password = request.getParameter("passwd");
        String selectedLanguageType = request.getParameter("ddLanguageSelector");
        response.setContentType("text/html");
        LoginService loginService = new LoginService();
        UserEmailService userEmailService = new UserEmailService();
        ServletContext context = getServletContext();
        String sessionTokenForLogin = context.getInitParameter("TOKEN");
        String clientTokenForLogin = request.getParameter("CSRFTokenLogin");
        try {
            List<User> userinfo = null;
            int lockValue = 0;
            Date verifiedAt = null;
            if (sessionTokenForLogin.equals(clientTokenForLogin)) {
            Date updatedAt = new Date(System.currentTimeMillis());
            if (name != null) {
                userinfo = userEmailService.getUserInformation(name);
                if (userinfo.size() > 0) {
                    lockValue = userinfo.get(0).getLockStatus();
                    verifiedAt = userinfo.get(0).getVerifiedAt();
                    String emailTo = userinfo.get(0).getEmail();
                    String firstName = userinfo.get(0).getFirstName();
                    String familyName = userinfo.get(0).getFamilyName();
                    if (lockValue >= PersoniumConstants.MAX_LOCK_STATUS) {
                        MailNotifications mailNotifications = new MailNotifications();
                            mailNotifications.sendMailForAccountLocked(name,
                                    emailTo, firstName, familyName);
                    } else if (lockValue < PersoniumConstants.MAX_LOCK_STATUS && verifiedAt == null) {
                        session = request.getSession(true);
                        boolean emailNotVerified = true;
                        String contextPath = request.getContextPath();
                        contextPath = getFullHttpsRootUrl(request);//Hack for K5
                        session.setAttribute("errorFlag", emailNotVerified);
                        session.setAttribute("errorMessage",
                                PersoniumConstants.EMAIL_NOT_VERIFIED_MSG);
                        response.sendRedirect(contextPath);
                        return;
                    }
                }
            }
            isUserExist = loginService.validateUsernamePassword(name, password,
                    updatedAt);
                if (isUserExist && lockValue < PersoniumConstants.MAX_LOCK_STATUS
                        && verifiedAt != null) {
                Date lastLoginAt = userinfo.get(0).getLastLoginAt();
                Date termsAndConditionAcceptedAt = userinfo.get(0).getTermsAndConditionAcceptedAt();
                Date passwordResetAt = userinfo.get(0).getPasswordResetAt();
                organizationStatus = loginService.checkOrganizationStatus(name);
                organizationID = loginService.getOrganizationID(name);
                String dbUsername = userinfo.get(0).getName();
                if (organizationStatus == 0) {
                    session = request.getSession(true);
                    boolean organizationNotActivated = true;
                    String contextPath = request.getContextPath();
                    contextPath = getFullHttpsRootUrl(request);//Hack for K5
                    session.setAttribute("errorFlag", organizationNotActivated);
                    session.setAttribute("errorMessage",
                            PersoniumConstants.LOGIN_ORG_NOTACTIVATED_MSG);
                    response.sendRedirect(contextPath);
                } else {
                    boolean isLastLoggedInUpdateSuccess = loginService
                            .updateLastLoggedIn(name, currentDateTime);
                    if (isLastLoggedInUpdateSuccess) {
                        session = request.getSession(true);
                        session.setAttribute("id", dbUsername);
                        session.setAttribute("organizationID", organizationID);
                        UUID randomUUID = UUID.randomUUID();
                        String requestId = randomUUID.toString();
                        String contextRoot = getServletContext()
                                .getContextPath();
                        contextRoot = getFullHttpsRootUrl(request);//Hack for K5.
                        session.setAttribute("contextRoot", contextRoot);
                        session.setAttribute("requestId", requestId);
                        boolean isFirstTimeUser = firstTimeUser(
                                termsAndConditionAcceptedAt, passwordResetAt, lastLoginAt);
                        session.setAttribute("selectedLanguage", selectedLanguageType);
                            if (isFirstTimeUser) {
                            String firstName = userinfo.get(0).getFirstName();
                            String familyName = userinfo.get(0).getFamilyName();
                            String fullName = firstName + " " + familyName;
                            fullName = StringEscapeUtils.escapeHtml(fullName);
                            fullName = fullName.replaceAll("\'", "↔");
                                session.setAttribute("firstTimeUserFullName",
                                        fullName);
                                response.sendRedirect(contextRoot + "/htmls/"
                                        + selectedLanguageType
                                        + "/firstTimeUser.jsp");
                        } else {
                            response.sendRedirect(contextRoot + "/htmls/" + selectedLanguageType + "/home.jsp");
                        }
                    }
                }
            } else {
                session = request.getSession(true);
                boolean invalidUID = true;
                String contextPath = request.getContextPath();
                contextPath = getFullHttpsRootUrl(request);//Hack for K5
                session.setAttribute("errorFlag", invalidUID);
                session.setAttribute("errorMessage",
                        PersoniumConstants.LOGIN_INVALID_CREDENTIALS_MSG);
                response.sendRedirect(contextPath);
            }
            } else {
                String contextPath = request.getContextPath();
                contextPath = getFullHttpsRootUrl(request);//Hack for K5
                response.sendRedirect(contextPath);
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }
    
    private String getFullHttpsRootUrl(HttpServletRequest hsr) {
    	return "https://" + hsr.getServerName();
    }
    
    /**
     * This method fetches the list of environments with unit urls.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException exception
     * @throws IOException exception
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        HttpSession session = null;
        session = request.getSession(false);
        String organizationID = (String) session.getAttribute("organizationID");
        Map<Integer, List<String>> unitUrlList = null;
        String organizationName = null;
        LoginService loginService = new LoginService();
        JSONObject responseObject = new JSONObject();
        PrintWriter out = response.getWriter();
        try {
            CreateEnvironmentService createEnvironmentService = new CreateEnvironmentService();
            unitUrlList = createEnvironmentService
                    .getUnitURLList(organizationID);
            Map<String, List<List<String>>> environmentList = loginService
                    .getEnvironmentList(organizationID);
            Map<Integer, Boolean> notificationStatusList = loginService
                    .getNotificationStatusList(unitUrlList);
            UserEmailService userEmailService = new UserEmailService();
            organizationName = userEmailService.getUserOrganizationName(organizationID);
            responseObject.put("unitURLList", unitUrlList);
            responseObject.put("environmentList", environmentList);
            responseObject.put("notificationStatusList", notificationStatusList);
            responseObject.put("organizationName", organizationName);
            out.print(responseObject);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }
    /**
     * This method checks the first time user validity.
     * @param termsAndConditionAcceptedAt termsAndConditionAcceptedAt
     * @param passwordResetAt passwordResetAt
     * @param lastLoginAt lastLoginAt
     * @return boolean status
     */
    protected boolean firstTimeUser(Date termsAndConditionAcceptedAt,
            Date passwordResetAt, Date lastLoginAt) {
        boolean isFirstTimeUser = false;
        if (lastLoginAt == null || termsAndConditionAcceptedAt == null) {
            isFirstTimeUser = true;
        } else if (termsAndConditionAcceptedAt != null
                && passwordResetAt != null) {
            long termsAndConditionAcceptedAtInMilliseconds = termsAndConditionAcceptedAt
                    .getTime();
            long passwordResetAtInMilliSeconds = passwordResetAt.getTime();
            if (termsAndConditionAcceptedAtInMilliseconds > passwordResetAtInMilliSeconds) {
                isFirstTimeUser = true;
            }
        }
        return isFirstTimeUser;
    }
}
