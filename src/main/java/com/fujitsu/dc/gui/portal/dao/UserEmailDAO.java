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
package com.fujitsu.dc.gui.portal.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fujitsu.dc.gui.portal.HibernateUtil;
import com.fujitsu.dc.gui.portal.PropertiesUtil;
import com.fujitsu.dc.gui.portal.model.Organization;
import com.fujitsu.dc.gui.portal.model.User;
/**
 * UserEmmailDAO DAO object for Email.
 *
 */
public class UserEmailDAO {
    /**
     * This method retrieves user information on
     * the basis of name.
     * @param name user name
     * @return listUser
     */
    public List<User> getUserInformation(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("MF-IN-001", logDetails, false);
        Session session = null;
        List<User> listUser = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE Name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", name);
            listUser = selectQuery.list();
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("MF-IN-002", logDetails, false);
        return listUser;
    }

    /**
     * This method retrieves subscriber email from database.
     * @param name name
     * @return email
     */
    public String getSubscriberEmail(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("MF-IN-001", logDetails, false);
        Session session = null;
        String email = null;
        List<User> listUser = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE Name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", name);
            //List<User> lstQuery = selectQuery.list();
            listUser = selectQuery.list();
            if (listUser != null && listUser.size() > 0) {
                email = listUser.get(0).getEmail();
            }
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("MF-IN-002", logDetails, false);
        return email;
    }

    /**
     * This method updates the verified at and updated at date of user table.
     * @param userid user id
     * @param currentDateTime current date time
     * @return true/false
     */
    public boolean updateUserVerifiedAt(int userid,
            Date currentDateTime) {
        String[] logDetails = null;
        boolean isSuccess = false;
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            User user = (User) session.get(User.class, userid);
            user.setVerifiedAt(currentDateTime);
            user.setUpdatedAt(currentDateTime);
            session.save(user);
            transaction.commit();
            isSuccess = true;
            logDetails = new String[] {Integer.toString(userid)};
            PropertiesUtil.appendLog("MF-IN-005", logDetails, false);
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[]{rtExe.toString()};
                PropertiesUtil.appendLog("MF-ER-002", logDetails, true);
            }
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return isSuccess;
    }

    /**
     * This method updates the verification send at and updated at date of user table.
     * @param userid user id
     * @param currentDateTime current date time
     * @return true/false
     */
    public boolean updateMailVerificationSendAt(int userid,
            Date currentDateTime) {
        String[] logDetails = null;
        boolean isSuccess = false;
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            User user = (User) session.get(User.class, userid);
            user.setVerifiedAt(null);
            user.setVerificationSendAt(currentDateTime);
            user.setUpdatedAt(currentDateTime);
            session.save(user);
            transaction.commit();
            isSuccess = true;
            logDetails = new String[] {Integer.toString(userid)};
            PropertiesUtil.appendLog("MF-IN-005", logDetails, false);
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[]{rtExe.toString()};
                PropertiesUtil.appendLog("MF-ER-002", logDetails, true);
            }
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return isSuccess;
    }
    /**
     * This method updates the user status.
     * @param userid user id
     * @param currentDateTime current date time
     * @param userStatus userStatus
     * @return true/false
     */
    public boolean updateUserStatusToActive(int userid,
            Date currentDateTime, String userStatus) {
        String[] logDetails = null;
        boolean isSuccess = false;
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            User user = (User) session.get(User.class, userid);
            user.setUpdatedAt(currentDateTime);
            user.setStatus(userStatus);
            session.save(user);
            transaction.commit();
            isSuccess = true;
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[]{rtExe.toString()};
                PropertiesUtil.appendLog("MF-ER-002", logDetails, true);
            }
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return isSuccess;
    }

    /**
     * This method retrieves organization name..
     * @param orgId orgId
     * @return orgName
     */
    public String getUserOrganizationName(String orgId) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("MF-IN-001", logDetails, false);
        Session session = null;
        String orgName = null;
        List<Organization> listUser = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM Organization WHERE orgId = :orgId";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("orgId", orgId);
            //List<User> lstQuery = selectQuery.list();
            listUser = selectQuery.list();
            if (listUser != null && listUser.size() > 0) {
                orgName = listUser.get(0).getName();
            }
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("MF-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("MF-IN-002", logDetails, false);
        return orgName;
    }
}
