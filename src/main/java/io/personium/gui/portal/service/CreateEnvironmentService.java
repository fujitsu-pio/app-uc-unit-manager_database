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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.personium.gui.portal.PersoniumConstants;
import io.personium.gui.portal.PersoniumCustomException;
import io.personium.gui.portal.PropertiesUtil;
import io.personium.gui.portal.dao.CreateEnvironmentDAO;
import io.personium.gui.portal.model.Unit;

/**
 * This is the service layer for Create Environment operation.
 */
public class CreateEnvironmentService {
    private CreateEnvironmentDAO createEnvironmentDAO = null;
    /**
     * Constructor for initializing objects.
     */
    public CreateEnvironmentService() {
        createEnvironmentDAO = new CreateEnvironmentDAO();
    }
    /**
     * This method creates the environment for the user in an organization with all validations.
     * @param envtName environment name
     * @param orgID organization id
     * @param unitID unit id
     * @param userName user name
     * @param envtID environment id
     * @return result
     * @throws Exception exception
     */
    public String createEnvironment(String envtName, String orgID, int unitID,
            String userName, String envtID) throws Exception {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CE-IN-001", logDetails, false);
        String result = null;
        try {
            String privilege = createEnvironmentDAO
                    .checkUserPrivilege(userName);
            if (PersoniumConstants.ADMIN.equalsIgnoreCase(privilege)
                    || PersoniumConstants.SUBSCRIBER.equalsIgnoreCase(privilege)) {
                int userID = createEnvironmentDAO.fetchUserIDOnName(userName);
                String unitType = createEnvironmentDAO.fetchUnitType(unitID);
                if (!(PersoniumConstants.UNIT_TYPE_ALL.equalsIgnoreCase(unitType))) {
                    PropertiesUtil.appendLog("CE-DB-001", logDetails, false);
                    int maxEnvtLimit = createEnvironmentDAO
                            .fetchMaxEnvironmentLimit(orgID, unitID);
                    int noOfCreatedEnvt = createEnvironmentDAO
                            .fetchNumberOfCreatedEnvt(orgID, unitID);
                    if (noOfCreatedEnvt < maxEnvtLimit) {
                        boolean success = createEnvironmentDAO
                                .insertEnvironmentRecord(envtName, orgID,
                                        unitID, envtID, userID);
                        if (success) {
                            PropertiesUtil.appendLog("CE-DB-002", logDetails, false);
                            result = PersoniumConstants.SUCCESS;
                            logDetails = new String[] {envtName, userName };
                            PropertiesUtil.appendLog("CE-DB-003", logDetails, false);
                        } else {
                            PropertiesUtil.appendLog("CE-WR-001", logDetails, false);
                            throw new PersoniumCustomException("Error Occurred!");
                        }
                    } else {
                        PropertiesUtil.appendLog("CE-WR-002", logDetails, false);
                        result = PersoniumConstants.EXC_MAX_ENVT;
                    }
                } else {
                    logDetails = new String[] {unitType };
                    PropertiesUtil.appendLog("CE-DB-004", logDetails, false);
                    boolean success = createEnvironmentDAO
                            .insertEnvironmentRecord(envtName, orgID, unitID,
                                    envtID, userID);
                    if (success) {
                        logDetails = null;
                        PropertiesUtil.appendLog("CE-DB-002", logDetails, false);
                        result = PersoniumConstants.SUCCESS;
                    } else {
                        logDetails = null;
                        PropertiesUtil.appendLog("CE-WR-001", logDetails, false);
                        throw new PersoniumCustomException("Error Occurred!");
                    }
                }
            } else {
                result = PersoniumConstants.NOT_ADMIN;
            }
        } catch (Exception exception) {
            logDetails = new String[] {exception.toString() };
            PropertiesUtil.appendLog("CE-ER-001", logDetails, true);
            throw new Exception();
        }
        PropertiesUtil.appendLog("CE-IN-002", logDetails, false);
        return result;
    }
    /**
     * This method fetches the list of available unit urls for the given user.
     * @param orgID organization id
     * @return unitUrlList
     * @throws Exception exception
     */
    public Map<Integer, List<String>> getUnitURLList(String orgID) throws Exception {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CE-IN-001", logDetails, false);
        Map<Integer, List<String>> unitUrlList = null;
        List<String> unitData = null;
        try {
            List<Unit> unitList = createEnvironmentDAO.fetchUnitURLList(orgID);
            if (unitList != null && unitList.size() > 0) {
                int noOfUnits = unitList.size();
                unitUrlList = new HashMap<Integer, List<String>>();
                for (int index = 0; index < noOfUnits; index++) {
                    unitData = new ArrayList<String>();
                    unitData.add(Integer.toString(unitList.get(index).getUnitId()));
                    unitData.add(unitList.get(index).getUrl());
                    unitData.add(unitList.get(index).getName());
                    unitData.add(unitList.get(index).getPlan());
                    //unitData.add(unitList.get(index).getStatus());
                    unitData.add(unitList.get(index).getMail());
                    unitData.add(unitList.get(index).getMailPort());
/*                    unitData.add(((Long) unitList.get(index).getWebdavDisk()).toString());
                    unitData.add(((Long) unitList.get(index).getOdataDisk()).toString());*/
                    unitData.add(unitList.get(index).getType());
                    unitData.add(unitList.get(index).getVsysid());
                    unitData.add(unitList.get(index).getGlobalIP());
                    unitData.add(((Byte) unitList.get(index).getIsAvailable()).toString());
                    unitData.add(unitList.get(index).getDisplayId());
                    unitUrlList.put(unitList.get(index).getUnitId(), unitData);
                }
            }
            PropertiesUtil.appendLog("CE-IN-002", logDetails, false);
            return unitUrlList;
        } catch (Exception exception) {
            logDetails = new String[] {exception.toString() };
            PropertiesUtil.appendLog("CE-ER-001", logDetails, true);
            throw new PersoniumCustomException(exception.getMessage().toString());
        }
    }
    /**
     * Returns createEnvironmentDAO.
     * @return createEnvironmentDAO
     */
    public CreateEnvironmentDAO getCreateEnvironmentDAO() {
        return createEnvironmentDAO;
    }
    /**
     * Sets createEnvironmentDAO.
     * @param createEnvironmentDAO CreateEnvironmentDAO
     */
    public void setCreateEnvironmentDAO(
            CreateEnvironmentDAO createEnvironmentDAO) {
        this.createEnvironmentDAO = createEnvironmentDAO;
    }
}
