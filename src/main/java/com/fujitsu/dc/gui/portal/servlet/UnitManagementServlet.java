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
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.fujitsu.dc.gui.portal.PCSConstants;
import com.fujitsu.dc.gui.portal.service.UnitManagementService;

/**
 * This servlet performs operations for unit management screen.
 */
public class UnitManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * This is the constructor for UnitManagementServlet calling parent constructor
     * internally.
     */
    public UnitManagementServlet() {
        super();
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
        String contentTypeHeader = "application/json; charset="
                + PCSConstants.UTF8 + "";
        response.setHeader("Content-Type", contentTypeHeader);
        response.setHeader("Cache-Control",
                "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        JSONObject responseObject = new JSONObject();
        PrintWriter out = response.getWriter();
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        response.setHeader("Cache-Control",
                "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        UnitManagementService unitManagementService = null;
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String userName = (String) session.getAttribute("id");
                String unitID = request.getParameter("unitID");
                if (userName != null) {
                    unitManagementService = new UnitManagementService();
                    Map<Integer, List<String>> arrMessageListJson = unitManagementService
                            .getNotificationMessageDetails(Integer.parseInt(unitID));
                    responseObject
                            .put("arrMessageListJson", arrMessageListJson);
                    response.setContentType("application/json");
                    out.print(responseObject);
                } else {
                    responseObject.put("arrMessageListJson", "sessionTimeOut");
                    out.print(responseObject);
                }
            } else {
                responseObject.put("arrMessageListJson", "sessionTimeOut");
                out.print(responseObject);
            }
        } catch (Exception exception) {
            responseObject.put("arrMessageListJson", "sessionTimeOut");
            out.print(responseObject);
        }
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
        request.setCharacterEncoding(PCSConstants.UTF8);
        response.setHeader("Cache-Control",
                "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        HttpSession session = request.getSession(false);
        JSONObject responseObject = new JSONObject();
        PrintWriter out = response.getWriter();
        String result = null;
        UnitManagementService unitManagementService = null;
        if (session != null) {
            String sessionRequestId = (String) session.getAttribute("requestId");
            String csrfTokenUnitName = request.getParameter("csrfTokenValueUnitName");
            if (sessionRequestId.equals(csrfTokenUnitName)) {
            unitManagementService = new UnitManagementService();
            String unitId = URLDecoder.decode(request.getParameter("unitId"), PCSConstants.UTF8);
            String unitName = URLDecoder.decode(request.getParameter("unitName"), PCSConstants.UTF8);
            unitName = unitName.replaceAll("^ +| +$|( )+", " ");
            //int unitID = Integer.parseInt(unitId);
            Date updatedAt = new Date();
            result = unitManagementService.updateUnitName(Integer.parseInt(unitId), unitName,
                    updatedAt);
            } else {
             result = "sessionTimeOut";
            }
        } else {
            result = "sessionTimeOut";
        }
        responseObject.put("editUnitName", result);
        out.print(responseObject);
    }
}
