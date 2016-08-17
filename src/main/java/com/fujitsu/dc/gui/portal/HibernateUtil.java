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
package com.fujitsu.dc.gui.portal;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class for configuring Hibernate.
 */
public class HibernateUtil {
    /**
     * This is the constructor.
     */
    private HibernateUtil() {
    }
    private static final SessionFactory SESSIONFACTORY = buildSessionFactory();
    /**
     * It creates Session factory.
     * @return an instance of SessionFactory
     */
    private static SessionFactory buildSessionFactory() {
         if (SESSIONFACTORY != null) {
               return SESSIONFACTORY;
         } else {
             try {
                 // Create the SessionFactory from hibernate.cfg.xml
                   return new Configuration()
                  .configure("hibernate.cfg.xml")
                  .buildSessionFactory();
             } catch (Throwable ex) {
                 String[] logDetails = new String[]{ex.toString()};
                 PropertiesUtil.appendLog("OT-ER-003", logDetails, false);
                 throw new ExceptionInInitializerError(ex);
             }
         }
    }
    /**
     * It return session factory object.
     * @return sessionFactory sessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return SESSIONFACTORY;
    }
}
