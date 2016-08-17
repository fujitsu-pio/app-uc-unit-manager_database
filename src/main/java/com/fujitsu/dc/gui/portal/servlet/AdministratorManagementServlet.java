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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fujitsu.dc.gui.portal.PCSCustomException;
import com.fujitsu.dc.gui.portal.PCSConstants;
import com.fujitsu.dc.gui.portal.SHAHashing;
import com.fujitsu.dc.gui.portal.model.User;
import com.fujitsu.dc.gui.portal.model.UserBean;
import com.fujitsu.dc.gui.portal.service.AdministratorManagementService;
import com.fujitsu.dc.gui.portal.service.LoginService;

/**
 * Servlet implementation class AdministratorManagementServlet.
 */
@WebServlet("/AdministratorManagementServlet")
public class AdministratorManagementServlet extends HttpServlet {
     private static final long serialVersionUID = 1L;

     /**
      * @see HttpServlet#HttpServlet()
      */
     public AdministratorManagementServlet() {
          super();
     }

     /**
      * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
      *      response)
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @throws ServletException exception
      * @throws IOException exception
      */
     protected void doPost(HttpServletRequest request,
               HttpServletResponse response) throws ServletException, IOException {
          String contentTypeHeader = "application/json; charset=" + PCSConstants.UTF8 + "";
          response.setHeader("Content-Type", contentTypeHeader);
          response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
          response.setHeader("Pragma", "no-cache");
          HttpSession session = request.getSession(false);
          JSONObject responseObject = new JSONObject();
          PrintWriter out = response.getWriter();
          boolean insertStatus = false;
          String result = null;
          if (session != null) {
               try {
               String sessionRequestId = (String) session.getAttribute("requestId");
                    String operation = request.getParameter("operation");
                    if (operation.equals("CREATE_USER")) {
                        String formValueCreateUSer = request.getParameter("CSRFTokenCreateUser");
                        if (sessionRequestId.equals(formValueCreateUSer)) {
                        insertStatus = createNewUser(request, session);
                         if (insertStatus) {
                              result = "createSuccess";
                              responseObject.put("createNewUser", result);
                              out.print(responseObject);
                         }
                        } else {
                            result = "sessionTimeOut";
                            responseObject.put("sessionTimeOut", "sessionTimeOut");
                            out.print(responseObject);
                        }
                    }
                    if (operation.equals("UPDATE_USER_DETAILS")) {
                         String formValueEditUSer = request.getParameter("CSRFTokenEditUser");
                         if (sessionRequestId.equals(formValueEditUSer)) {
                         result = updateUserDetails(request);
                         responseObject.put("adminMgmntEditUserInfo", result);
                         out.print(responseObject);
                         } else {
                             result = "sessionTimeOut";
                             responseObject.put("sessionTimeOut", "sessionTimeOut");
                             out.print(responseObject);
                         }
                    }
                    if (operation.equals("PASSWORD_SETTINGS")) {
                         String formValuePassword = request.getParameter("CSRFTokenPassword");
                         if (sessionRequestId.equals(formValuePassword)) {
                         boolean passwordUpdateStatus = updatePassword(request);
                         responseObject.put("passwordUpdateStatus",
                                   passwordUpdateStatus);
                         out.print(responseObject);
                         } else {
                             result = "sessionTimeOut";
                             responseObject.put("sessionTimeOut", "sessionTimeOut");
                             out.print(responseObject);
                         }

                    }
                    if (operation.equals("DELETE_USER")) {
                         String formValueDelete = request.getParameter("CSRFTokenDeleteUser");
                         if (sessionRequestId.equals(formValueDelete)) {
                         boolean deleteUserStatus = false;
                         deleteUserStatus = deletUser(request);
                         responseObject.put("deleteStatus", deleteUserStatus);
                         out.print(responseObject);
                         } else {
                             result = "sessionTimeOut";
                             responseObject.put("sessionTimeOut", "sessionTimeOut");
                             out.print(responseObject);
                         }
                    }
               } catch (Exception exe) {
                    exe.printStackTrace();
               }
          } else {
               result = "sessionTimeOut";
               responseObject.put("sessionTimeOut", "sessionTimeOut");
               out.print(responseObject);
          }
     }

     /**
      * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @throws ServletException exception
      * @throws IOException exception
      */
     @Override
     protected void doGet(HttpServletRequest request,
               HttpServletResponse response) throws ServletException, IOException {
         String contentTypeHeader = "application/json; charset=" + PCSConstants.UTF8 + "";
         response.setHeader("Content-Type", contentTypeHeader);
          response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
          response.setHeader("Pragma", "no-cache");
          HttpSession session = request.getSession(false);
          PrintWriter out = response.getWriter();
          JSONObject activeUserListJson = new JSONObject();
          try {
              if (session != null) {
                    JSONArray usersListJson = retrieveActiveUserList(session);
                    activeUserListJson.put("activeUsersList", usersListJson);
                    response.setContentType("application/json");
                    out.print(activeUserListJson);
              } else {
                  activeUserListJson.put("sessionTimeOut", "sessionTimeOut");
                  out.print(activeUserListJson);
              }
          } catch (Exception exe) {
              exe.printStackTrace();
          }
     }

     /**
      * This method creates a new user.
      * @param request HttpServletRequest
      * @param session HttpSession
      * @return boolean status
      * @throws UnsupportedEncodingException exception
      */
     protected boolean createNewUser(HttpServletRequest request,
               HttpSession session) throws UnsupportedEncodingException {
          request.setCharacterEncoding(PCSConstants.UTF8);
          String superUserID = (String) session.getAttribute("id");
          String organizationID = null;
          boolean insertStatus = false;
          String username = request.getParameter("username");
          String password = request.getParameter("password");
          String firstName = URLDecoder.decode(request.getParameter("firstName"), PCSConstants.UTF8);
          String familyName = URLDecoder.decode(request.getParameter("familyName"), PCSConstants.UTF8);
          String email = URLDecoder.decode(request.getParameter("encodedEAddress"), PCSConstants.UTF8);
          String privilege = request.getParameter("privilege");
          // The following regular expression finds string of white spaces.
          firstName = firstName.replaceAll("^ +| +$|( )+", " ");
          familyName = familyName.replaceAll("^ +| +$|( )+", " ");
          SHAHashing shaHashing = new SHAHashing();
          LoginService loginService = new LoginService();
          try {
               organizationID = loginService.getOrganizationID(superUserID);
               String salt = UUID.randomUUID().toString();
               String encryptedPass = shaHashing.createHashedPassword(password, salt)
                         .trim();
               // The following regular expression finds string of white spaces.
               encryptedPass = encryptedPass.replaceAll("^ +| +$|( )+", " ");
               // The following regular expression finds a non-word character.
               encryptedPass = encryptedPass.replaceAll("\\W", "");
               encryptedPass = null;
               salt = null;
               //String salt = SHAHashing.salt;
               Date createdAt = new Date(System.currentTimeMillis());
               Date updatedAt = new Date(System.currentTimeMillis());
               Date passwordResetAt = null;
               Date lastLoginAt = null;
               Date verificationSendAt = null;
               Date verifiedAt = null;
               String status = PCSConstants.INACTIVE_USER_STATUS;
               String randomId = UUID.randomUUID().toString();
               Date termsAndConditionAcceptedAt = null;
               AdministratorManagementService administratorManagementService =
                       new AdministratorManagementService();
               UserBean userBean = new UserBean();
               userBean.setCreatedAt(createdAt);
               userBean.setEmail(email);
               userBean.setEncryptedPass(encryptedPass);
               userBean.setFamilyName(familyName);
               userBean.setFirstName(firstName);
               userBean.setLastLoginAt(lastLoginAt);
               userBean.setOrganizationID(organizationID);
               userBean.setPasswordResetAt(passwordResetAt);
               userBean.setPrivilege(privilege);
               userBean.setRandomId(randomId);
               userBean.setSalt(salt);
               userBean.setStatus(status);
               userBean.setUpdatedAt(updatedAt);
               userBean.setUsername(username);
               userBean.setVerificationSendAt(verificationSendAt);
               userBean.setVerifiedAt(verifiedAt);
               userBean.setTermsAndConditionAcceptedAt(termsAndConditionAcceptedAt);
               insertStatus = administratorManagementService.createNewUser(userBean);
          } catch (Exception exe) {
              exe.printStackTrace();
          }
          return insertStatus;
     }

     /**
      * This method fetches the list of active users.
      * @param session HttpSession
      * @return JSONArray usersListJson
     * @throws Exception exception
      */
     protected JSONArray retrieveActiveUserList(HttpSession session) throws Exception {
          JSONObject userDetailsJson = null;
          JSONArray usersListJson = new JSONArray();
          String username = null;
          String firstName = null;
          String familyName = null;
          String email = null;
          String privilege = null;
          String strRegistrationDate = null;
          String strUpdatedDate = null;
          java.util.Date registrationDate = null;
          java.util.Date updatedDate = null;
          java.util.Date verificationSendAtDate = null;
          java.util.Date verifiedAtDate = null;
          java.util.Date lastLoginAtDate = null;
          String strVerificationSendAtDate = null;
          String strVerifiedAtDate = null;
          String strLastLoginAtDate = null;
          String userStatus = null;
          String userOrgID = null;
          int lockStatus = 0;
          String status = PCSConstants.DEFAULT_USER_STATUS;
          AdministratorManagementService administratorManagementService =
                  new AdministratorManagementService();
          String superUsername = (String) session.getAttribute("id");
          LoginService loginservice = new LoginService();
          String orgID = loginservice.getOrganizationID(superUsername);
          List<User> activeUsersList = administratorManagementService
                    .getActiveUsersList(status, orgID);
          if (activeUsersList != null && activeUsersList.size() > 0) {
               for (Iterator iterator = activeUsersList.iterator(); iterator
                         .hasNext();) {
                   strVerificationSendAtDate = null;
                   strVerifiedAtDate = null;
                   strLastLoginAtDate = null;
                   strUpdatedDate = null;
                   strRegistrationDate = null;
                    userDetailsJson = new JSONObject();
                    User user = (User) iterator.next();
                    username = user.getName();
                    firstName = user.getFirstName();
                    familyName = user.getFamilyName();
                    email = user.getEmail();
                    privilege = user.getPrivilege();
                    registrationDate = user.getCreatedAt();
                    updatedDate = user.getUpdatedAt();
                    userStatus = user.getStatus();
                    userOrgID = orgID;
                    verificationSendAtDate = user.getVerificationSendAt();
                    verifiedAtDate = user.getVerifiedAt();
                    lastLoginAtDate = user.getLastLoginAt();
                    if (registrationDate != null) {
                        strRegistrationDate = registrationDate.toString();
                    }
                    if (verificationSendAtDate != null) {
                        strVerificationSendAtDate = verificationSendAtDate.toString();
                    }
                    if (verifiedAtDate != null) {
                        strVerifiedAtDate = verifiedAtDate.toString();
                    }
                    if (lastLoginAtDate != null) {
                        strLastLoginAtDate = lastLoginAtDate.toString();
                    }
                    lockStatus = user.getLockStatus();
                    if (updatedDate != null) {
                         strUpdatedDate = updatedDate.toString();
                    } else {
                          strUpdatedDate = "Not available";
                    }
                    userDetailsJson.put("username", username);
                    userDetailsJson.put("firstName", firstName);
                    userDetailsJson.put("familyName", familyName);
                    userDetailsJson.put("email", email);
                    userDetailsJson.put("privilege", privilege);
                    userDetailsJson.put("registrationDate", strRegistrationDate);
                    userDetailsJson.put("updatedDate", strUpdatedDate);
                    userDetailsJson.put("lockStatus", lockStatus);
                    userDetailsJson.put("verificationSendAtDate", strVerificationSendAtDate);
                    userDetailsJson.put("userStatus", userStatus);
                    userDetailsJson.put("userOrgID", userOrgID);
                    userDetailsJson.put("verifiedAtDate", strVerifiedAtDate);
                    userDetailsJson.put("lastLoginAtDate", strLastLoginAtDate);
                    usersListJson.add(userDetailsJson);
               }
          }
          return usersListJson;
     }

     /**
      * This method updates the password.
      * @param request HttpServletRequest
      * @return boolean status
      */
     protected boolean updatePassword(HttpServletRequest request) {
          boolean passwordUpdateStatus = false;
          String userName = request.getParameter("userName");
          String newPassword = request.getParameter("newPassword");
          String encryptedPass = null;
          SHAHashing shaHashing = new SHAHashing();
          String salt = null;
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
          Date updatedAt = new Date(System.currentTimeMillis());
          Date passwordResetAt = new Date(System.currentTimeMillis());
          AdministratorManagementService administratorManagementService =
                  new AdministratorManagementService();
          passwordUpdateStatus = administratorManagementService.updatePassword(
                    userName, encryptedPass, salt, updatedAt, passwordResetAt);
          return passwordUpdateStatus;
     }

     /**
      * This method updates the user details.
      * @param request HttpServletRequest
      * @return String result
      * @throws UnsupportedEncodingException exception
      */
     protected String updateUserDetails(HttpServletRequest request) throws UnsupportedEncodingException {
          request.setCharacterEncoding(PCSConstants.UTF8);
          String result = null;
          String userName = request.getParameter("userName");
          String newFirstName = URLDecoder.decode(request.getParameter("newFirstName"), PCSConstants.UTF8);
          String newFamilyName = URLDecoder.decode(request.getParameter("newFamilyName"), PCSConstants.UTF8);
          String newEmail =  URLDecoder.decode(request.getParameter("encodedNewEAddress"), PCSConstants.UTF8);
          String newPrivilege = request.getParameter("newPrivilege");
          //The following regular expression finds string of white spaces.
          newFirstName = newFirstName.replaceAll("^ +| +$|( )+", " ");
          newFamilyName = newFamilyName.replaceAll("^ +| +$|( )+", " ");
          if (userName != null && newFirstName != null && newFamilyName != null
                    && newEmail != null && newPrivilege != null) {
               Date updatedAt = new Date(System.currentTimeMillis());
               AdministratorManagementService administratorManagementService =
                       new AdministratorManagementService();
               result = administratorManagementService.updateAdminMgmntUserInfo(
                         userName, newFirstName, newFamilyName, newEmail, updatedAt,
                         newPrivilege);

          }
          return result;
     }

     /**
      * This method deletes the user.
      * @param request HttpServletRequest
      * @return boolean status
      */
     protected boolean deletUser(HttpServletRequest request) {
          boolean deleteUserStatus = false;
          String userName = request.getParameter("deleteUsername");
          AdministratorManagementService administratorManagementService =
                  new AdministratorManagementService();
          Date updatedAt = new Date(System.currentTimeMillis());
          String status = PCSConstants.INACTIVE_USER_STATUS;
          deleteUserStatus = administratorManagementService.deleteUser(userName,
                    updatedAt, status);
          return deleteUserStatus;
     }
}
