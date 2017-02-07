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
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import io.personium.gui.PersoniumEnvironment;
import io.personium.gui.portal.AbstractOAuth2Token;
import io.personium.gui.portal.AbstractOAuth2Token.TokenDsigException;
import io.personium.gui.portal.AbstractOAuth2Token.TokenParseException;
import io.personium.gui.portal.AbstractOAuth2Token.TokenRootCrtException;
import io.personium.gui.portal.CellLocalRefreshToken;
import io.personium.gui.portal.IRefreshToken;
import io.personium.gui.portal.PersoniumConstants;
import io.personium.gui.portal.PropertiesUtil;
import io.personium.gui.portal.Role;
import io.personium.gui.portal.TransCellAccessToken;
import io.personium.gui.portal.service.LoginService;

/**
 * Servlet implementation class tokenGeneration
 */
@WebServlet(description = "This class will be used for creating access token.", urlPatterns = { "/tokenGeneration" })
public class TokenGeneration extends HttpServlet {
     private static final long serialVersionUID = 1L;
     /**
      * This is the constructor for TokenGeneration.
      */
     public TokenGeneration() {
          super();
     }
     /*protected void doGet(HttpServletRequest request,
               HttpServletResponse response) throws ServletException, IOException {}*/
     /**
      * The purpose of the following method is to display error message for bad request.
      * @param response HttpServletResponse
      * @param statusCode int
      * @param message String
      * @param errorCode String
      * @throws IOException exception
      */
     private void displayErrorMessage(HttpServletResponse response,
          int statusCode,String message,String errorCode) throws IOException {
          Map responseObject = new LinkedHashMap();
          Map objMessage = new LinkedHashMap();
          objMessage.put("lang", "en");
          objMessage.put("value", message);
          switch (statusCode) {
          case PersoniumConstants.EXCEPTIONCODE_400:
               response.setStatus(response.SC_BAD_REQUEST);
               response.setContentType("application/json");
               response.setCharacterEncoding(PersoniumConstants.UTF8);
               response.addHeader("Connection", "Keep-alive");
               responseObject.put("code", errorCode);
               responseObject.put("message",objMessage);
               response.getWriter().write( JSONObject.toJSONString(responseObject));
               break;
          case PersoniumConstants.EXCEPTIONCODE_401:
               response.setStatus(response.SC_UNAUTHORIZED);
               response.setContentType("application/json");
               response.setCharacterEncoding(PersoniumConstants.UTF8);
               response.addHeader("Connection", "Keep-alive");
               responseObject.put("code", errorCode);
               responseObject.put("message",objMessage);
               response.getWriter().write(JSONObject.toJSONString(responseObject));
               break;
          }
     }
     /**This method checks if Unit Url(p_target) is valid or not.
      * @param targetURL String
      * @param userName String
      * @return
      */
     public Boolean isUnitURLValid(String targetURL, String userName) {
          return new LoginService().validateUnitURL(targetURL, userName);
     }
     /**
      * This method checks if environment is valid or not.
      * @param targetURL String
      * @param environmentID String
      * @param userName String
      * @return
      */
     public Boolean isEnvironmentValid(String targetURL, String environmentID,
               String userName) {
          return new LoginService().validateEnvironment(targetURL, environmentID,
                    userName);
     }
     /**
      * The purpose of the following method is to get issuer from the property file.
      * @return issuer String
      */
     public String getIssuer() {
          String issuer = PersoniumEnvironment.getInstance().getResourceBundle().getString("issuer");
          return issuer;
     }
     /**
      * The purpose of the following method is to create Token.
      * @param environmentName String
      * @param targetURL String
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @param username String
      */
     public void createToken(String environmentName, String targetURL,
               HttpServletRequest request, HttpServletResponse response,String username) {
          try {
               String issuer = getIssuer();
               String accesstoken;
               String refreshToken;
               int refreshTokenExpiryDuration;
               int accessTokenExpiryDuration;
               accesstoken = getAccessTokenValue(environmentName, targetURL, issuer);
               accessTokenExpiryDuration = parseAccessToken(accesstoken);
               refreshToken = getRefreshToken(environmentName, targetURL,new Date().getTime(),username);
               refreshTokenExpiryDuration =  parseRefreshToken(refreshToken, issuer);
               printAccessToken(accessTokenExpiryDuration, refreshToken, refreshTokenExpiryDuration, accesstoken, response);
           } catch (IOException|GeneralSecurityException e) {
                e.printStackTrace();
           }
     }
     /**
      * This function ensures that irrelevant values are not passed.
      * @param targetURL String
      * @param environmentName String
      * @return true/false
      */
     private boolean isPatterMatches(String targetURL){
         Properties refPropertiesUtil = PropertiesUtil.getProperties();
        String urlPattern = refPropertiesUtil.getProperty("validURLRegex");
         boolean isURLMatches = targetURL.matches(urlPattern);
         if (isURLMatches){
             if (targetURL.length() <= 2083) {
                 return true;
             }
            else {
                return false;
            }
         } else {
             return false;
         }
     }
     /**
      * This method generates the token.
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @throws ServletException exception
      * @throws IOException exception
      */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Properties refPropertiesUtil = PropertiesUtil.getProperties();
        String environmentName = null;
        String grantType = null;
        String refreshToken = null;
        environmentName = request.getParameter("p_env");
        String targetURL = null;
        targetURL = request.getParameter("p_target");
        String userAgent = request.getHeader("User-Agent");
        grantType = request.getParameter("grant_type");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        refreshToken = request.getParameter("refresh_token");
        PropertiesUtil.appendLog("OT-IN-001", new String[]{environmentName}, false);
        if (userAgent.contains("curl") == false) {
          HttpSession session = request.getSession(false);
            if (session != null) {
                String sessionRequestId = (String) session
                        .getAttribute("requestId");
                boolean isTargetURLValid = isPatterMatches(targetURL);
                String formValueDisplayEnvironment = request
                        .getParameter("CSRFTokenDisplayEnvironment");
                if (sessionRequestId != null && !sessionRequestId.equals(formValueDisplayEnvironment)) {
                    displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                            refPropertiesUtil
                                    .getProperty("unAuthenticatedRequest"),
                            refPropertiesUtil
                                    .getProperty("unAuthenticatedRequestCode"));
                    return;
                } else if (!isTargetURLValid) {
                    displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                            refPropertiesUtil
                                    .getProperty("unAuthenticatedRequest"),
                            refPropertiesUtil
                                    .getProperty("unAuthenticatedRequestCode"));
                    return;
                }
            }
        }
            // This validation ensures that the p_target parameter is badly
            // formed or empty.
        if (targetURL == null) {
            displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_400,
                    refPropertiesUtil.getProperty("pTargetMalFormed"),
                    refPropertiesUtil.getProperty("pTargetMalFormedCode"));
            return;
        }
            // when value of p_target is Blank
            else if (targetURL.trim().length() == 0) {
                if (grantType.toLowerCase().equals("refresh_token")) {
                    displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                            refPropertiesUtil.getProperty("pTargetInvalid"),
                            refPropertiesUtil.getProperty("refreshTokenpTargetInvalidCode"));
                    return;
                } else {
                    displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                            refPropertiesUtil.getProperty("pTargetInvalid"),
                            refPropertiesUtil.getProperty("pTargetInvalidCode"));
                    return;
                }
            }
            // when parameter p_env is missing or wrongly formed.
        else if (environmentName == null) {
            displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_400,
                    refPropertiesUtil.getProperty("pEnvironmentMalFormed"),
                    refPropertiesUtil.getProperty("pEnvironmentMalFormedCode"));
            return;
        }
            // when value of p_env is Blank
        else if (environmentName.trim().length() == 0) {
            // when Grant type is Refresh_token, code is varying.401-AU-011
            if (grantType.toLowerCase().equals("refresh_token")) {
                displayErrorMessage(
                        response,
                        PersoniumConstants.EXCEPTIONCODE_401,
                        refPropertiesUtil.getProperty("pEnvironmentInvalid"),
                        refPropertiesUtil
                                .getProperty("refreshTokenInvalidEnvironmentCode"));
            } else {
                displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                        refPropertiesUtil.getProperty("pEnvironmentInvalid"),
                        refPropertiesUtil
                                .getProperty("pEnvironmentInvalidCode"));
                return;
            }
            }
            // Password Authentication.
            else if (grantType != null
                    && grantType.toLowerCase().equals("password")) {
                // This validation is for the scenario when a user provides
                // all five
                // parameters in order to get access token outside the
                // application
                // i.e. through CURL command.
                if (targetURL != null && environmentName != null
                        && grantType != null && username != null
                        && password != null) {
                    handlePasswordAuthentication(targetURL, environmentName,
                            username, password, request, response);
                }
                // When grant type is Refresh Token.
            } else if (grantType != null
                    && grantType.toLowerCase().equals("refresh_token")) {
                if (refreshToken != null && targetURL != null
                        && environmentName != null) {
                    receiveRefresh(targetURL, environmentName,
                            refreshToken, response);
                } else {
                displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_400,
                        refPropertiesUtil.getProperty("grantTypeMalFormed"),
                        refPropertiesUtil.getProperty("grantTypeMalFormedCode"));
                }
                // When grant type is neither password nor refresh. 400-BR-003 401-AU-008
            } else if (grantType != null) {
                if (grantType.toLowerCase() != "password"
                        || grantType.toLowerCase() != "refresh_token") {
                displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                        refPropertiesUtil.getProperty("grantTypeInvalid"),
                        refPropertiesUtil.getProperty("grantTypeInvalidCode"));
                }
            }
            // When call is made through application.
            else if (grantType == null && targetURL != null && environmentName != null) {
                createToken(environmentName, targetURL, request, response, username);
            }
    }
     /**
      * The purpose of this method is to return Token.This method is being used after a user successfully logs in.
      * @param uid String
      * @param baseURL String
      * @param cellRootUrl String
      * @return token value String
      * @throws NoSuchAlgorithmException exception
      * @throws InvalidKeySpecException exception
      * @throws IOException exception
      * @throws GeneralSecurityException exception
      */
     public String getAccessTokenValue(String uid, String baseURL,
               String cellRootUrl) throws NoSuchAlgorithmException,
               InvalidKeySpecException, IOException, GeneralSecurityException {
          TransCellAccessToken.configureX509(null, null, null);
          String target = baseURL;
          String schema = null;
          String subject = uid;
          List<Role> roleList = new ArrayList<Role>();
          TransCellAccessToken tcToken = new TransCellAccessToken(cellRootUrl,
                    subject, target, roleList, schema);
          return tcToken.toTokenString();
     }
     /**
      * The purpose of the following method is to fetch refresh token.
      * @param environment String
      * @param target String
      * @param issuedAt long
      * @param userName String
      * @return refresh token String
      */
     @SuppressWarnings("unchecked")
     public String getRefreshToken(String environment, String target,
               long issuedAt,String userName) {
            final int REFRESH_TOKEN_EXPIRES_HOUR = 24;
            final int MILLISECS_IN_AN_HOUR = 3600000;
            final int lifeSpan = REFRESH_TOKEN_EXPIRES_HOUR * MILLISECS_IN_AN_HOUR;
          String issuer = getIssuer();
          String subject = environment;
          String schema = null;
          @SuppressWarnings("rawtypes")
          CellLocalRefreshToken refToken = new CellLocalRefreshToken(
                    issuedAt, lifeSpan, issuer, subject, schema,userName);
               return refToken.toTokenString();
     }
     /**
      * The purpose of the following method is to parse access token.
      * @param accessToken String
      * @return accessTokenExpiryTime int
      */
     public int parseAccessToken(String accessToken) {
          int accessTokenExpiryTime = 0;
          TransCellAccessToken refTransCellAccessToken;
          try {
               refTransCellAccessToken = TransCellAccessToken.parse(accessToken);
               accessTokenExpiryTime = refTransCellAccessToken.expiresIn();
          } catch (TokenParseException | TokenDsigException
                    | TokenRootCrtException e) {
               e.printStackTrace();
          }
          return accessTokenExpiryTime;
     }
     /**
      * The purpose of the following method is to parse refresh token.
      * @param refreshToken String
      * @param issuer String
      * @return refreshExpiresIn int
      */
     public int parseRefreshToken(String refreshToken, String issuer) {
          int refreshExpiresIn = 0;
          try {
               CellLocalRefreshToken refTransCellRefreshToken = CellLocalRefreshToken
                         .parse(refreshToken, issuer);
               refreshExpiresIn = refTransCellRefreshToken.refreshExpiresIn();
          } catch (TokenParseException e) {
               e.printStackTrace();
          }
          return refreshExpiresIn;
     }
     /**
      * This method is to process refresh token and thereby creating new access and refresh token.
      * @param target String
      * @param environmentID String
      * @param host String
      * @param refreshToken String
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @throws IOException exception
      */
     private void receiveRefresh(final String target, String environmentID,
               final String refreshToken, HttpServletResponse response)
               throws IOException {
         Properties refPropertiesUtil = PropertiesUtil.getProperties();
          try {
               String issuer = getIssuer();
               AbstractOAuth2Token token = AbstractOAuth2Token.parse(refreshToken,
                         issuer, "");
               String userNameFromRefreshToken =  null;
               userNameFromRefreshToken = token.getUserName();
               if (!(token instanceof IRefreshToken)) {
                    displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                            refPropertiesUtil.getProperty("invalidRefreshToken"),
                            refPropertiesUtil.getProperty("invalidRefreshTokenCode"));
               }
               // リフレッシュトークンの有効期限チェック
               if (token.isRefreshExpired()) {
                    displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                            refPropertiesUtil.getProperty("expiredRefreshToken"),
                            refPropertiesUtil.getProperty("expiredRefreshTokenCode"));
                    return;
               }
               //Is p_target URL valid.
               Boolean isUnitURLExist = isUnitURLValid(target, userNameFromRefreshToken);
               //Is p_target URL valid.
               if (isUnitURLExist) {
                    Boolean isEnvironmentExist = isEnvironmentValid(target, environmentID, userNameFromRefreshToken);
                    //Does environment exist.
                    if (isEnvironmentExist == false) {
                    displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                            refPropertiesUtil.getProperty("pEnvironmentInvalid"),
                            refPropertiesUtil.getProperty("invalidEnvironmentRefTokenCode"));
                         return;
                    }
               }
               else {
                    displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                            refPropertiesUtil.getProperty("pTargetInvalid"),
                            refPropertiesUtil.getProperty("invalidTargetRefTokenCode"));
                    return;
               }
               long issuedAt = new Date().getTime();
               // 受け取ったRefresh Tokenから AccessTokenとRefreshTokenを再生成 Regenerate the
               // RefreshToken and AccessToken from Refresh Token received
               String newRefreshToken =null;
               IRefreshToken rToken = (IRefreshToken) token;
               rToken = rToken.refreshRefreshToken(issuedAt);
               newRefreshToken =  rToken.toTokenString();
               try {
                    int newAccessTokenExpiryTime = 0;
                    int newRefreshTokenExpiryTime = 0;
                    String newAccessToken = getAccessTokenValue(environmentID,
                              target, issuer);
                    newAccessTokenExpiryTime = parseAccessToken(newAccessToken);
                    newRefreshTokenExpiryTime = parseRefreshToken(newRefreshToken,
                              issuer);
                    printRefreshToken(newAccessTokenExpiryTime, newRefreshToken, newRefreshTokenExpiryTime, newAccessToken, response);
               } catch (IOException|GeneralSecurityException e) {
                    e.printStackTrace();
               }
          } catch (TokenParseException|TokenDsigException|TokenRootCrtException e) {
               displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                       refPropertiesUtil.getProperty("tokenParserError"),
                       refPropertiesUtil.getProperty("tokenParserCode"));
          }
     }
     /**
      * The purpose of this method is to print refresh token.
      * @param newAccessTokenExpiryTime int
      * @param newRefreshToken String
      * @param newRefreshTokenExpiryTime int
      * @param newAccessToken String
      * @param response HttpServletResponse
      * @throws IOException exception
      */
     private void printRefreshToken(int newAccessTokenExpiryTime,String newRefreshToken,
      int newRefreshTokenExpiryTime,String newAccessToken, HttpServletResponse response) throws IOException {
          response.setContentType("application/json");
          PrintWriter out = response.getWriter();
          JSONObject responseObject = new JSONObject();
          responseObject.put("token_type", "Bearer");
          responseObject.put("expires_in", newAccessTokenExpiryTime);
          responseObject.put("refresh_token", newRefreshToken);
          responseObject.put("refresh_token_expires_in",
                    newRefreshTokenExpiryTime);
          responseObject.put("access_token", newAccessToken);
          out.print(responseObject);
     }
     /**
      * The purpose of this method is to print access token.
      * @param accessTokenExpiryDuration int
      * @param refreshToken String
      * @param refreshTokenExpiryDuration int
      * @param accesstoken String
      * @param response HttpServletResponse
      * @throws IOException exception
      */
     private void printAccessToken(int accessTokenExpiryDuration,String refreshToken,
         int refreshTokenExpiryDuration,String accesstoken,
         HttpServletResponse response) throws IOException{
          response.setContentType("application/json");
          PrintWriter out = response.getWriter();
          JSONObject responseObject = new JSONObject();
          // Start: prepare and send final JSON response object
          responseObject.put("token_type", "Bearer");
          responseObject.put("expires_in",accessTokenExpiryDuration);
          responseObject.put("refresh_token",refreshToken);
          responseObject.put("refresh_token_expires_in", refreshTokenExpiryDuration);
          responseObject.put("access_token", accesstoken);
          out.print(responseObject);
     }
     /**
      * This method performs password authentication.
      * @param targetURL String
      * @param environmentName String
      * @param username String
      * @param password String
      * @param request HttpServletRequest
      * @param response HttpServletResponse
      * @throws IOException exception
      */
     private void handlePasswordAuthentication(String targetURL,
               String environmentName, String username, String password,
               HttpServletRequest request, HttpServletResponse response)
               throws IOException {
          // Should password and user name doesn't match , 401 would be returned
          Boolean isUserExist = isUserValid(username, password);
          Properties refPropertiesUtil = PropertiesUtil.getProperties();
          // Should password and user name doesn't match , 401 would be
          // returned
          if (isUserExist) {
               Boolean isUnitURLExist = isUnitURLValid(targetURL, username);
               // Is p_target URL valid.
               if (isUnitURLExist) {
                    Boolean isEnvironmentExist = isEnvironmentValid(targetURL,
                              environmentName, username);
                    // Does environment exist.
                    if (isEnvironmentExist) {
                         createToken(environmentName, targetURL, request, response,
                                   username);
                    } else {
                    displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                            refPropertiesUtil.getProperty("pEnvironmentInvalid"),
                            refPropertiesUtil.getProperty("pEnvironmentInvalidCode"));
                    }
               } else {
                   displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                           refPropertiesUtil.getProperty("pTargetInvalid"),
                           refPropertiesUtil.getProperty("pTargetInvalidCode"));
               }
          } else {
               displayErrorMessage(response, PersoniumConstants.EXCEPTIONCODE_401,
                       refPropertiesUtil.getProperty("incorrectUserNamePassword"),
                       refPropertiesUtil.getProperty("incorrectUserNamePasswordCode"));
          }
     }
     /**
     * The purpose of the following method is to validate if user is valid or not.
     * @param userName String
     * @param password String
     * @return true/false
     */
     public Boolean isUserValid(String userName, String password) {
          Boolean isUserExist = false;
          int organizationStatus = 0;
          LoginService objLoginService = new LoginService();
          try {
               Date updatedAt = new Date(System.currentTimeMillis());
               isUserExist = objLoginService.validateUsernamePassword(userName,
                         password, updatedAt);
          } catch (Exception exe) {
              exe.printStackTrace();
          }
          // check to ensure if log on is successful ,the organization of the
          // corresponding account should be active.
          if (isUserExist) {
               organizationStatus = objLoginService
                         .checkOrganizationStatus(userName);
               return organizationStatus == 1;
          } else {
               return false;
          }
     }
}
