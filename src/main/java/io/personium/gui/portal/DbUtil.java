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
package io.personium.gui.portal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The purpose of this class is to establish a connection to a database.
 */
public class DbUtil {
    /**
     * Private Constructor.
     */
     private DbUtil() {
     }
     private static Connection connection = null;

     /**
      * The purpose of this method is to establish connection. It reads the
      * properties file for Database related information. It uses a username,
      * password, driver and a JDBC url to establish a connection to the
      * database.
      * @return connection Connection object.
      * @throws PersoniumCustomException exception
      */
     public static Connection getConnection() throws PersoniumCustomException {
          if (connection != null) {
               return connection;
          } else {
               try {
                    Properties properties = new Properties();
                    InputStream inputStream = DbUtil.class.getClassLoader()
                              .getResourceAsStream("./environment.properties");
                    properties.load(inputStream);
                    String driver = properties.getProperty("driver");
                    String url = properties.getProperty("url");
                    String user = properties.getProperty("user");
                    String password = properties.getProperty("password");
                    // Load the JDBC driver
                    Class.forName(driver);
                    // Create a connection to the database
                    connection = DriverManager.getConnection(url, user, password);
               } catch (ClassNotFoundException exe) {
                    exe.printStackTrace();
                    throw new PersoniumCustomException(
                              "Error: Unable to load driver class.");
               } catch (SQLException exe) {
                    exe.printStackTrace();
                    throw new PersoniumCustomException(
                              "Error: Could not connect to the database.");
               } catch (FileNotFoundException exe) {
                    exe.printStackTrace();
                    throw new PersoniumCustomException("FileNotFoundException" + exe);
               } catch (IOException exe) {
                    exe.printStackTrace();
                    throw new PersoniumCustomException("PCSCustomException" + exe);
               } catch (RuntimeException exe) {
                    exe.printStackTrace();
                    throw new PersoniumCustomException("RuntimeException" + exe);
               }
               return connection;
          }
     }

     /**
      * close connection.
      */
     public static void closeAndCleanConnection() {
          try {
               connection.close();
          } catch (SQLException e) {
               e.printStackTrace();
          }
          connection = null;
     }
}
