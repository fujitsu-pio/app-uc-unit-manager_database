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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import io.personium.gui.portal.PersoniumConstants;
import io.personium.gui.portal.service.CreateEnvironmentService;

/**
 * This servlet is used to perform operations related to create environment.
 */
public class CreateEnvironmentServlet extends HttpServlet {
     private static final long serialVersionUID = 1L;
     /**
      * This method fetches the list of available unit urls for the given user.
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @throws ServletException exception
      * @throws IOException exception
      */
     @Override
     protected void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
          HttpSession session = request.getSession(false);
          JSONObject responseObject = new JSONObject();
          PrintWriter out = response.getWriter();
          String orgID = (String) session.getAttribute("organizationID"); //request.getParameter("orgID");
          String contentTypeHeader = "application/json; charset=" + PersoniumConstants.UTF8 + "";
          response.setHeader("Content-Type", contentTypeHeader);
          response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
          response.setHeader("Pragma", "no-cache");
          CreateEnvironmentService createEnvironmentService = new CreateEnvironmentService();
          Map<Integer, List<String>> unitUrlList = null;
          try {
               unitUrlList = createEnvironmentService.getUnitURLList(orgID);
               responseObject.put("uniturllist", unitUrlList);
               out.print(responseObject);
          } catch (Exception exception) {
               responseObject.put("uniturllist", unitUrlList);
               out.print(responseObject);
          }
     }

     /**
      * This method creates an environment for the given user.
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @throws ServletException exception
      * @throws IOException exception
      */
     @Override
     protected void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
          String result = null;
          JSONObject responseObject = new JSONObject();
          PrintWriter out = response.getWriter();
          String contentTypeHeader = "application/json; charset=" + PersoniumConstants.UTF8 + "";
          response.setHeader("Content-Type", contentTypeHeader);
          response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
          response.setHeader("Pragma", "no-cache");
          try {
               HttpSession session = request.getSession(false);
               if (session != null) {
                    String sessionRequestId = (String) session.getAttribute("requestId");
                    String cSRFTokenCreateEnvironment = request.getParameter("CSRFTokenCreateEnvironment");
                    String envtName = request.getParameter("envtName");
                    String orgID = (String) session.getAttribute("organizationID");
                    String unitID = null;
                    if (request.getParameter("unitID") != null) {
                         unitID = request.getParameter("unitID");
                    }
                    String userName = (String) session.getAttribute("id");
                    if (sessionRequestId.equals(cSRFTokenCreateEnvironment)) {
                    if (envtName != null && orgID != null && unitID != null && userName != null) {
                         String envtID = UUID.randomUUID().toString();
                         CreateEnvironmentService createEnvironmentService = new CreateEnvironmentService();
                         result = createEnvironmentService.createEnvironment(envtName,
                                 orgID, Integer.parseInt(unitID), userName, envtID);
                    } else {
                         result = "sessionTimeOut";
                    }
                    } else {
                         result = "sessionTimeOut";
                    }
               } else {
                    result = "sessionTimeOut";
               }
               responseObject.put("createEnvtResult", result);
               out.print(responseObject);
          } catch (Exception exception) {
               exception.printStackTrace();
               result = "error";
               responseObject.put("createEnvtResult", result);
               out.print(responseObject);
          }
     }

}
