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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.fujitsu.dc.gui.portal.service.RegistrationService;
/**
 * This servlet checks the availability of userid.
 */
public class CheckServlet extends HttpServlet {
      /**
       * serialVersionUID.
       */
     private static final long serialVersionUID = 1910997114681403821L;
     /*
      * (non-Javadoc)
      * @see
      * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
      * , javax.servlet.http.HttpServletResponse)
      */
     @Override
     protected void doGet(HttpServletRequest request,
               HttpServletResponse response) throws ServletException, IOException {
          doPost(request, response);
     }

     /*
      * (non-Javadoc)
      * @see
      * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
      * , javax.servlet.http.HttpServletResponse)
      */
     @Override
     protected void doPost(HttpServletRequest request,
               HttpServletResponse response) throws ServletException, IOException {
         RegistrationService registrationService = new RegistrationService();
          JSONObject responseObject = new JSONObject();
          PrintWriter out = response.getWriter();
          response.setContentType("text/html;charset=UTF-8");
          String userid = request.getParameter("useridtext");
          /*Pattern pattern = Pattern.compile("^[0-9a-zA-Z-_]+$");
          Matcher matcher = pattern.matcher(userid);
          boolean found = matcher.find();*/
          boolean isExist = false;
          /*if (!found) {
               out.println("<font color=red>Only '-' and '_' are allowed</font>");
          } else {*/
               try {
                    isExist = registrationService.checkUserStatus(userid);
                    if (isExist) {
                         responseObject.put("userIdMsg", "inUse");
                    /*out.println("<font color=red><b>" + userid
                              + "</b> is already in use</font>");*/
                    } else {
                         /*out.println("<font color=green><b>" + userid
                                   + "</b> is avaliable");*/
                         responseObject.put("userIdMsg", "available");
                    }
                    out.print(responseObject);
               } catch (Exception ex) {
                    out.println("Error ->" + ex.getMessage());
               } finally {
                    out.close();
               }
     /*     }*/
     }
}
