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

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import io.personium.gui.portal.PropertiesUtil;

/**
 * Implements the logic for logout feature.
 */
public class LogOutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
     /**
      * This method invalidates the session and clears cookie.
      * @param req HttpServletRequest
      * @param resp HttpServletResponse
      * @throws ServletException exception
      * @throws IOException exception
      */
     @Override
     protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String uid = null;
        resp.setHeader("Content-Type", "application/json; charset=UTF-8");
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        Cookie[] cookies = req.getCookies();
        JSONObject responseObject = new JSONObject();
        if (session != null) {
            uid = (String) session.getValue("id");
            session.invalidate();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    cookie.setSecure(true);
                    cookie.setHttpOnly(true);
                    cookie.setValue("-");
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                }
                PrintWriter out = resp.getWriter();
                resp.setContentType("application/json");
                responseObject.put("logout", uid);
                out.print(responseObject);
            }
        }
        responseObject.put("logout", uid);
        if (uid != null) {
            String[] logDetails = new String[]{uid};
            PropertiesUtil.appendLog("LG-IN-004", logDetails, false);
        }
    }
}
