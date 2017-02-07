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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.personium.gui.portal.PersoniumConstants;
import io.personium.gui.portal.PropertiesUtil;
import io.personium.gui.portal.dao.UnitManagementDAO;
import io.personium.gui.portal.model.Message;

/**
 * UnitManagementService service class is for unit management.
 */
public class UnitManagementService {
    private UnitManagementDAO unitManagementDAO = null;

    /**
     * Constructor for initializing objects.
     */
    public UnitManagementService() {
        unitManagementDAO = new UnitManagementDAO();
    }

    /**
     * This method gets the notification message details.
     * @param unitID unitID
     * @return allMessageList
     */
    public Map<Integer, List<String>> getNotificationMessageDetails(int unitID) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("UM-IN-001", logDetails, false);
        String content = null;
        byte isGlobalValue = 0;
        Date expiredAt = null;
        List<String> messageDetailslist = null;
        Map<Integer, List<String>> messageContentList = new HashMap<Integer, List<String>>();
        List<Message> allMessageList = unitManagementDAO
                .getNotificationMessageDetails(unitID);
        if (allMessageList != null && !allMessageList.isEmpty()) {
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < allMessageList.size(); i++) {
                messageDetailslist = new ArrayList<String>();
                isGlobalValue = allMessageList.get(i).getIsGlobal();
                content = allMessageList.get(i).getContent();
                expiredAt = allMessageList.get(i).getExpiredAt();
                Date currentDate = org.apache.commons.lang.time.DateUtils.truncate(new Date(),
                        Calendar.DAY_OF_MONTH);
                int expiredValue = expiredAt.compareTo(currentDate);
                if (expiredValue >= 0) {
                    String strIsGlobal = String.valueOf(isGlobalValue);
                    String strExpiredAt = formatter.format(expiredAt);
                    messageDetailslist.add(strIsGlobal);
                    messageDetailslist.add(content);
                    messageDetailslist.add(strExpiredAt);
                    messageContentList.put(i, messageDetailslist);
                }
            }
        }
        logDetails = null;
        PropertiesUtil.appendLog("UM-IN-002", logDetails, false);
        return messageContentList;
    }

    /**
     * This method updates the unit name.
     * @param unitId unitId
     * @param unitName unitName
     * @param updatedAt updatedAt
     * @return result
     */
    public String updateUnitName(int unitId, String unitName, Date updatedAt) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("UM-IN-001", logDetails, false);
        logDetails = new String[]{unitName};
        PropertiesUtil.appendLog("UM-DB-001", logDetails, false);
        String result = null;
        int rowUpdated = unitManagementDAO.updateUnitName(unitId, unitName,
                updatedAt);
        if (rowUpdated == 1) {
            result = PersoniumConstants.SUCCESS;
        }
        logDetails = null;
        PropertiesUtil.appendLog("UM-IN-002", logDetails, false);
        logDetails = new String[] {String.valueOf(result)};
        PropertiesUtil.appendLog("UM-DB-002", logDetails, false);
        return result;
    }

    /**
     * The unitManagementDAO.
     * @return unitManagementDAO
     */
    public UnitManagementDAO getUnitManagementDAO() {
        return unitManagementDAO;
    }
    /** Sets unitManagementDAO.
     * @param unitManagementDAO unitManagementDAO
     */
    public void setUnitManagementDAO(UnitManagementDAO unitManagementDAO) {
        this.unitManagementDAO = unitManagementDAO;
    }
}
