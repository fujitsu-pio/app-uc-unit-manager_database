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
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import io.personium.gui.portal.service.ChangePasswordService;

/**
 * Servlet implementation class ChangePasswordServlet.
 */
@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
     private static final long serialVersionUID = 1L;
     /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePasswordServlet() {
        super();
    }

     /**
      * This method perform change password operation.
      * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @throws ServletException exception
      * @throws IOException exception
      */
     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
     ServletException, IOException {
          String result = null;
          JSONObject responseObject = new JSONObject();
          PrintWriter out = response.getWriter();
          ChangePasswordService changePasswordService = new ChangePasswordService();
          try {
               HttpSession session = request.getSession(false);
               if (session != null) {
                   String sessionRequestId = (String) session.getAttribute("requestId");
                   String formValueChangePassword = request.getParameter("CSRFTokenChangePassword");
                   if (sessionRequestId.equals(formValueChangePassword)) {
                      String userName = (String) session.getAttribute("id");
                      String newPassword = request.getParameter("newPassword");
                      String currentPassword = request.getParameter("currentPassword");
                      String salt  = UUID.randomUUID().toString();
                      if (userName != null && newPassword != null && currentPassword != null) {
                         Date passwordResetAt = new Date();
                         Date updatedAt = new Date();
                         result = changePasswordService.validateAndUpdatePassword(userName,
                          currentPassword, newPassword, passwordResetAt, updatedAt, salt);
                      } else {
                         result = "sessionTimeOut";
                      }
                    } else {
                        result = "sessionTimeOut";
                    }
               } else {
                   result = "sessionTimeOut";
               }
               responseObject.put("changePassword", result);
               out.print(responseObject);
          } catch (Exception exception) {
               exception.printStackTrace();
               responseObject.put("changePassword", result);
               out.print(responseObject);
          }
     }

}
