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

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import io.personium.gui.portal.HibernateUtil;
import io.personium.gui.portal.PersoniumConstants;
import io.personium.gui.portal.PropertiesUtil;

/**
 * This is the Data Access Layer for performing database operations on Delete Environment.
 */

public class DeleteEnvironmentDAO {

     /**
      * The purpose of the following method is to delete envID from UserEnvironment table.
     * @param envID environment id
     * @param session session
     * @return affected rows
     */
    public int deleteUserEnvironment(String envID, Session session) {
          String[] logDetails = null;
          PropertiesUtil.appendLog("DE-IN-001", logDetails, false);
          int rowsAffected = 0;
          try {
               if (session == null) {
                    SessionFactory factory = null;
                    factory = HibernateUtil.getSessionFactory();
                    session = factory.openSession();
                    session.beginTransaction();
               }
               //Delete record from UserEnvironment table.
               String hql = "DELETE FROM UserEnvironment "
                         + "WHERE envid = :envID";
               Query query = session.createQuery(hql);
               query.setParameter("envID", envID);
               rowsAffected = query.executeUpdate();
               logDetails = new String[]{String.valueOf(rowsAffected)};
               PropertiesUtil.appendLog("DE-DB-001", logDetails, false);
               return rowsAffected;
          } catch (Exception exception) {
               logDetails = new String[]{exception.toString()};
               PropertiesUtil.appendLog("DE-ER-001", logDetails, true);
               throw exception;
          }
        }
     /**
      * The purpose of the following method is to delete envID from Environment table.
     * @param envID environment id
     * @param session session object
     * @return affected rows
     */
    public int deleteEnvironment(String envID, Session session) {
          String[] logDetails = null;
          PropertiesUtil.appendLog("DE-IN-001", logDetails, false);
          int rowsAffected = 0;
          try {
            if (session == null) {
                    SessionFactory factory = null;
                    factory = HibernateUtil.getSessionFactory();
                    session = factory.openSession();
                    session.beginTransaction();
               }
               //Delete record from Environment table.
               String hql = "DELETE FROM Environment "
                         + "WHERE envid = :envID";
               Query query = session.createQuery(hql);
               query.setParameter("envID", envID);
               rowsAffected = query.executeUpdate();
               logDetails = new String[]{String.valueOf(rowsAffected)};
               PropertiesUtil.appendLog("DE-DB-001", logDetails, false);
               return rowsAffected;
          } catch (Exception exception) {
               logDetails = new String[]{exception.toString()};
               PropertiesUtil.appendLog("DE-ER-001", logDetails, true);
               throw exception;
          }
     }
     /**
      * The purpose of the following method is to delete envID from both - UserEnvironment and Environment tables.
     * @param envID environment id
     * @return envtDeletedSuccess
     */
    public String deleteEnvironmentRecord(String envID) {
        String[] logDetails = null;
        PropertiesUtil.appendLog("DE-IN-001", logDetails, false);
        Session session = null;
        String envtDeletedSuccess = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            int userEnvironmentDeletedRow = deleteUserEnvironment(envID,
                    session);
            if (userEnvironmentDeletedRow == 1) {
                int environmentDeletedRow = deleteEnvironment(envID, session);
                if (environmentDeletedRow == 1) {
                    transaction.commit();
                    envtDeletedSuccess = PersoniumConstants.SUCCESS;
                }
            }
            PropertiesUtil.appendLog("DE-IN-002", logDetails, false);
            return envtDeletedSuccess;
        } catch (Exception exception) {
            logDetails = new String[]{exception.toString()};
            PropertiesUtil.appendLog("DE-ER-001", logDetails, true);
            try {
                transaction.rollback();
            } catch (RuntimeException rtExe) {
                logDetails = new String[]{rtExe.toString()};
                PropertiesUtil.appendLog("DE-ER-002", logDetails, true);
            }
            throw exception;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
