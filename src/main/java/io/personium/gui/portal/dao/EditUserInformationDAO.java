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

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import io.personium.gui.portal.HibernateUtil;
import io.personium.gui.portal.PropertiesUtil;
import io.personium.gui.portal.model.User;

/**
 * This class is used to edit user information.
 *
 */
public class EditUserInformationDAO {
    /** The purpose of this method is to update the user information into user table.
     * @param name name
     * @param firstName first name
     * @param familyName family name
     * @param email email
     * @param updatedAt date updated at
     * @return result
     */
    public int updateUserDetails(String name, String firstName,
            String familyName, String email, Date updatedAt) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("EU-IN-001", logDetails, false);
        logDetails = new String[]{name};
        PropertiesUtil.appendLog("EU-DB-001", new String[]{name}, false);
        Session session = null;
        Transaction transaction = null;
        int result = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String hql = "UPDATE User set firstName = :firstName, familyName = :familyName,"
                    + " email = :email, updatedAt = :updatedAt "
                    + "WHERE name = :name";
            Query query = session.createQuery(hql);
            query.setParameter("firstName", firstName);
            query.setParameter("familyName", familyName);
            query.setParameter("email", email);
            query.setParameter("updatedAt", updatedAt);
            query.setParameter("name", name);
            result = query.executeUpdate();
            transaction.commit();
            logDetails = new String[]{String.valueOf(result)};
            PropertiesUtil.appendLog("EU-DB-002", logDetails, false);
        } catch (Exception exe) {
            logDetails = new String[]{exe.toString()};
            PropertiesUtil.appendLog("EU-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[]{rtExe.toString()};
                PropertiesUtil.appendLog("EU-ER-002", logDetails, true);
            }
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        logDetails = null;
        PropertiesUtil.appendLog("EU-IN-002", logDetails, false);
        return result;
    }
    /**
     * The purpose of this method is to update the user information into user table.
     * @param name user name
     * @return user information
     */
    public List<User> getUserDetails(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("EU-IN-001", logDetails, false);
        Session session = null;
        List<User> userInfo = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE Name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", name);
            userInfo = selectQuery.list();
        } catch (Exception exe) {
            logDetails = new String[]{exe.toString()};
            PropertiesUtil.appendLog("EU-ER-001", logDetails, true);
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("EU-IN-002", logDetails, false);
        return userInfo;
    }
}
