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

import java.io.InputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose of this class is to load properties file for logging.
 */
public class PropertiesUtil {
    /**
     * Private Constructor.
     */
     private PropertiesUtil() {
     }
     private static final Properties PROPERTIESLOADER = loadProperties();
     static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

     /**
      * This method loads property file.
      * @return properties Properties
      */
     private static Properties loadProperties() {
              Properties properties = new Properties();
              try {
                   InputStream inputStream = PropertiesUtil.class.getClassLoader()
                             .getResourceAsStream("./application-log.properties");
                   properties.load(inputStream);
              } catch (Throwable ex) {
                  LOGGER.debug("Initial SessionFactory creation failed." + ex);
                  throw new ExceptionInInitializerError(ex);
              }
              return properties;
    }
     /**
      * It fetches properties reference.
      * @return PROPERTIESLOADER Properties
      */
     public static Properties getProperties() {
         return PROPERTIESLOADER;
     }
     /**
      * It gives the current date in specified format yyyy-MM-dd hh:mm:ss.SSS.
      * @return date String
      */
     public static String getDate() {
         Date dNow = new Date();
         SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
         return ft.format(dNow);
     }
     /**
      * This method appends the information in logger based on error code.
      * @param errorCode errorCode
      * @param message message
      * @param exception exception
      */
     public static void appendLog(String errorCode, String[] message, boolean exception) {
         String loggerLevel = errorCode.split("-")[1];
         String errorDetail = "";
         final int messageIndexFourth = 3;
         final int messageIndexFifth = 4;
         final int totalMsgsAllowed = 5;
         if (!exception) {
             errorDetail = getProperties().getProperty(errorCode);
             if (message != null) {
                 int noOfMsgs = message.length;
                 if (noOfMsgs == 1) {
                     if (message[0] != null) {
                         errorDetail = MessageFormat.format(errorDetail, message[0].toString());
                     }
                 } else if (noOfMsgs == 2) {
                      if (message[0] != null && message[1] != null) {
                         errorDetail = MessageFormat.format(errorDetail, message[0].toString(),
                             message[1].toString());
                      }
                 } else if (noOfMsgs == messageIndexFourth) {
                     if (message[0] != null && message[1] != null && message[2] != null) {
                        errorDetail = MessageFormat.format(errorDetail, message[0].toString(),
                            message[1].toString(), message[2].toString());
                     }
                 } else if (noOfMsgs == messageIndexFifth) {
                     if (message[0] != null && message[1] != null && message[2] != null
                             && message[messageIndexFourth] != null) {
                        errorDetail = MessageFormat.format(errorDetail, message[0].toString(),
                            message[1].toString(), message[2].toString(),
                            message[messageIndexFourth].toString());
                     }
                 } else if (noOfMsgs == totalMsgsAllowed && message[0] != null && message[1] != null
                         && message[2] != null && message[messageIndexFourth] != null
                         && message[messageIndexFifth] != null) {
                        errorDetail = MessageFormat.format(errorDetail, message[0].toString(),
                            message[1].toString(), message[2].toString(),
                            message[messageIndexFourth].toString(), message[messageIndexFifth].toString());
                 }
             }
         } else {
              if (message != null) {
                   errorDetail = message[0].toString();
           }
         }
         if (loggerLevel.equalsIgnoreCase(getProperties().getProperty("INFO"))) {
              LOGGER.info(getDate() + " [" + Thread.currentThread().getId() + "] "
                + "[" + getProperties().getProperty("infoLevel") + "] " + PCSConstants.LOG_OUTPUT_CLASS_NAME
                + " [" + errorCode + "] - " + "[" + new Exception().getStackTrace()[1].getClassName() + "#"
                + new Exception().getStackTrace()[1].getMethodName() + ":"
                + new Exception().getStackTrace()[1].getLineNumber() + "] - "
                + errorDetail);
         } else if (loggerLevel.equalsIgnoreCase(getProperties().getProperty("ERROR"))) {
             LOGGER.error(getDate() + " [" + Thread.currentThread().getId() + "] "
                     + "[" + getProperties().getProperty("errorLevel") + "] " + PCSConstants.LOG_OUTPUT_CLASS_NAME
                     + " [" + errorCode + "] - " + "[" + new Exception().getStackTrace()[1].getClassName() + "#"
                     + new Exception().getStackTrace()[1].getMethodName() + ":"
                     + new Exception().getStackTrace()[1].getLineNumber() + "] - "
                     + errorDetail);
         } else if (loggerLevel.equalsIgnoreCase(getProperties().getProperty("WARN"))) {
             LOGGER.warn(getDate() + " [" + Thread.currentThread().getId() + "] "
                     + "[" + getProperties().getProperty("warnLevel") + "] " + PCSConstants.LOG_OUTPUT_CLASS_NAME
                     + " [" + errorCode + "] - " + "[" + new Exception().getStackTrace()[1].getClassName() + "#"
                     + new Exception().getStackTrace()[1].getMethodName() + ":"
                     + new Exception().getStackTrace()[1].getLineNumber() + "] - "
                     + errorDetail);
         } else if (loggerLevel.equalsIgnoreCase(getProperties().getProperty("DEBUG"))) {
             LOGGER.debug(getDate() + " [" + Thread.currentThread().getId() + "] "
                     + "[" + getProperties().getProperty("debugLevel") + "] " + PCSConstants.LOG_OUTPUT_CLASS_NAME
                     + " [" + errorCode + "] - " + "[" + new Exception().getStackTrace()[1].getClassName() + "#"
                     + new Exception().getStackTrace()[1].getMethodName() + ":"
                     + new Exception().getStackTrace()[1].getLineNumber() + "] - "
                     + errorDetail);
         }
     }
}
