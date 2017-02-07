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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.personium.gui.portal.dao.UnitManagementDAO;
import io.personium.gui.portal.model.Message;

public class UnitManagementServiceTest {
    private UnitManagementService unitManagementService = null;
    private UnitManagementDAO     mockUnitManagementDAO = null;

    @Before
    public void setUp() throws Exception {
        unitManagementService = new UnitManagementService();
        mockUnitManagementDAO = EasyMock
                .createStrictMock(UnitManagementDAO.class);
        unitManagementService.setUnitManagementDAO(mockUnitManagementDAO);
    }

    @After
    public void tearDown() {
        unitManagementService = null;
    }

    @Test
    public void testGetNotificationMessageDetails() {
        int unitID = 1;
        byte isGlobal = 0;
        List<Message> messageList = new ArrayList<Message>();
        Message message = new Message();
        message.setContent("Unit down in coming week");
        message.setExpiredAt(new Date());
        message.setIsGlobal(isGlobal);
        messageList.add(message);
        EasyMock.expect(
                mockUnitManagementDAO.getNotificationMessageDetails(unitID))
                .andReturn(messageList);
        EasyMock.replay(mockUnitManagementDAO);
        Map<Integer, List<String>> messageDetail = unitManagementService
                .getNotificationMessageDetails(unitID);
        assertEquals(messageDetail.size(), 1);
    }

    @Test
    public void testUpdateUnitName() {
        int unitId = 1;
        String unitName = "YOKOYAMA";
        Date updatedAt = new Date();
        int result = 1;
        EasyMock.expect(
                mockUnitManagementDAO.updateUnitName(unitId, unitName,
                        updatedAt)).andReturn(result);
        EasyMock.replay(mockUnitManagementDAO);
        String updateStatus = unitManagementService.updateUnitName(unitId,
                unitName, updatedAt);
        assertEquals(updateStatus, "success");
    }

    @Test
    public void testGetUnitManagementDAO() {
        unitManagementService.setUnitManagementDAO(mockUnitManagementDAO);
        UnitManagementDAO expectedValue = mockUnitManagementDAO;
        UnitManagementDAO actualValue = unitManagementService
                .getUnitManagementDAO();
        assertTrue("Correct Value successfully retrieved",
                expectedValue == actualValue);
    }

}
