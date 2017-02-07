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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.personium.gui.portal.PersoniumConstants;
import io.personium.gui.portal.PropertiesUtil;
import io.personium.gui.portal.SHAHashing;
import io.personium.gui.portal.dao.CreateEnvironmentDAO;
import io.personium.gui.portal.dao.LoginDao;
import io.personium.gui.portal.model.Environment;
import io.personium.gui.portal.model.Unit;

/**
 *  Login service class for Login process.
 */
public class LoginService {
    private LoginDao loginDao = null;
    private CreateEnvironmentDAO createEnvironmentDAO = null;
    private SHAHashing shaHashing = null;
    /**
     * Constructor for initializing objects.
     */
    public LoginService() {
        loginDao = new LoginDao();
        createEnvironmentDAO = new CreateEnvironmentDAO();
    }
    /**
     * This method validates user name and password entered by the user.
     * @param name name
     * @param password password
     * @param updatedAt date password updated at
     * @return true/false
     * @throws Exception exception
     */
    public boolean validateUsernamePassword(String name, String password,
            Date updatedAt) throws Exception {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        boolean isUserExist = false;
        int result = 0;
        String dbUsername = loginDao.getDBUsername(name);
        if (dbUsername != null) {
            int lockValue = loginDao.getLockValue(dbUsername);
            String status = loginDao.getStatusByName(dbUsername);
            if (lockValue >= PersoniumConstants.MAX_LOCK_STATUS) {
                lockValue = lockValue + 1;
                result = loginDao.updateLockStatus(dbUsername,
                        lockValue, updatedAt);
                isUserExist = false;
                PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
                return isUserExist;
            }
            if (lockValue < PersoniumConstants.MAX_LOCK_STATUS
                    && status.equals(PersoniumConstants.DEFAULT_USER_STATUS)) {
                String salt = loginDao.getSaltByName(dbUsername);
                shaHashing = new SHAHashing();
                String hashedPassword = shaHashing.validateHashedPassword(salt,
                        password);
                String encryptedPassword = loginDao
                        .getEncryptedPasswordByName(dbUsername);
                if (hashedPassword.equals(encryptedPassword)) {
                    int defaultLockValue = PersoniumConstants.DEFUALT_LOCK_STATUS;
                    result = loginDao.updateLockStatus(dbUsername,
                            defaultLockValue, updatedAt);
                    if (result == 1) {
                        isUserExist = true;
                    }
                } else {
                    lockValue = lockValue + 1;
                    result = loginDao.updateLockStatus(dbUsername,
                            lockValue, updatedAt);
                    if (result == 1) {
                        isUserExist = false;
                    }
                    PropertiesUtil.appendLog("LG-ER-004", new String[]{dbUsername}, false);
                }
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return isUserExist;
    }
    /**
     * This method checks the organization status of the logged in user.
     * @param name name
     * @return organizationStatus
     */
    public int checkOrganizationStatus(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        int organizationStatus = 0;
        organizationStatus = loginDao.getOrganizationStatus(name);
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return organizationStatus;
    }
    /**
     * This method updated the last logged in column in database.
     * @param name name
     * @param currentDateTime current date time
     * @return isSuccess
     */
    public boolean updateLastLoggedIn(String name, Date currentDateTime) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        boolean isSuccess = false;
        int userID = loginDao.getUserIDByName(name);
        isSuccess = loginDao.updateLastLoginAt(userID, name, currentDateTime);
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return isSuccess;
    }
    /**
     * This method updated the terms and condition accepted column in database.
     * @param name name
     * @param currentDateTime current date time
     * @return isSuccess
     */
    public boolean updateUserTermsAndConditionAcceptedAt(String name, Date currentDateTime) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        boolean isSuccess = false;
        int userID = loginDao.getUserIDByName(name);
        isSuccess = loginDao.updateTermsAndConditionAcceptedAt(userID, name, currentDateTime);
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return isSuccess;
    }
    /**
     * This method retrieves the organization ID on the basis of name.
     * @param name name
     * @return organizationID
     */
    public String getOrganizationID(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        String organizationID = loginDao.getOrganizationID(name);
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return organizationID;
    }
    /**
     * The purpose of the following method is to check whether environment exists or not.
     * @param unitURL unit url
     * @param environmentID environment id
     * @param userName user name
     * @return true/false
     */
    public boolean validateEnvironment(String unitURL, String environmentID,
            String userName) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        String organizationID = getOrganizationID(userName);
        List<Unit> unitList = createEnvironmentDAO
                .fetchUnitURLList(organizationID);
        List<Environment> listEnvironment = null;
        boolean isUnitURLValid = false;
        int noOfUnits = unitList.size();
        for (int index = 0; index < noOfUnits; index++) {
            if (unitList.get(index).getUrl().equals(unitURL)) {
                int unitID = unitList.get(index).getUnitId();
                listEnvironment = loginDao.fetchEnvironmentList(organizationID,
                        unitID);
                if (listEnvironment.size() > 0) {
                    for (int i = 0; i < listEnvironment.size(); i++) {
                        String envId = listEnvironment.get(i).getEnvId();
                        if (envId.toLowerCase().equalsIgnoreCase(environmentID)) {
                            isUnitURLValid = true;
                            return true;
                        }
                    }
                }
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return isUnitURLValid;
    }
    /**
     * The purpose of the following method is to check whether target (Unit Url) exists or not.
     * @param unitURL unit url
     * @param userName user name
     * @return true/false
     */
    public boolean validateUnitURL(String unitURL, String userName) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        String organizationID = getOrganizationID(userName);
        List<Unit> unitList = createEnvironmentDAO
                .fetchUnitURLList(organizationID);
        boolean isUnitURLValid = false;
        if (unitList != null && unitList.size() > 0) {
            int noOfUnits = unitList.size();
            for (int index = 0; index < noOfUnits; index++) {
                if (unitList.get(index).getUrl().equals(unitURL)) {
                    isUnitURLValid = true;
                }
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return isUnitURLValid;
    }
    /**
     * This method returns the environment list of the logged in user.
     * @param orgID organization id
     * @return environmentList
     * @throws Exception exception
     */
    public Map<String, List<List<String>>> getEnvironmentList(String orgID)
            throws Exception {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        Map<String, List<List<String>>> lstUnitEnvt = null;
        List<Environment> listEnvironment = null;
        List<List<String>> envtIDNameList = null;
        try {
            List<Unit> unitList = createEnvironmentDAO.fetchUnitURLList(orgID);
            if (unitList != null && unitList.size() > 0) {
                int noOfUnits = unitList.size();
                lstUnitEnvt = new HashMap<String, List<List<String>>>();

                List<String> envtValue = null;
                for (int index = 0; index < noOfUnits; index++) {
                    envtIDNameList = new ArrayList<List<String>>();
                    int unitID = unitList.get(index).getUnitId();
                    listEnvironment = loginDao.fetchEnvironmentList(orgID,
                            unitID);
                    for (int i = 0; i < listEnvironment.size(); i++) {
                        envtValue = new ArrayList<String>();
                        String envId = listEnvironment.get(i).getEnvId();
                        String envName = listEnvironment.get(i).getName();
                        envtValue.add(envId);
                        envtValue.add(envName);
                        envtIDNameList.add(envtValue);
                    }
                    lstUnitEnvt.put(unitList.get(index).getUrl(),
                            envtIDNameList);
                }
            }
            PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
            return lstUnitEnvt;
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exception;
        }
    }
    /**
     * The purpose of this method is to get name from database.
     * @param name name
     * @return name
     */
    public String getNameFromDB(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        String dbUsername = null;
        dbUsername = loginDao.getDBUsername(name);
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return dbUsername;
    }
    /**
     * This method returns the user privilege on the basis of user name.
     * @param userName user name
     * @return privilege
     */
    public String getUserPrivilege(String userName) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        String privilege = createEnvironmentDAO.checkUserPrivilege(userName);
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return privilege;
    }
    /**
     * This method returns the notification message status for all units.
     * @param unitURLList unitURLList
     * @return notificationStatusList
     */
    public Map<Integer, Boolean> getNotificationStatusList(Map<Integer, List<String>> unitURLList) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        Map<Integer, Boolean> notificationStatusList = new HashMap<Integer, Boolean>();
        boolean globalNotificationExists = false;
        try {
            List<Date> messageList = loginDao.getNotificationStatusListGlobal();
            if (messageList != null) {
                int noOfMessages = messageList.size();
                for (int index = 0; index < noOfMessages; index++) {
                    Date currentDate = org.apache.commons.lang.time.DateUtils.truncate(new Date(),
                            Calendar.DAY_OF_MONTH);
                    Date expiryDate = messageList.get(index);
                    int expiredValue = expiryDate.compareTo(currentDate);
                    if (expiredValue >= 0) {
                        PropertiesUtil.appendLog("LG-DB-001", logDetails, false);
                        globalNotificationExists = true;
                        for (int unitID : unitURLList.keySet()) {
                            notificationStatusList.put(unitID, true);
                        }
                        break;
                    }
                }
            }
            if (!globalNotificationExists && unitURLList != null) {
                for (int unitID : unitURLList.keySet()) {
                    List<Date> messageListForUnit = loginDao.getNotificationStatusListForUnit(unitID);
                    if (messageListForUnit != null) {
                        int noOfMessages = messageListForUnit.size();
                        for (int index = 0; index < noOfMessages; index++) {
                             Date currentDate = org.apache.commons.lang.time.DateUtils.truncate(new Date(),
                                    Calendar.DAY_OF_MONTH);
                             Date expiryDate = messageListForUnit.get(index);
                             int expiredValue = expiryDate.compareTo(currentDate);
                             if (expiredValue >= 0) {
                                notificationStatusList.put(unitID, true);
                                break;
                            }
                        }
                        if (notificationStatusList.get(unitID) == null) {
                            notificationStatusList.put(unitID, false);
                        }
                        logDetails = new String[]{Integer.toString(unitID),
                                notificationStatusList.get(unitID).toString()};
                        PropertiesUtil.appendLog("LG-DB-002", logDetails, false);
                    }
                }
            }
            PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exception;
        }
        return notificationStatusList;
    }
    /**
     * The loginDao.
     * @return loginDao
     */
    public LoginDao getLoginDao() {
        return loginDao;
    }
    /**
     * The loginDao to set.
     * @param loginDao the loginDao to set
     */
    public void setLoginDao(LoginDao loginDao) {
        this.loginDao = loginDao;
    }
    /**
     * The shaHashing.
     * @return shaHashing
     */
    public SHAHashing getShaHashing() {
        return shaHashing;
    }
    /**
     * The shaHashing to set.
     * @param shaHashing SHAHashing
     */
    public void setShaHashing(SHAHashing shaHashing) {
        this.shaHashing = shaHashing;
    }
    /**
     * The createEnvironmentDAO.
     * @return createEnvironmentDAO
     */
    public CreateEnvironmentDAO getCreateEnvironmentDAO() {
        return createEnvironmentDAO;
    }
    /**
     *  The createEnvironmentDAO to set.
     * @param createEnvironmentDAO CreateEnvironmentDAO
     */
    public void setCreateEnvironmentDAO(
            CreateEnvironmentDAO createEnvironmentDAO) {
        this.createEnvironmentDAO = createEnvironmentDAO;
    }
}
