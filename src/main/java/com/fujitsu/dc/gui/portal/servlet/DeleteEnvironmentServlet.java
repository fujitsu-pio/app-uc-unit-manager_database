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
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.fujitsu.dc.gui.portal.service.DeleteEnvironmentService;

/**
 * This servlet is used for performing operations during Delete Environment.
 */
public class DeleteEnvironmentServlet extends HttpServlet {
     private static final long serialVersionUID = 1L;
    /* @Override
     protected void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
     }*/
     /**
      * This method deletes an environment for the given user.
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
          try {
               HttpSession session = request.getSession(false);
               if (session != null) {
                    String envtID = request.getParameter("envID");
                    if (envtID != null) {
                         DeleteEnvironmentService deleteEnvironmentService = new DeleteEnvironmentService();
                         result = deleteEnvironmentService.deleteEnvironment(envtID);
                    } else {
                         result = "sessionTimeOut";
                    }
               }
               responseObject.put("deleteEnvtResult", result);
               out.print(responseObject);
          } catch (Exception exception) {
              exception.printStackTrace();
               result = "error";
               responseObject.put("deleteEnvtResult", result);
               out.print(responseObject);
          }
     }
    /* public static void main(String[] args) {
     }*/
}
