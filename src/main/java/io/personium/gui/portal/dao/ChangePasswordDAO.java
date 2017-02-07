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
 * This class is used for perform database operations during change password.
 */
public class ChangePasswordDAO {
    /**
     * This method is used to get the user details for the given user name.
     * @param userName user name
     * @return user
     */
    public User getUserByUserName(String userName) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CP-IN-001", logDetails, false);
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE name = :userName";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("userName", userName);
            List<User> lstQuery = selectQuery.list();
            if (lstQuery != null && lstQuery.size() > 0) {
                user = lstQuery.get(0);
                logDetails = new String[]{user.toString()};
                PropertiesUtil.appendLog("CP-IN-004", logDetails, false);
            }
            return user;
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("CP-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    /**
     * This method is used for updating the password for the current user.
     * @param userName user name
     * @param encryptedPassphrase encrypted password
     * @param salt salt
     * @param passwordResetAt password reset at
     * @param updatedAt date updated at
     * @param lockStatus lock status
     * @return result
     */
    public int changePassword(String userName, String encryptedPassphrase,
            String salt, Date passwordResetAt, Date updatedAt, int lockStatus) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CP-IN-001", logDetails, false);
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String hql = "UPDATE User set encryptedPassphrase = :encryptedPassphrase, "
                    + "lockStatus = :lockStatus,"
                    + " salt = :salt,"
                    + " passwordResetAt = :passwordResetAt,"
                    + " updatedAt = :updatedAt "
                    + "WHERE name = :name";
            Query query = session.createQuery(hql);
            query.setParameter("encryptedPassphrase", encryptedPassphrase);
            query.setParameter("salt", salt);
            query.setParameter("passwordResetAt", passwordResetAt);
            query.setParameter("updatedAt", updatedAt);
            query.setParameter("lockStatus", lockStatus);
            query.setParameter("name", userName);
            int result = query.executeUpdate();
            transaction.commit();
            PropertiesUtil.appendLog("CP-IN-003", new String[]{userName}, false);
            PropertiesUtil.appendLog("CP-IN-002", logDetails, false);
            return result;
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("CP-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[]{rtExe.toString()};
                PropertiesUtil.appendLog("CP-ER-002", logDetails, true);
            }
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
