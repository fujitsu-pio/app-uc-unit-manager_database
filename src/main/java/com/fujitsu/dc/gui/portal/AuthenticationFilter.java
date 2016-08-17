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
package com.fujitsu.dc.gui.portal;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 *  performs pre-and post-processing of the request before it reaches to the servlet.
 */
public class AuthenticationFilter implements Filter {
     /**
      * Initialization method.
      * @param config FilterConfig
      * @throws ServletException exception
      */
    public void init(FilterConfig config) throws ServletException {
     }
     /**
      * Cleanup method.
      */
     public void destroy() {
     }

     /**
      * Check session value on every page.
      * @param request ServletRequest
      * @param response ServletResponse
      * @param chain FilterChain
      * @exception IOException exception
      * @exception ServletException exception
      */
     public void doFilter(ServletRequest request, ServletResponse response,
               FilterChain chain) throws IOException, ServletException {
          HttpServletResponse res = (HttpServletResponse) response;
          HttpServletRequest req = (HttpServletRequest) request;
          HttpSession session = req.getSession(false);
        String path = req.getContextPath();
          if (session == null || session.getAttribute("id") == null) {
               res.sendRedirect(path);
               return;
          } else {
               res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
               res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
               res.setDateHeader("Expires", 0);
             chain.doFilter(request, response);
          }
     }

}
