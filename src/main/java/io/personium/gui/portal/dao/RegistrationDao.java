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

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import io.personium.gui.portal.HibernateUtil;
import io.personium.gui.portal.PropertiesUtil;
import io.personium.gui.portal.model.Organization;
import io.personium.gui.portal.model.User;
import io.personium.gui.portal.model.UserBean;

/**
 * This class is used for perform database operations during registering a user.
 *
 */
public class RegistrationDao {
    Organization organization = null;
    /**
     * This method perform user registration operation.
     * @param userBean UserBean
     * @return true/false
     * @throws Exception exception
     */
    public boolean insertUserDetails(UserBean userBean) throws Exception {
        String[] logDetails = null;
        PropertiesUtil.appendLog("RG-IN-001", logDetails, false);
        organization = new Organization();
        boolean successUser = false;
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            User user = new User();
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
            organization.setIsActive(userBean.getOrgStatus());
            organization.setName(userBean.getOrganizationName());
            organization.setOrgId(userBean.getOrganizationID());
            organization.getUsers().add(user);
            user.setOrganization(organization);
            session.save(organization);
            transaction.commit();
            successUser = true;
            logDetails = new String[]{String.valueOf(successUser)};
            PropertiesUtil.appendLog("RG-DB-001", logDetails, false);
        } catch (Exception exe) {
            logDetails = new String[]{exe.toString()};
            PropertiesUtil.appendLog("RG-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[]{rtExe.toString()};
                PropertiesUtil.appendLog("RG-ER-002", logDetails, true);
            }
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("RG-IN-002", logDetails, false);
        return successUser;
    }
    /**
     * The purpose of this method is to check user name availability before registration process.
     * @param name name
     * @return true/false
     */
    public boolean checkUserName(String name) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("RG-IN-001", logDetails, false);
        boolean isExist = false;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE Name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", name);
            List<User> lstQuery = selectQuery.list();
            if (lstQuery != null && lstQuery.size() > 0) {
                isExist = true;
            }
        } catch (Exception exe) {
            logDetails = new String[]{exe.toString()};
            PropertiesUtil.appendLog("RG-ER-001", logDetails, true);
            throw exe;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        PropertiesUtil.appendLog("RG-IN-002", logDetails, false);
        return isExist;
    }
}
