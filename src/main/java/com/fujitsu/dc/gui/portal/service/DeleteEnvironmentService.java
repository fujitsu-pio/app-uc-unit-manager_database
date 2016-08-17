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

import com.fujitsu.dc.gui.portal.PCSConstants;
import com.fujitsu.dc.gui.portal.PCSCustomException;
import com.fujitsu.dc.gui.portal.PropertiesUtil;
import com.fujitsu.dc.gui.portal.dao.DeleteEnvironmentDAO;

/**
 * This is the service layer for Delete Environment operation.
 */
public class DeleteEnvironmentService {
    private DeleteEnvironmentDAO deleteEnvironmentDAO = null;
    /**
     * Constructor for initializing objects.
     */
    public DeleteEnvironmentService() {
        deleteEnvironmentDAO = new DeleteEnvironmentDAO();
    }
    /**
     * The purpose of the following method is to delete environment.
     * @param envtID environment ID
     * @return result
     * @throws Exception exception
     */
    public String deleteEnvironment(String envtID) throws Exception {
        String[] logDetails = null;
        PropertiesUtil.appendLog("DE-IN-001", logDetails, false);
        String result = null;
        try {
            String success = null;
            success = deleteEnvironmentDAO.deleteEnvironmentRecord(envtID);
            if (success != null) {
                if (success.equals(PCSConstants.SUCCESS)) {
                    PropertiesUtil.appendLog("DE-IN-003", logDetails, false);
                    result = PCSConstants.SUCCESS;
                    logDetails = new String[]{envtID};
                    PropertiesUtil.appendLog("DE-IN-004", logDetails, false);
                } else {
                    PropertiesUtil.appendLog("DE-WR-001", logDetails, false);
                    throw new PCSCustomException("Error Occurred!");
                }
            }

        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("DE-ER-001", logDetails, true);
            throw new PCSCustomException(exception.getMessage().toString());
        }
        PropertiesUtil.appendLog("DE-IN-002", logDetails, false);
        return result;
    }
    /**
     * Returns deleteEnvironmentDAO.
     * @return deleteEnvironmentDAO
     */
    public DeleteEnvironmentDAO getDeleteEnvironmentDAO() {
        return deleteEnvironmentDAO;
    }
    /**
     * Sets deleteEnvironmentDAO.
     * @param deleteEnvironmentDAO DeleteEnvironmentDAO
     */
    public void setDeleteEnvironmentDAO(
            DeleteEnvironmentDAO deleteEnvironmentDAO) {
        this.deleteEnvironmentDAO = deleteEnvironmentDAO;
    }
}
