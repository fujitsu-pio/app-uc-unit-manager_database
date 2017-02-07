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
import java.net.URLDecoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import io.personium.gui.portal.PersoniumConstants;
import io.personium.gui.portal.model.User;
import io.personium.gui.portal.service.EditUserInformationService;

/**
 * Servlet implementation class EditUserInformationServlet.
 */
@WebServlet("/EditUserInformationServlet")
public class EditUserInformationServlet extends HttpServlet {
     private static final long serialVersionUID = 1L;

     /**
      * @see HttpServlet#HttpServlet()
      */
     public EditUserInformationServlet() {
          super();
     }

     /**
      * The purpose of this method is to perform edit user information operation.
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @throws ServletException exception
      * @throws IOException exception
      */
     protected void doPost(HttpServletRequest request,
               HttpServletResponse response) throws ServletException, IOException {
          request.setCharacterEncoding(PersoniumConstants.UTF8);
          response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
          response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
          response.setDateHeader("Expires", 0); // Proxies.
          HttpSession session = request.getSession(false);
          JSONObject responseObject = new JSONObject();
          PrintWriter out = response.getWriter();
          String result = null;
          if (session != null) {
             String sessionRequestId = (String) session.getAttribute("requestId");
             String formValue = request.getParameter("csrfTokenValue");
             // String requestId = URLDecoder.decode(request.getParameter("requestID"));
             if (sessionRequestId.equals(formValue)) {
               EditUserInformationService editUserInformationService = null;
               String userName = (String) session.getAttribute("id");
               String newFirstName = URLDecoder.decode(request.getParameter("newFirstName"), PersoniumConstants.UTF8);
               String newFamilyName = URLDecoder.decode(request.getParameter("newFamilyName"), PersoniumConstants.UTF8);
               String newEmail = request.getParameter("newEmail");
               // The following regular expression finds string of white spaces.
               newFirstName = newFirstName.replaceAll("^ +| +$|( )+", " ");
               newFamilyName = newFamilyName.replaceAll("^ +| +$|( )+", " ");
               if (userName != null && newFirstName != null
                         && newFamilyName != null && newEmail != null) {
                    Date updatedAt = new Date();
                    editUserInformationService = new EditUserInformationService();
                    result = editUserInformationService.updateUserInfo(userName,
                              newFirstName, newFamilyName, newEmail, updatedAt);
               }
             } else {
                result = "sessionTimeOut";
             }
          } else {
               result = "sessionTimeOut";
          }
          responseObject.put("editUserInfo", result);
          out.print(responseObject);
     }

     /**
      * The purpose of this method is to fetch user information.
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @throws ServletException exception
      * @throws IOException exception
      */
     @Override
     protected void doGet(HttpServletRequest request,
               HttpServletResponse response) throws ServletException, IOException {
         String contentTypeHeader = "application/json; charset=" + PersoniumConstants.UTF8 + "";
          response.setHeader("Content-Type", contentTypeHeader);
          response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
          response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
          response.setDateHeader("Expires", 0); // Proxies.
          HttpSession session = request.getSession(false);
          EditUserInformationService editUserInformationService = null;
          JSONObject responseObject = new JSONObject();
          PrintWriter out = response.getWriter();
          if (session != null) {
               String userName = (String) session.getAttribute("id");
               editUserInformationService = new EditUserInformationService();
               List<User> userInfo = null;
               userInfo = editUserInformationService.getUserInfo(userName);
               if (userInfo != null && userInfo.size() > 0) {
                    for (Iterator iterator = userInfo.iterator(); iterator
                              .hasNext();) {
                         User user = (User) iterator.next();
                         responseObject
                                   .put("existingFirstName", user.getFirstName());
                         responseObject.put("existingFamilyName",
                                   user.getFamilyName());
                         responseObject.put("existingEmail", user.getEmail());
                    }
                    out.print(responseObject);
               }
          } else {
               responseObject.put("sessionTimeOut", "sessionTimeOut");
               out.print(responseObject);
          }
     }
}
