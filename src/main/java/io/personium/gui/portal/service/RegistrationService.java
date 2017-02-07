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

import java.util.UUID;

import io.personium.gui.portal.PropertiesUtil;
import io.personium.gui.portal.dao.RegistrationDao;
import io.personium.gui.portal.model.UserBean;

/**
 * Registration service class for Registration process.
 *
 */
public class RegistrationService {

    private RegistrationDao registrationDao = null;
    /**
     * Constructor for initializing objects.
     */
    public RegistrationService() {
        registrationDao = new RegistrationDao();
    }
    /**
     * The purpose of this method to perform user registration process.
     * @param userBean UserBean
     * @return success
     * @throws Exception exception
     */
    public boolean registerUser(UserBean userBean) throws Exception {
        String[] logDetails = null;
        PropertiesUtil.appendLog("RG-IN-001", logDetails, false);
        boolean success = false;
        success = registrationDao.insertUserDetails(userBean);
        logDetails = new String[]{String.valueOf(success)};
        PropertiesUtil.appendLog("RG-DB-002", logDetails, false);
        logDetails = new String[]{userBean.getFirstName()};
        PropertiesUtil.appendLog("RG-DB-003", logDetails, false);
        logDetails = new String[]{userBean.getFamilyName()};
        PropertiesUtil.appendLog("RG-DB-004", logDetails, false);
        logDetails = null;
        PropertiesUtil.appendLog("RG-IN-002", logDetails, false);
        return success;
    }
    /**
     * The purpose of this method is to generate 8-digit random number.
     * @return randomNumber
     */
    public String generateRandomNumber() {
        String[] logDetails = null;
        PropertiesUtil.appendLog("RG-IN-001", logDetails, false);
        String randomNumber = UUID.randomUUID().toString();
        final int randomCount = 8;
        randomNumber = randomNumber.substring(0, randomCount);
        logDetails = new String[]{randomNumber};
        PropertiesUtil.appendLog("RG-DB-005", logDetails, false);
        logDetails = null;
        PropertiesUtil.appendLog("RG-IN-002", logDetails, false);
        return randomNumber;
    }
    /**
     * The purpose of this method is to check user name availability before registration process.
     * @param name name
     * @return isExist true/false
     */
    public boolean checkUserStatus(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("RG-IN-001", logDetails, false);
        boolean isExist = false;
        isExist = registrationDao.checkUserName(name);
        logDetails = new String[]{String.valueOf(isExist)};
        PropertiesUtil.appendLog("RG-DB-006", logDetails, false);
        logDetails = null;
        PropertiesUtil.appendLog("RG-IN-002", logDetails, false);
        return isExist;
    }
/**
 * Returns registrationDao.
 * @return registrationDao
 */
    public RegistrationDao getRegistrationDao() {
        return registrationDao;
    }
/**
 * Sets registrationDao.
 * @param registrationDao RegistrationDao
 */
    public void setRegistrationDao(RegistrationDao registrationDao) {
        this.registrationDao = registrationDao;
    }
}
