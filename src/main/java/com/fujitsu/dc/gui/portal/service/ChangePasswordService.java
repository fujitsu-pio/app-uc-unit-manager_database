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

import com.fujitsu.dc.gui.portal.PCSConstants;
import com.fujitsu.dc.gui.portal.PCSCustomException;
import com.fujitsu.dc.gui.portal.PropertiesUtil;
import com.fujitsu.dc.gui.portal.SHAHashing;
import com.fujitsu.dc.gui.portal.dao.ChangePasswordDAO;
import com.fujitsu.dc.gui.portal.model.User;

/**
 * This is the service layer for Change Password operation.
 */
public class ChangePasswordService {
    private ChangePasswordDAO changePasswordDAO;
    private SHAHashing shaHashing;
    /**
     * Constructor for initializing objects.
     */
    public ChangePasswordService() {
        changePasswordDAO = new ChangePasswordDAO();
        shaHashing = new SHAHashing();
    }
    /**
     * This method is used to validate whether the current password is correct or not.
     * @param userName user name
     * @param password password
     * @return validCurrentPassword
     */
    public boolean validateCurrentPassword(String userName, String password) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CP-IN-001", logDetails, false);
        String salt = null;
        String savedPassword = null;
        boolean validCurrentPassword = false;
        try {
            User user = changePasswordDAO.getUserByUserName(userName);
            salt = user.getSalt();
            savedPassword = user.getEncryptedPassphrase();
            String hashedPassword = shaHashing.validateHashedPassword(salt,
                    password);
            if (savedPassword.equals(hashedPassword)) {
                validCurrentPassword = true;
            } else {
                validCurrentPassword = false;
            }
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("CP-ER-001", logDetails, true);
        }
        PropertiesUtil.appendLog("CP-IN-002", logDetails, false);
        return validCurrentPassword;
    }
    /**
     * This method is used to change the current password.
     * @param userName user name
     * @param password password
     * @param passwordResetAt date password reset at
     * @param updatedAt date password updated at
     * @param salt salt
     * @return true/false
     */
    public boolean changePassword(String userName, String password,
            Date passwordResetAt, Date updatedAt, String salt) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CP-IN-001", logDetails, false);
        boolean result = false;
        try {
            String encryptedPassphrase = shaHashing.createHashedPassword(
                    password, salt).trim();
            encryptedPassphrase = encryptedPassphrase.replaceAll(
                    "^ +| +$|( )+", " ");
            encryptedPassphrase = encryptedPassphrase.replaceAll("\\W", "");
            int defaultLockValue = PCSConstants.DEFUALT_LOCK_STATUS;
            int rowUpdated = changePasswordDAO.changePassword(userName,
                    encryptedPassphrase, salt, passwordResetAt, updatedAt,
                    defaultLockValue);
            if (rowUpdated == 1) {
                result = true;
                logDetails = new String[]{userName};
                PropertiesUtil.appendLog("CP-DB-001", logDetails, false);
            }
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("CP-ER-001", logDetails, true);
        }
        PropertiesUtil.appendLog("CP-IN-002", logDetails, false);
        return result;
    }
    /**
     * This method is used to update password based on validations.
     * @param userName user name
     * @param currentPassword current password
     * @param newPassword new password
     * @param passwordResetAt date password reset at
     * @param updatedAt date password updated at
     * @param salt salt
     * @return result
     * @throws Exception exception
     */
    public String validateAndUpdatePassword(String userName,
            String currentPassword, String newPassword, Date passwordResetAt,
            Date updatedAt, String salt) throws Exception {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CP-IN-001", logDetails, false);
        String result = null;
        try {
            boolean validCurrentPassword = validateCurrentPassword(userName,
                    currentPassword);
            if (validCurrentPassword) {
                boolean passwordUpdated = changePassword(userName, newPassword,
                        passwordResetAt, updatedAt, salt);
                if (passwordUpdated) {
                    result = PCSConstants.SUCCESS;
                } else {
                    throw new PCSCustomException(
                            "Error occurred in Change Password");
                }
            } else {
                result = PCSConstants.INVALID_CURRENT_PWD;
            }
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("CP-ER-001", logDetails, true);
            throw new PCSCustomException(exception.getMessage().toString());
        }
        PropertiesUtil.appendLog("CP-IN-002", logDetails, false);
        return result;
    }
    /**
     * Returns changePasswordDAO.
     * @return changePasswordDAO
     */
    public ChangePasswordDAO getChangePasswordDAO() {
        return changePasswordDAO;
    }
    /**
     * Sets changePasswordDAO.
     * @param changePasswordDAO ChangePasswordDAO
     */
    public void setChangePasswordDAO(ChangePasswordDAO changePasswordDAO) {
        this.changePasswordDAO = changePasswordDAO;
    }
    /**
     * Returns shaHashing.
     * @return shaHashing
     */
    public SHAHashing getShaHashing() {
        return shaHashing;
    }
    /**
     * Sets shaHashing.
     * @param shaHashing SHAHashing
     */
    public void setShaHashing(SHAHashing shaHashing) {
        this.shaHashing = shaHashing;
    }
}
