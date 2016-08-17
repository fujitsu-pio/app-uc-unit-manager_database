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
import java.sql.Date;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fujitsu.dc.gui.portal.PCSConstants;
import com.fujitsu.dc.gui.portal.PropertiesUtil;
import com.fujitsu.dc.gui.portal.SHAHashing;
import com.fujitsu.dc.gui.portal.model.UserBean;
import com.fujitsu.dc.gui.portal.service.RegistrationService;
/**
 * The purpose of this class is to handle with unit user registration logic.
 *
 */
     public class UnitUserRegisterServlet extends HttpServlet {
     private static final long serialVersionUID = 1L;
     /**
      * This method performs registration operation for unit user.
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @throws ServletException exception
      * @throws IOException exception
      */
     @Override
     public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
          request.setCharacterEncoding(PCSConstants.UTF8);
          Properties refPropertiesUtil = PropertiesUtil.getProperties();
          RegistrationService registrationService = new RegistrationService();
          String id = request.getParameter("cid").trim();
          String passd = request.getParameter("cpassd").trim();
          String firstName = request.getParameter("cfName").trim();
          String familyName = request.getParameter("cfamilyName").trim();
          String orgName = request.getParameter("coName").trim();
          String email = request.getParameter("cemail").trim();
          Date createdAt = new Date(System.currentTimeMillis());
          Date updatedAt = new Date(System.currentTimeMillis());
          Date passwordResetAt = null;
          Date lastLoginAt = null;
          Date verificationSendAt = null;
          Date verifiedAt = null;
          Date termsAndConditionAcceptedAt = null;
          boolean insertStatus = false;
          String status = refPropertiesUtil.getProperty("active");
          String randomId = UUID.randomUUID().toString();
          String orgId = registrationService.generateRandomNumber();
          //HttpSession session = request.getSession();
          HttpSession session = null;
          session = request.getSession(false);
          session.invalidate();
          SHAHashing shahash = new SHAHashing();
          // The following regular expression finds string of white spaces.
          id = id.replaceAll("^ +| +$|( )+", " ");
          // The following regular expression finds a whitespace character.
          id = id.replaceAll("\\s", "");
          // The following regular expression finds string of white spaces.
          passd = passd.replaceAll("^ +| +$|( )+", " ");
          // The following regular expression finds a whitespace character.
          passd = passd.replaceAll("\\s", "");
          // The following regular expression finds string of white spaces.
          firstName = firstName.replaceAll("^ +| +$|( )+", " ");
          // The following regular expression finds string of white spaces.
          familyName = familyName.replaceAll("^ +| +$|( )+", " ");
          // The following regular expression finds string of white spaces.
          orgName = orgName.replaceAll("^ +| +$|( )+", " ");
          // The following regular expression finds string of white spaces.
          email = email.replaceAll("^ +| +$|( )+", " ");
          String privilege = refPropertiesUtil.getProperty("subscriber");
          session = request.getSession(true);
          session.setAttribute("id", id);
          int  orgStatus = 0;
          ServletContext context = getServletContext();
          String sessionTokenForRegister = context.getInitParameter("TOKEN");
          String cSRFTokenUserRegistration = request.getParameter("CSRFTokenUserRegistration");
          try {
              if (sessionTokenForRegister.equals(cSRFTokenUserRegistration)) {
               String salt = UUID.randomUUID().toString();
               String encryptPassd = shahash.createHashedPassword(passd, salt).trim();
               // The following regular expression finds string of white spaces.
               encryptPassd = encryptPassd.replaceAll("^ +| +$|( )+", " ");
               // The following regular expression finds a non-word character.
               encryptPassd = encryptPassd.replaceAll("\\W", "");
               //String salt = SHAHashing.salt;
               UserBean userBean = new UserBean();
               userBean.setUsername(id);
               userBean.setEncryptedPass(encryptPassd);
               userBean.setSalt(salt);
               userBean.setFirstName(firstName);
               userBean.setFamilyName(familyName);
               userBean.setEmail(email);
               userBean.setStatus(status);
               userBean.setPrivilege(privilege);
               userBean.setRandomId(randomId);
               userBean.setCreatedAt(createdAt);
               userBean.setUpdatedAt(updatedAt);
               userBean.setVerificationSendAt(verificationSendAt);
               userBean.setVerifiedAt(verifiedAt);
               userBean.setLastLoginAt(lastLoginAt);
               userBean.setPasswordResetAt(passwordResetAt);
               userBean.setOrgStatus(orgStatus);
               userBean.setOrganizationName(orgName);
               userBean.setOrganizationID(orgId);
               userBean.setTermsAndConditionAcceptedAt(termsAndConditionAcceptedAt);
               insertStatus = registrationService.registerUser(userBean);
               if (insertStatus) {
                    boolean flag = true;
                    //HttpSession ses = request.getSession();
                    session.putValue("flag", flag);
                    response.sendRedirect("login.jsp");
               } else {
                    response.sendRedirect("login.jsp");
               }
              } else {
                   response.sendRedirect("login.jsp");
               }
          } catch (IOException ioExe) {
              ioExe.printStackTrace();
          } catch (IllegalStateException exe) {
              exe.printStackTrace();
          } catch (Exception exe) {
              exe.printStackTrace();
          }
     }
}
