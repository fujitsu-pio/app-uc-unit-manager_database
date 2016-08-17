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
package com.fujitsu.dc.gui.portal.service;

import java.sql.Date;
import java.util.List;

import com.fujitsu.dc.gui.portal.PCSConstants;
import com.fujitsu.dc.gui.portal.PropertiesUtil;
import com.fujitsu.dc.gui.portal.dao.AdministratorManagementDAO;
import com.fujitsu.dc.gui.portal.model.User;
import com.fujitsu.dc.gui.portal.model.UserBean;

/**
 * AdministratorManagementService service class for Administration management.
 */
public class AdministratorManagementService {
    private AdministratorManagementDAO administratorManagementDAO = null;
    /**
     * Constructor for initializing objects.
     */
    public AdministratorManagementService() {
        administratorManagementDAO = new AdministratorManagementDAO();
    }
    /**
     * createNewUser The purpose of this method is to create new user.
     * @param userBean UserBean
     * @return success/fail
     */
    public boolean createNewUser(UserBean userBean) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("AD-IN-001", logDetails, false);
        boolean success = false;
        success = administratorManagementDAO.insertNewUserDetails(userBean);
        logDetails = new String[] {String.valueOf(success), userBean.getUsername() };
        PropertiesUtil.appendLog("AD-IN-004", logDetails, false);
        return success;
    }

    /**
     * getActiveUsersList The purpose of this method is to retrieve the list of active users.
     * @param status user status
     * @param orgId organization Id
     * @return List of user
     */
    public List<User> getActiveUsersList(final String status, final String orgId) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("AD-IN-001", logDetails, false);
        List<User> activeUsersList = null;
        activeUsersList = administratorManagementDAO.getActiveUsersList(status,
                orgId);
        logDetails = new String[] {String.valueOf(activeUsersList.size()) };
        PropertiesUtil.appendLog("AD-IN-003", logDetails, false);
        return activeUsersList;
    }

    /**
     * updateAdminMgmntUserInfo Update user information.
     * @param userName user name
     * @param newFirstName first name to update
     * @param newFamilyName family name to update
     * @param newEmail email to update
     * @param updatedAt date
     * @param newPrivilege privilege to update
     * @return result
     */
    public String updateAdminMgmntUserInfo(final String userName,
            final String newFirstName, final String newFamilyName,
            final String newEmail, final Date updatedAt,
            final String newPrivilege) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("AD-IN-001", logDetails, false);
        String result = null;
        int rowUpdated = administratorManagementDAO
                .updateAdminMgmntUserDetails(userName, newFirstName,
                        newFamilyName, newEmail, updatedAt, newPrivilege);
        if (rowUpdated == 1) {
            result = PCSConstants.SUCCESS;
            PropertiesUtil.appendLog("AD-IN-006", new String[]{userName}, false);
        }
        PropertiesUtil.appendLog("AD-IN-002", logDetails, false);
        return result;
    }

    /**
     * updatePassword Update password changed from Administrator Management.
     * @param userName user name
     * @param encryptedPass encrypted password
     * @param salt salt
     * @param updatedAt date
     * @param passwordResetAt date
     * @return result
     */
    public boolean updatePassword(final String userName,
            final String encryptedPass, final String salt,
            final Date updatedAt, final Date passwordResetAt) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("AD-IN-001", logDetails, false);
        boolean result = false;
        int rowUpdated = 0;
        int defaultLockStatus = PCSConstants.DEFUALT_LOCK_STATUS;
        rowUpdated = administratorManagementDAO.updatePassword(userName,
                encryptedPass, salt, updatedAt, passwordResetAt,
                defaultLockStatus);
        if (rowUpdated == 1) {
            result = true;
            PropertiesUtil.appendLog("AD-IN-005", new String[]{userName}, false);
        }
        PropertiesUtil.appendLog("AD-IN-002", logDetails, false);
        return result;
    }

    /**
     * deleteUser The purpose of this function is to delete user i.e set status inactive.
     * @param userName user name
     * @param updatedAt date
     * @param status user status
     * @return result
     */
    public boolean deleteUser(final String userName, final Date updatedAt,
            final String status) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("AD-IN-001", logDetails, false);
        boolean result = false;
        int rowUpdated = 0;
        rowUpdated = administratorManagementDAO.setStatusInactive(userName,
                status, updatedAt);
        if (rowUpdated == 1) {
            result = true;
        }
        PropertiesUtil.appendLog("AD-IN-002", logDetails, false);
        return result;
    }

    /**
     * AdministratorManagementDAO Get instance of AdministrationManagementDAO.
     * @return administratorManagementDAO
     */
    public AdministratorManagementDAO getAdministratorManagementDAO() {
        return administratorManagementDAO;
    }

    /**
     * setAdministratorManagementDAO set the DAO.
     * @param administratorManagementDAO the administratorManagementDAO to set
     */
    public void setAdministratorManagementDAO(
            AdministratorManagementDAO administratorManagementDAO) {
        this.administratorManagementDAO = administratorManagementDAO;
    }
}
