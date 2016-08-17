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

import java.sql.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fujitsu.dc.gui.portal.HibernateUtil;
import com.fujitsu.dc.gui.portal.PropertiesUtil;
import com.fujitsu.dc.gui.portal.model.Organization;
import com.fujitsu.dc.gui.portal.model.User;
import com.fujitsu.dc.gui.portal.model.UserBean;

/**
 * AdministartorManagement DAO class for administrator environment.
 */
public class AdministratorManagementDAO {

    /**
     * The purpose of this method is to insert user details in database.
     * @param userBean UserBean
     * @return isInsertSuccess
     */
    public boolean insertNewUserDetails(UserBean userBean) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("AD-IN-001", logDetails, false);
        boolean isInsertSuccess = false;
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            User user = new User();
            Organization organization = (Organization) session.get(
                    Organization.class, userBean.getOrganizationID());
            user.setName(userBean.getUsername());
            user.setEncryptedPassphrase(userBean.getEncryptedPass());
            user.setSalt(userBean.getSalt());
            user.setFirstName(userBean.getFirstName());
            user.setFamilyName(userBean.getFamilyName());
            user.setEmail(userBean.getEmail());
            user.setStatus(userBean.getStatus());
            user.setPrivilege(userBean.getPrivilege());
            user.setRandomId(userBean.getRandomId());
            user.setCreatedAt(userBean.getCreatedAt());
            user.setVerificationSendAt(userBean.getVerificationSendAt());
            user.setVerifiedAt(userBean.getVerifiedAt());
            user.setUpdatedAt(userBean.getUpdatedAt());
            user.setLastLoginAt(userBean.getLastLoginAt());
            user.setPasswordResetAt(userBean.getPasswordResetAt());
            user.setTermsAndConditionAcceptedAt(userBean.getTermsAndConditionAcceptedAt());
            organization.setOrgId(userBean.getOrganizationID());
            user.setOrganization(organization);
            session.save(user);
            transaction.commit();
            isInsertSuccess = true;
        } catch (Exception exe) {
            logDetails = new String[] {exe.toString() };
            PropertiesUtil.appendLog("AD-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[] {rtExe.toString()};
                PropertiesUtil.appendLog("AD-ER-002", logDetails, true);
            }
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("AD-IN-002", logDetails, false);
        return isInsertSuccess;
    }
    /**
     * The purpose of this method is to retrieve list of active users.
     * @param status status
     * @param orgId organization id
     * @return activeUsersList
     */
    public List<User> getActiveUsersList(String status, String orgId) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("AD-IN-001", logDetails, false);
        Session session = null;
        List<User> activeUsersList = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE orgId = :orgId";
            Query selectQuery = session.createQuery(hql);
            //selectQuery.setParameter("status", status);
            selectQuery.setParameter("orgId", orgId);
            activeUsersList = selectQuery.list();
        } catch (Exception exe) {
            logDetails = new String[] {exe.toString() };
            PropertiesUtil.appendLog("AD-ER-001", logDetails, true);
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("AD-IN-002", logDetails, false);
        return activeUsersList;
    }
    /**
      * The purpose of this function is to update user details changed from,
     * Administrator Management.
     * @param name Name
     * @param firstName first name
     * @param familyName family name
     * @param email email
     * @param updatedAt date record updated at
     * @param privilege privilege
     * @return result
     */
    public int updateAdminMgmntUserDetails(String name, String firstName,
            String familyName, String email, Date updatedAt, String privilege) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("AD-IN-001", logDetails, false);
        Transaction transaction = null;
        Session session = null;
        int result = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String hql = "UPDATE User set firstName = :firstName, familyName = :familyName,"
                    + " email = :email, updatedAt = :updatedAt, privilege = :privilege "
                    + "WHERE name = :name";
            Query query = session.createQuery(hql);
            query.setParameter("firstName", firstName);
            query.setParameter("familyName", familyName);
            query.setParameter("email", email);
            query.setParameter("updatedAt", updatedAt);
            query.setParameter("name", name);
            query.setParameter("privilege", privilege);
            result = query.executeUpdate();
            transaction.commit();
            PropertiesUtil.appendLog("AD-DB-002", new String[]{name}, false);
        } catch (Exception exe) {
            logDetails = new String[] {exe.toString() };
            PropertiesUtil.appendLog("AD-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[] {rtExe.toString()};
                PropertiesUtil.appendLog("AD-ER-002", logDetails, true);
            }
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("AD-IN-002", logDetails, false);
        return result;
    }
    /**
     * The purpose of this method is to update password changed from,
     * Administrator Management.
     * @param userName user name
     * @param encryptedPassphrase encrypted password
     * @param salt salt
     * @param updatedAt date password updated on
     * @param passwordResetAt date password is reset
     * @param lockStatus status if account is locked or not
     * @return result
     */
    public int updatePassword(String userName, String encryptedPassphrase,
            String salt, Date updatedAt, Date passwordResetAt, int lockStatus) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("AD-IN-001", logDetails, false);
        Transaction transaction = null;
        Session session = null;
        int result = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String hql = "UPDATE User set encryptedPassphrase = :encryptedPassphrase, salt = :salt,"
                    + " passwordResetAt = :passwordResetAt, updatedAt = :updatedAt, lockStatus = :lockStatus "
                    + "WHERE name = :name";
            Query query = session.createQuery(hql);
            query.setParameter("encryptedPassphrase", encryptedPassphrase);
            query.setParameter("salt", salt);
            query.setParameter("passwordResetAt", passwordResetAt);
            query.setParameter("updatedAt", updatedAt);
            query.setParameter("lockStatus", lockStatus);
            query.setParameter("name", userName);
            result = query.executeUpdate();
            transaction.commit();
            PropertiesUtil.appendLog("AD-DB-001", new String[]{userName}, false);
        } catch (Exception exe) {
            logDetails = new String[] {exe.toString() };
            PropertiesUtil.appendLog("AD-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[] {rtExe.toString()};
                PropertiesUtil.appendLog("AD-ER-002", logDetails, true);
            }
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("AD-IN-002", logDetails, false);
        return result;
    }
    /**
     * The purpose of this method is to set user status inactive.
     * @param name name
     * @param status status
     * @param updatedAt date record updated on
     * @return result
     */
    public int setStatusInactive(String name, String status, Date updatedAt) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("AD-IN-001", logDetails, false);
        Session session = null;
        Transaction transaction = null;
        int result = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String hql = "UPDATE User set status = :status, updatedAt = :updatedAt "
                    + "WHERE name = :name";
            Query query = session.createQuery(hql);
            query.setParameter("status", status);
            query.setParameter("updatedAt", updatedAt);
            query.setParameter("name", name);
            result = query.executeUpdate();
            transaction.commit();
        } catch (Exception exe) {
            logDetails = new String[] {exe.toString() };
            PropertiesUtil.appendLog("AD-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[] {rtExe.toString()};
                PropertiesUtil.appendLog("AD-ER-002", logDetails, true);
            }
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("AD-IN-002", logDetails, false);
        return result;
    }
}
