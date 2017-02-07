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
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import io.personium.gui.portal.HibernateUtil;
import io.personium.gui.portal.PropertiesUtil;
import io.personium.gui.portal.model.Environment;
import io.personium.gui.portal.model.OrgUnit;
import io.personium.gui.portal.model.Organization;
import io.personium.gui.portal.model.Unit;
import io.personium.gui.portal.model.User;
import io.personium.gui.portal.model.UserEnvironment;
import io.personium.gui.portal.model.UserEnvironmentId;

/**
 * This is the Data Access Layer for performing database operations on Create Environment.
 */
public class CreateEnvironmentDAO {
    /**
     * This method fetches privilege for the current user.
     * @param userName user name to be checked
     * @return privilege
     */
    public String checkUserPrivilege(String userName) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CE-IN-001", logDetails, false);
        String privilege = null;
        SessionFactory factory = null;
        Session session = null;
        try {
            factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", userName);
            List<User> privileges = selectQuery.list();
            if (privileges != null && !privileges.isEmpty()) {
                User user = privileges.get(0);
                privilege = user.getPrivilege();
            }
            PropertiesUtil.appendLog("CE-IN-002", logDetails, false);
            return privilege;
        } catch (Exception exception) {
            logDetails = new String[] {exception.toString() };
            PropertiesUtil.appendLog("CE-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    /**
     * This method fetches user id for the given user name.
     * @param userName user name
     * @return userID userid
     */
    public int fetchUserIDOnName(String userName) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CE-IN-001", logDetails, false);
        int userID = 0;
        SessionFactory factory = null;
        Session session = null;
        try {
            factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            String hql = "FROM User WHERE name = :name";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("name", userName);
            List<User> users = selectQuery.list();
            if (users != null && !users.isEmpty()) {
                User user = users.get(0);
                userID = user.getUserid();
            }
            PropertiesUtil.appendLog("CE-IN-002", logDetails, false);
            return userID;
        } catch (Exception exception) {
            logDetails = new String[] {exception.toString() };
            PropertiesUtil.appendLog("CE-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    /**
     * This method inserts record in Environment and UserEnvironment tables.
     * @param envtName environment name
     * @param orgID organization Id
     * @param unitID Unit Id
     * @param envtID environment Id
     * @param userID userId
     * @return true/false
     */
    public boolean insertEnvironmentRecord(String envtName, String orgID,
            int unitID, String envtID, int userID) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CE-IN-001", logDetails, false);
        boolean result = false;
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Organization organization = (Organization) session.get(
                    Organization.class, orgID);
            Unit unit = (Unit) session.get(Unit.class, unitID);
            Environment environment = new Environment(envtID, envtName,
                    organization, unit);
            UserEnvironment userEnvironment = new UserEnvironment();
            UserEnvironmentId userEnvironmentId = new UserEnvironmentId();
            environment.setEnvId(envtID);
            userEnvironmentId.setEnvironment(environment);
            userEnvironmentId.setUser((User) session.get(User.class, userID));
            userEnvironment.setUserEnvironmentId(userEnvironmentId);
            environment.getUserEnvironments().add(userEnvironment);
            session.save(environment);
            transaction.commit();
            result = true;
            PropertiesUtil.appendLog("CE-IN-002", logDetails, false);
            return result;
        } catch (Exception exception) {
            logDetails = new String[] {exception.toString() };
            PropertiesUtil.appendLog("CE-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[]{rtExe.toString()};
                PropertiesUtil.appendLog("CE-ER-002", logDetails, true);
            }
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    /**
     * This method fetches the list of available unit url for the user.
     * @param orgID organization Id
     * @return list of units
     */
    public List<Unit> fetchUnitURLList(String orgID) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CE-IN-001", logDetails, false);
        List<Unit> unitList = null;
        SessionFactory factory = null;
        Session session = null;
        String hql = null;
        try {
            factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            String hqlQuery = "SELECT unit.unitId FROM Unit as unit, Organization as org, OrgUnit as orgunit"
                    + " WHERE unit.isDeleted = 0 AND org.orgId = :orgID AND "
                    + "org.orgId = orgunit.orgUnitId.organization.orgId"
                    + " AND orgunit.orgUnitId.unit.unitId = unit.unitId";
            Query queryMain = session.createQuery(hqlQuery);
            queryMain.setParameter("orgID", orgID);
            List<Integer> lstUnitIds = queryMain.list();
            Query query = null;
            if (lstUnitIds != null && !lstUnitIds.isEmpty()) {
                hql = "FROM Unit WHERE isDeleted=0 AND type='all' OR isDeleted=0 AND unitId IN (:unitIDs)";
                query = session.createQuery(hql);
                query.setParameterList("unitIDs", lstUnitIds);
            } else {
                hql = "FROM Unit WHERE type='all' AND isDeleted=0";
                query = session.createQuery(hql);
            }
            unitList = query.list();
            PropertiesUtil.appendLog("CE-IN-002", logDetails, false);
            return unitList;
        } catch (Exception exception) {
            logDetails = new String[] {exception.toString() };
            PropertiesUtil.appendLog("CE-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    /**
     * This method fetches the maximum limit of environments that can be created under a unit for the organization.
     * @param orgID organization Id
     * @param unitID unit Id
     * @return max environment limit
     */
    public int fetchMaxEnvironmentLimit(String orgID, int unitID) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CE-IN-001", logDetails, false);
        int noOfMaxEnvt = 0;
        SessionFactory factory = null;
        Session session = null;
        try {
            factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            String hql = "FROM OrgUnit WHERE orgId = :orgID and unitId = :unitID";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("orgID", orgID);
            selectQuery.setParameter("unitID", unitID);
            List<OrgUnit> orgUnits = selectQuery.list();
            if (orgUnits != null && !orgUnits.isEmpty()) {
                OrgUnit orgUnit = orgUnits.get(0);
                noOfMaxEnvt = orgUnit.getMaxEnv();
            }
            PropertiesUtil.appendLog("CE-IN-002", logDetails, false);
            return noOfMaxEnvt;
        } catch (Exception exception) {
            logDetails = new String[] {exception.toString() };
            PropertiesUtil.appendLog("CE-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    /** This method fetches the number of created environments under a unit for the organization.
     * @param orgID organization Id
     * @param unitID unit Id
     * @return number of created environments
     */
    public int fetchNumberOfCreatedEnvt(String orgID, int unitID) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CE-IN-001", logDetails, false);
        int noOfCreatedEnvt = 0;
        SessionFactory factory = null;
        Session session = null;
        try {
            factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            String hql = "FROM Environment WHERE  organizationenv.orgId = :orgID and unit.unitId = :unitID";
            Query selectQuery = session.createQuery(hql);
            selectQuery.setParameter("orgID", orgID);
            selectQuery.setParameter("unitID", unitID);
            List<Environment> envs = selectQuery.list();
            if (envs != null && !envs.isEmpty()) {
                noOfCreatedEnvt = envs.size();
            }
            PropertiesUtil.appendLog("CE-IN-002", logDetails, false);
            return noOfCreatedEnvt;
        } catch (Exception exception) {
            logDetails = new String[] {exception.toString() };
            PropertiesUtil.appendLog("CE-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    /**
     * The of this method is to fetch the type of unit.
     * @param unitID unit Id
     * @return unit type
     */
    public String fetchUnitType(int unitID) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("CE-IN-001", logDetails, false);
        String unitType = null;
        SessionFactory factory = null;
        Session session = null;
        try {
            factory = HibernateUtil.getSessionFactory();
            session = factory.openSession();
            session.beginTransaction();
            Unit unit = (Unit) session.get(Unit.class, unitID);
            if (unit != null) {
                unitType = unit.getType();
            }
            PropertiesUtil.appendLog("CE-IN-002", logDetails, false);
            return unitType;
        } catch (Exception exception) {
            logDetails = new String[] {exception.toString() };
            PropertiesUtil.appendLog("CE-ER-001", logDetails, true);
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
