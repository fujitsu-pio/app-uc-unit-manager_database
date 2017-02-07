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
import io.personium.gui.portal.model.Environment;
import io.personium.gui.portal.model.Organization;
import io.personium.gui.portal.model.User;

/**
 * LoginDAO DAO object for Login.
 *
 */
public class LoginDao {
    /**
     * This method retrieves the salt on the basis of name.
     * @param name user name
     * @return salt
     */
    public String getSaltByName(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        Session session = null;
        String salt = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE Name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", name);
            List<User> lstQuery = selectQuery.list();
            if (lstQuery != null && lstQuery.size() > 0) {
                salt = lstQuery.get(0).getSalt();
            }
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return salt;
    }
    /**
     * This method retrieves encrypted password on the basis of name.
     * @param name user name
     * @return encryptedPassword
     */
    public String getEncryptedPasswordByName(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        Session session = null;
        String encryptedPassword = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE Name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", name);
            List<User> lstQuery = selectQuery.list();
            if (lstQuery != null && lstQuery.size() > 0) {
                encryptedPassword = lstQuery.get(0).getEncryptedPassphrase();
            }
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return encryptedPassword;
    }
    /**
     * This method retrieves the organization status on the basis of name.
     * @param name user name
     * @return organizationStatus
     */
    public int getOrganizationStatus(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        String orgId = getOrganizationID(name);
        Session session = null;
        int organizationStatus = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM Organization WHERE orgId = :orgId";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("orgId", orgId);
            List<Organization> lstQuery = selectQuery.list();
            if (lstQuery != null && lstQuery.size() > 0) {
                organizationStatus = lstQuery.get(0).getIsActive();
            }
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return organizationStatus;
    }
    /**
     *  This method retrieves the organization ID on the basis of name.
     * @param name user name
     * @return organizationID
     */
    public String getOrganizationID(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        Session session = null;
        String organizationID = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", name);
            List<User> lstQuery = selectQuery.list();
            if (lstQuery != null && lstQuery.size() > 0) {
                organizationID = lstQuery.get(0).getOrganization().getOrgId();
            }
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return organizationID;
    }
    /**
     * This method retrieves the user ID on the basis of name.
     * @param name user name
     * @return userID
     */
    public int getUserIDByName(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        int userID = 0;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", name);
            List<User> lstQuery = selectQuery.list();
            if (lstQuery != null && lstQuery.size() > 0) {
                userID = lstQuery.get(0).getUserid();
            }
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return userID;
    }
    /**
     * This method updates the last logged in date and updated at date of user table.
     * @param userid user id
     * @param name name
     * @param currentDateTime current date time
     * @return true/false
     */
    public boolean updateLastLoginAt(int userid, String name,
            Date currentDateTime) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        boolean isSuccess = false;
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            User user = (User) session.get(User.class, userid);
            user.setLastLoginAt(currentDateTime);
            session.save(user);
            transaction.commit();
            isSuccess = true;
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[]{rtExe.toString()};
                PropertiesUtil.appendLog("LG-ER-002", logDetails, true);
            }
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return isSuccess;
    }
    /**
     * This method retrieves the environment list on the basis of organization,
     * ID and unit ID.
     * @param orgID organization id
     * @param unitID unit id
     * @return listEnvironment
     */
    public List<Environment> fetchEnvironmentList(String orgID, int unitID) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        Session session = null;
        List<Environment> listEnvironment = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM Environment as env "
                + "WHERE env.isDeleted = 0 and env.unit.unitId = :unitID and"
                + " env.organizationenv.orgId = :orgID";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("unitID", unitID);
            selectQuery.setParameter("orgID", orgID);
            listEnvironment = selectQuery.list();
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return listEnvironment;
    }
    /**
     * The purpose of this function is to get name from database.
     * @param name user name
     * @return dbUsername
     */
    public String getDBUsername(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        String dbUsername = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE Name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", name);
            List<User> lstQuery = selectQuery.list();
            if (lstQuery != null && lstQuery.size() > 0) {
                dbUsername = lstQuery.get(0).getName();
            } else  if (lstQuery.size() == 0) {
                PropertiesUtil.appendLog("LG-ER-003", logDetails, false);
            }
        } catch (Exception exe) {
            logDetails = new String[]{exe.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return dbUsername;
    }
    /**
     * The purpose of this method is to get lock value from database.
     * @param name user name
     * @return lock
     */
    public int getLockValue(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        int lock = 0;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE Name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", name);
            List<User> lstQuery = selectQuery.list();
            if (lstQuery != null && lstQuery.size() > 0) {
                lock = lstQuery.get(0).getLockStatus();
            }
        } catch (Exception exe) {
            logDetails = new String[]{exe.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return lock;
    }
    /**
     * The purpose of this function is to update lock status.
     * @param name user name
     * @param lockStatus lockStatus
     * @param updatedAt updatedAt
     * @return result
     */
    public int updateLockStatus(String name, int lockStatus, Date updatedAt) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        Session session = null;
        Transaction transaction = null;
        int result = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String hql = "UPDATE User set lockStatus = :lockStatus "
                    + "WHERE name = :name";
            Query query = session.createQuery(hql);
            query.setParameter("lockStatus", lockStatus);
            query.setParameter("name", name);
            result = query.executeUpdate();
            transaction.commit();
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[]{rtExe.toString()};
                PropertiesUtil.appendLog("LG-ER-002", logDetails, true);
            }
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return result;
    }
    /**
     * The purpose of this function is to get status by name.
     * @param name user name
     * @return status
     */
    public String getStatusByName(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        String status = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE Name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", name);
            List<User> lstQuery = selectQuery.list();
            if (lstQuery != null && lstQuery.size() > 0) {
                status = lstQuery.get(0).getStatus();
            }
        } catch (Exception exe) {
            logDetails = new String[]{exe.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return status;
    }
    /**
     * The purpose of this function is to get the expiration date of a message for a unit.
     * @param unitID Unit ID
     * @return List<Date> Expiration Date List of Messages for a Unit
     */
    public List<Date> getNotificationStatusListForUnit(int unitID) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        Session session = null;
        List<Date> messageListForUnit = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "SELECT msg.expiredAt FROM Message msg,UnitMessage umsg WHERE"
                    + " msg.messageId = umsg.unitMessageId.message.messageId AND"
                    + " umsg.unitMessageId.unit.unitId = :unitID";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("unitID", unitID);
            messageListForUnit = selectQuery.list();
        } catch (Exception exe) {
            logDetails = new String[]{exe.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return messageListForUnit;
    }
    /**
     * The purpose of this function is to get the expiration date of a global message.
     * @return List<Date> Expiration Date List of Global Messages
     */
    public List<Date> getNotificationStatusListGlobal() {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        Session session = null;
        List<Date> messageList = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "SELECT msg.expiredAt FROM Message msg WHERE msg.isGlobal = 1";
            Query selectQuery = session.createQuery(hql);
            messageList = selectQuery.list();
        } catch (Exception exe) {
            logDetails = new String[]{exe.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return messageList;
    }
    /**
     * This method updates the terms and condition accepted date of user table.
     * @param userid user id
     * @param name name
     * @param currentDateTime current date time
     * @return true/false
     */
    public boolean updateTermsAndConditionAcceptedAt(int userid, String name,
            Date currentDateTime) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("LG-IN-001", logDetails, false);
        boolean isSuccess = false;
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            User user = (User) session.get(User.class, userid);
            user.setTermsAndConditionAcceptedAt(currentDateTime);
            session.save(user);
            transaction.commit();
            isSuccess = true;
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("LG-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[]{rtExe.toString()};
                PropertiesUtil.appendLog("LG-ER-002", logDetails, true);
            }
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("LG-IN-002", logDetails, false);
        return isSuccess;
    }
}
