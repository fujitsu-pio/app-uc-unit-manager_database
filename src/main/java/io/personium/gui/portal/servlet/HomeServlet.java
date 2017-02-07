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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import io.personium.gui.portal.service.LoginService;

/**
 * This servlet performs operations for landing on home screen.
 */
public class HomeServlet extends HttpServlet {
     private static final long serialVersionUID = 1L;
     private static final int TIME_OUT = 1500;
     /**
      * This is the constructor for HomeServlet calling parent constructor internally.
      */
     public HomeServlet() {
          super();
     }

     /**
      * The purpose of this method is to fetch user privilege information.
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @throws ServletException exception
      * @throws IOException exception
      */
     @Override
     protected void doGet(HttpServletRequest request,
               HttpServletResponse response) throws ServletException, IOException {
               JSONObject responseObject = new JSONObject();
               PrintWriter out = response.getWriter();
               response.setHeader("Content-Type", "application/json; charset=UTF-8");
               response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
               response.setHeader("Pragma", "no-cache");
          try {
               HttpSession session = request.getSession(false);
               response.setContentType("application/json");
               if (session != null) {
                    String userName = (String) session.getAttribute("id");
                    if (userName != null) {
                         LoginService loginService = new LoginService();
                         String privilege = loginService.getUserPrivilege(userName);
                         responseObject.put("privilege", privilege);
                         out.print(responseObject);
                    } else {
                         responseObject.put("privilege", "sessionTimeOut");
                         out.print(responseObject);
                    }
               } else {
                    responseObject.put("privilege", "sessionTimeOut");
                    out.print(responseObject);
               }
          } catch (Exception exception) {
               responseObject.put("privilege", "sessionTimeOut");
               out.print(responseObject);
          }
     }

     @Override
     protected void doPost(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {
          HttpSession session = request.getSession(false);
          JSONObject responseObject = new JSONObject();
          PrintWriter out = response.getWriter();
          response.setHeader("Content-Type", "application/json; charset=UTF-8");
          response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
          response.setHeader("Pragma", "no-cache");
          String unitUrl = request.getParameter("unitUrl");
          String operation = request.getParameter("operation");
          //System.err.println ("Remote host timed out during read operation---->"+  operation);
          if (session != null) {
              String sessionRequestId = (String) session.getAttribute("requestId");
              if (operation.equals("DiscSpace")) {
                  String formValueDiscSpace = request.getParameter("CSRFTokenDiscSpace");
                  if (sessionRequestId.equals(formValueDiscSpace)) {
                   // Create a trust manager that does not validate certificate chains
                      TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                          public X509Certificate[] getAcceptedIssuers() {
                              return null;
                          }
                          public void checkClientTrusted(X509Certificate[] certs, String authType) {
                          }
                          public void checkServerTrusted(X509Certificate[] certs, String authType) {
                          }
                      } };
                      // Install the all-trusting trust manager
                      SSLContext sc = null;
                      try {
                          sc = SSLContext.getInstance("SSL");
                      } catch (NoSuchAlgorithmException e1) {
                          // TODO Auto-generated catch block
                          e1.printStackTrace();
                      }
                      try {
                          if (sc != null) {
                              sc.init(null, trustAllCerts, new java.security.SecureRandom());
                              HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                          }
                      } catch (KeyManagementException e) {
                          // TODO Auto-generated catch block
                          e.printStackTrace();
                      }
                      //HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                      // Create all-trusting host name verifier
                      HostnameVerifier allHostsValid = new HostnameVerifier() {
                          public boolean verify(String hostname, SSLSession session) {
                              return true;
                          }
                      };

                      // Install the all-trusting host verifier
                      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

                      URL url = new URL(unitUrl);
                      URLConnection con = url.openConnection();
                      con.setConnectTimeout(TIME_OUT);
                      con.setReadTimeout(TIME_OUT);
                      final Reader reader = new InputStreamReader(con.getInputStream());
                      final BufferedReader br = new BufferedReader(reader);
                      String line = "";
                      while ((line = br.readLine()) != null) {
                          responseObject.put("diskSpaceResponse", line);
                          out.print(responseObject);
                      }
                      br.close();
                  } else {
                      responseObject.put("sessionTimeOut", "sessionTimeOut");
                      out.print(responseObject);
                  }
             }
             if (operation.equals("APIVersion")) {
                 String formValueAPIVersion = request.getParameter("CSRFTokenAPIVersion");
                 if (sessionRequestId.equals(formValueAPIVersion)) {
                     URL url = new URL(unitUrl);
                     URLConnection con = url.openConnection();
                     con.setConnectTimeout(TIME_OUT);
                     con.setReadTimeout(TIME_OUT);
                     con.setRequestProperty("X-Dc-Version", "0");
                     try {
                         final Reader reader = new InputStreamReader(con.getInputStream());
                         final BufferedReader br = new BufferedReader(reader);
                         String line = "";
                         while ((line = br.readLine()) != null) {
                             responseObject.put("APIResponse", line);
                             out.print(responseObject);
                         }
                         br.close();
                     } catch (InterruptedIOException iioe) {
                         //System.err.println ("Remote host timed out during read operation");
                         responseObject.put("APIResponse", "0");
                         out.print(responseObject);
                     }
                 } else {
                     responseObject.put("sessionTimeOut", "sessionTimeOut");
                     out.print(responseObject);
                 }
             }
         }
    }
}
