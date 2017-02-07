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
package io.personium.gui.portal.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import io.personium.gui.portal.HibernateUtil;
import io.personium.gui.portal.PropertiesUtil;
import io.personium.gui.portal.model.Message;

/**
 * This class is used for unit management.
 *
 */
public class UnitManagementDAO {

    /** The purpose of this method is to get notification message details of
     * specified unit.
     * @param unitID unitID
     * @return allMessageList
     */
    public List<Message> getNotificationMessageDetails(int unitID) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("UM-IN-001", logDetails, false);
        SessionFactory factory = null;
        Session session = null;
        List<Message> messageList = null;
        List<Message> allMessageList = null;
        try {
            factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            String hql1 = "SELECT msg.messageId FROM Message msg,UnitMessage umsg WHERE"
                    + " msg.messageId = umsg.unitMessageId.message.messageId AND"
                    + " umsg.unitMessageId.unit.unitId = :unitID";
            Query selectQueryMessageId = session.createQuery(hql1);
            selectQueryMessageId.setParameter("unitID", unitID);
            List<Integer> messageIdList = selectQueryMessageId.list();
            allMessageList = new ArrayList<Message>();
            if (messageIdList != null && !messageIdList.isEmpty()) {
                for (int i = 0; i < messageIdList.size(); i++) {
                    int messageId = messageIdList.get(i);
                    String hql = "FROM Message WHERE messageId = :messageId";
                    Query selectQuery = session.createQuery(hql);
                    selectQuery.setParameter("messageId", messageId);
                    messageList = selectQuery.list();
                    allMessageList.addAll(messageList);
                }
            }

            String hql2 = "FROM Message WHERE isGlobal = 1";
            Query selectQuery1 = session.createQuery(hql2);
            List<Message> globalmessageList = selectQuery1.list();
            if (globalmessageList != null) {
                allMessageList.addAll(globalmessageList);
            }
            logDetails = null;
            PropertiesUtil.appendLog("UM-IN-002", logDetails, false);
        } catch (Exception exception) {
            logDetails = new String[] {exception.toString() };
            PropertiesUtil.appendLog("UM-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return allMessageList;
    }
    /**
     * The purpose of this method is to update unit name.
     * @param unitId unitId
     * @param unitName unitName
     * @param updatedAt updatedAt
     * @return result
     */
    public int updateUnitName(int unitId, String unitName, Date updatedAt) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("UM-IN-001", logDetails, false);
        logDetails = new String[]{unitName};
        PropertiesUtil.appendLog("UM-DB-001", logDetails, false);
        Session session = null;
        Transaction transaction = null;
        int result = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String hql = "UPDATE Unit set name = :unitName,updateddAt = :updateddAt WHERE unitId = :unitId";
            Query query = session.createQuery(hql);
            query.setParameter("unitName", unitName);
            query.setParameter("unitId", unitId);
            query.setParameter("updateddAt", updatedAt);
            result = query.executeUpdate();
            transaction.commit();
            logDetails = new String[]{String.valueOf(result)};
            PropertiesUtil.appendLog("UM-DB-002", logDetails, false);
            logDetails = null;
            PropertiesUtil.appendLog("UM-IN-002", logDetails, false);
        } catch (Exception exe) {
            logDetails = new String[]{exe.toString()};
            PropertiesUtil.appendLog("UM-ER-002", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[]{rtExe.toString()};
                PropertiesUtil.appendLog("UM-ER-003", logDetails, true);
            }
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

}
