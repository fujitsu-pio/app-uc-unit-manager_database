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
package io.personium.gui.portal.service;

import java.util.Date;
import java.util.List;

import io.personium.gui.portal.PropertiesUtil;
import io.personium.gui.portal.SHAHashing;
import io.personium.gui.portal.dao.LoginDao;
import io.personium.gui.portal.dao.UserEmailDAO;
import io.personium.gui.portal.model.User;

/**
 *  Email service class for mail process.
 */
public class UserEmailService {
    private UserEmailDAO userEmailDAO = null;
    private LoginDao loginDao = null;
    private SHAHashing shaHashing = null;
    /**
     * Constructor for initializing objects.
     */
    public UserEmailService() {
        userEmailDAO = new UserEmailDAO();
        loginDao = new LoginDao();
    }

    /**
     * This method fetches user information.
     * @param userName userName
     * @return userList
     */
    public List<User> getUserInformation(String userName) {
        String[] logDetails = null;
        logDetails = new String[]{userName};
        PropertiesUtil.appendLog("MF-IN-001", logDetails, false);
        List<User> userList = userEmailDAO.getUserInformation(userName);
        PropertiesUtil.appendLog("MF-IN-002", logDetails, false);
        return userList;
    }

    /**
     * This method fetches subscriber email.
     * @param userName userName
     * @return email
     */
    public String getSubscriberEmail(String userName) {
        String[] logDetails = null;
        logDetails = new String[]{userName};
        PropertiesUtil.appendLog("MF-IN-001", logDetails, false);
        String email = userEmailDAO.getSubscriberEmail(userName);
        logDetails = new String[]{email};
        PropertiesUtil.appendLog("MF-IN-002", logDetails, false);
        return email;
    }

    /**
     * This method updates the verified at time.
     * @param name name
     * @param currentDateTime current date time
     * @return isSuccess
     */
    public boolean updateUserVerifiedAt(String name, Date currentDateTime) {
        String[] logDetails = null;
        logDetails = new String[]{name};
        PropertiesUtil.appendLog("MF-IN-006", logDetails, false);
        boolean isSuccess = false;
        int userID = loginDao.getUserIDByName(name);
        isSuccess = userEmailDAO.updateUserVerifiedAt(userID, currentDateTime);
        logDetails = new String[] {name, Boolean.toString(isSuccess)};
        PropertiesUtil.appendLog("MF-IN-007", logDetails, false);
        return isSuccess;
    }

    /**
     * This method updates the verification send at time.
     * @param name name
     * @param currentDateTime current date time
     * @return isSuccess
     */
    public boolean updateMailVerificationSendAt(String name, Date currentDateTime) {
        String[] logDetails = null;
        logDetails = new String[]{"Updating verification send at for " + name};
        PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
        boolean isSuccess = false;
        int userID = loginDao.getUserIDByName(name);
        isSuccess = userEmailDAO.updateMailVerificationSendAt(userID, currentDateTime);
        logDetails = new String[] {"Update Verification Send At status for "
                + name + "is" + Boolean.toString(isSuccess) };
        PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
        return isSuccess;
    }

    /**
     * This method updates the user status.
     * @param name name
     * @param currentDateTime current date time
     * @param status status
     * @return isSuccess
     */
    public boolean updateUserStatus(String name, Date currentDateTime, String status) {
        String[] logDetails = null;
        logDetails = new String[]{"Updating status for " + name};
        PropertiesUtil.appendLog("MF-IN-004", logDetails, false);
        boolean isSuccess = false;
        int userID = loginDao.getUserIDByName(name);
        isSuccess = userEmailDAO.updateUserStatusToActive(userID, currentDateTime, status);
        logDetails = new String[]{Boolean.toString(isSuccess)};
        PropertiesUtil.appendLog("MF-IN-005", logDetails, false);
        return isSuccess;
    }

    /**
     * This method retrieves organization name.
     * @param orgID orgID
     * @return orgName
     */
    public String getUserOrganizationName(String orgID) {
        String[] logDetails = null;
        logDetails = new String[]{orgID};
        PropertiesUtil.appendLog("MF-IN-001", logDetails, false);
        String orgName = userEmailDAO.getUserOrganizationName(orgID);
        logDetails = new String[]{orgName};
        PropertiesUtil.appendLog("MF-IN-002", logDetails, false);
        return orgName;
    }
    /**
     * This method validates password entered by the user.
     * @param name name
     * @param password password
     * @return true/false
     * @throws Exception exception
     */
    public boolean validatePassword(String name, String password)
            throws Exception {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        boolean isPasswordExist = false;
        String dbUsername = loginDao.getDBUsername(name);
            if (dbUsername != null) {
                String salt = loginDao.getSaltByName(dbUsername);
                shaHashing = new SHAHashing();
                String hashedPassword = shaHashing.validateHashedPassword(salt,
                        password);
                String encryptedPassword = loginDao
                        .getEncryptedPasswordByName(dbUsername);
                if (hashedPassword.equals(encryptedPassword)) {
                    isPasswordExist = true;
                } else {
                    isPasswordExist = false;
                }
            }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return isPasswordExist;
    }
    /**
     * The userEmailDAO.
     * @return userEmailDAO
     */
    public UserEmailDAO getUserEmailDAO() {
        return userEmailDAO;
    }
    /**
     * The userEmailDAO to set.
     * @param userEmailDAO UserEmailDAO
     */
    public void setUserEmailDAO(
            UserEmailDAO userEmailDAO) {
        this.userEmailDAO = userEmailDAO;
    }
    /**
     * Returns loginDao.
     * @return loginDao
     */
    public LoginDao getLoginDAO() {
        return loginDao;
    }
    /**
     * Sets loginDao.
     * @param mockLoginDao mockLoginDao
     */
    public void setLoginDAO(LoginDao mockLoginDao) {
        this.loginDao = mockLoginDao;
    }
}
