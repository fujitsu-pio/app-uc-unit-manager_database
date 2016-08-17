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

import java.util.Date;
import java.util.List;

import com.fujitsu.dc.gui.portal.PCSConstants;
import com.fujitsu.dc.gui.portal.PropertiesUtil;
import com.fujitsu.dc.gui.portal.dao.EditUserInformationDAO;
import com.fujitsu.dc.gui.portal.model.User;
/**
 * EditUserInformation service class for editing user information.
 *
 */
public class EditUserInformationService {
    private EditUserInformationDAO editUserInformationDAO = null;
     /**
      * Constructor for initializing objects.
      */
    public EditUserInformationService() {
        editUserInformationDAO = new EditUserInformationDAO();
    }
    /**
     * The purpose of this method is to update user information.
     * @param userName user name
     * @param newFirstName new first name
     * @param newFamilyName new family name
     * @param newEmail new email
     * @param updatedAt date updated at
     * @return result
     */
    public String updateUserInfo(String userName, String newFirstName,
            String newFamilyName, String newEmail, Date updatedAt) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("EU-IN-001", logDetails, false);
        logDetails = new String[]{userName};
        PropertiesUtil.appendLog("EU-DB-001", new String[]{userName}, false);
        String result = null;
        int rowUpdated = editUserInformationDAO.updateUserDetails(userName,
                newFirstName, newFamilyName, newEmail, updatedAt);
        if (rowUpdated == 1) {
            result = PCSConstants.SUCCESS;
        }
        logDetails = null;
        PropertiesUtil.appendLog("EU-IN-002", logDetails, false);
        logDetails = new String[]{String.valueOf(result)};
        PropertiesUtil.appendLog("EU-DB-002", logDetails, false);
        return result;
    }
    /**
     * Gets user information.
     * @param userName user name
     * @return userInfo
     */
    public List<User> getUserInfo(String userName) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("EU-IN-001", logDetails, false);
        List<User> userInfo = null;
        userInfo = editUserInformationDAO.getUserDetails(userName);
        PropertiesUtil.appendLog("EU-IN-002", logDetails, false);
        return userInfo;
    }
    /**
     * The editUserInformationDAO.
     * @return editUserInformationDAO
     */
    public EditUserInformationDAO getEditUserInformationDAO() {
        return editUserInformationDAO;
    }
    /** Sets editUserInformationDAO.
     * @param editUserInformationDAO EditUserInformationDAO
     */
    public void setEditUserInformationDAO(
            EditUserInformationDAO editUserInformationDAO) {
        this.editUserInformationDAO = editUserInformationDAO;
    }
}
