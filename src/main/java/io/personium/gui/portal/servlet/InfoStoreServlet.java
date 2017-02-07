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
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import io.personium.gui.portal.PropertiesUtil;

/**
 * This class is Responsible to pick various required parameters from session
 * and pass on to UI against specified requested parameter.
 * */
public class InfoStoreServlet extends HttpServlet {
     private static final long serialVersionUID = 1L;
     /**
      * This is the constructor for InfoStoreServlet.
      */
     public InfoStoreServlet() {
          super();
     }

    /**
     * This method fetches information about baseURL, id, revision number and token.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException exception
     * @throws IOException exception
     */
     @Override
     protected void doGet(HttpServletRequest request,
               HttpServletResponse response) throws ServletException, IOException {
          JSONObject responseObject = new JSONObject();
          HttpSession session = request.getSession(false);
          PrintWriter out = response.getWriter();
          response.setContentType("application/json");
          response.setHeader("Content-Type", "application/json; charset=UTF-8");
          response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
          response.setHeader("Pragma", "no-cache");
          /* Start: prepare and send final JSON response object */
          responseObject.put("baseURL", session.getAttribute("baseURL"));
          responseObject.put("token", session.getAttribute("accessToken"));
          responseObject.put("id", session.getAttribute("id"));
          responseObject.put("revisionNumber", getRevisionNumber());
          out.print(responseObject);
          /* End: prepare and send final JSON response object */
     }

     /**
      * This method fetches the revision number.
      * @return build number based on environment
      */
     private String getRevisionNumber() {
          String revisionNum = null;
          String[] logDetails = null;
          try {
            InputStream inputStream = getServletContext().getResourceAsStream(
                    "/META-INF/MANIFEST.MF");
            Properties properties = new Properties();
            properties.load(inputStream);
            // if (!(val != null && val.toLowerCase().contains("production"))) {
            revisionNum = properties.getProperty("HudsonSvnRevision");
            if (revisionNum != null) {
                logDetails = new String[]{revisionNum};
            }
            PropertiesUtil.appendLog("OT-IN-002", logDetails, false);
            if (revisionNum == null || revisionNum.trim().length() == 0) {
                revisionNum = "";
            }
            /*
             * revisionNum = (revisionNum == null || revisionNum.trim().length()
             * == 0) ? "" : revisionNum;
             */
            // }
            //revisionNum = validateRevisionNumber(revisionNum);
            if (revisionNum != null) {
                String buildEnvironment = getBuildEnvironment();
                revisionNum = buildEnvironment + revisionNum;
            }
          } catch (Exception exception) {
              logDetails = new String[]{exception.toString()};
              PropertiesUtil.appendLog("OT-ER-001", logDetails, true);
          }
          if (revisionNum != null) {
              logDetails = new String[]{revisionNum};
          } else {
              logDetails = null;
          }
          PropertiesUtil.appendLog("OT-IN-003", logDetails, false);
          return revisionNum;
     }

     /**
      * This method checks and validates the revision number.
      * @param revisionNumber String
      * @return validatedBuildNumber else 0 if build number is valid
      */
     /*private String validateRevisionNumber(String revisionNumber) {
          boolean flag = true;
          String response = revisionNumber;
          if (response != null && response.trim().length() > 0) {
               try {
                    Integer.parseInt(response.trim());
               } catch (Exception ex) {
                   String[] logDetails = new String[]{ex.toString()};
                   PropertiesUtil.appendLog("OT-ER-001", logDetails, true);
                    flag = false;
               }
          } else {
               flag = false;
          }
          if (!flag) {
               response = "0";
          }
          return response;
     }*/
     /**
      * This method fetches the build environment.
      * @return build environment based on environment
      */
    private String getBuildEnvironment() {
        String buidEnvironment = null;
        String[] logDetails = null;
        try {
            InputStream inputStream = getServletContext().getResourceAsStream(
                    "/META-INF/MANIFEST.MF");
            Properties properties = new Properties();
            properties.load(inputStream);
            buidEnvironment = properties.getProperty("Build_Environment");
            if (buidEnvironment != null) {
                logDetails = new String[]{buidEnvironment};
            }
            PropertiesUtil.appendLog("OT-IN-004", logDetails, false);
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("OT-ER-001", logDetails, true);
        }
        if (buidEnvironment != null && buidEnvironment.length() > 0) {
            buidEnvironment = buidEnvironment.substring(0, 1);
        }
        return buidEnvironment;
    }
}
