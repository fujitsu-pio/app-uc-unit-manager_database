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

/**
 * This file contains the constants used throughout the java code.
 */
public class PersoniumConstants {
     /**
      * This is the constructor made private for utility class.
      */
      private PersoniumConstants() {
     }
     /**
      * Constant success.
      */
     public static final String SUCCESS = "success";
     /**
      * Constant EXC_MAX_ENVT.
      */
     public static final String EXC_MAX_ENVT = "exceedMaxEnvt";
     /**
      * Constant NOT_ADMIN.
      */
     public static final String NOT_ADMIN = "notAdmin";
     /**
      * Constant ADMIN.
      */
     public static final String ADMIN = "administrator";
     /**
      * Constant SUBSCRIBER.
      */
     public static final String SUBSCRIBER = "subscriber";
     /**
      * Constant INVALID_CURRENT_PWD.
      */
     public static final String INVALID_CURRENT_PWD = "invalidCurrentPassword";
     /**
      * Constant UNIT_TYPE_ALL.
      */
     public static final String UNIT_TYPE_ALL = "all";
     /**
      * Constant DEFAULT_USER_STATUS.
      */
     public static final String DEFAULT_USER_STATUS = "active";
     /**
      * Constant INACTIVE_USER_STATUS.
      */
     public static final String INACTIVE_USER_STATUS = "inactive";
     /**
      * Constant DEFUALT_LOCK_STATUS.
      */
     public static final int DEFUALT_LOCK_STATUS = 0;
     /**
      * Constant MAX_LOCK_STATUS.
      */
     public static final int MAX_LOCK_STATUS = 9999;
     /**
      * Constant LOGIN_INVALID_CREDENTIALS_MSG.
      */
     public static final String LOGIN_INVALID_CREDENTIALS_MSG = "Invalid username or password";
     /**
      * Constant LOGIN_ORG_NOTACTIVATED_MSG.
      */
     public static final String LOGIN_ORG_NOTACTIVATED_MSG = "Your organization is not yet activated";
     /**
      * Constant LOG_OUTPUT_CLASS_NAME.
      */
     public static final String LOG_OUTPUT_CLASS_NAME = "PersoniumUnitMgrLog";
     /**
      * Constant EXCEPTIONCODE_400.
      */
     public static final int EXCEPTIONCODE_401 = 401;
     /**
      * Constant EXCEPTIONCODE_400.
      */
     public static final int EXCEPTIONCODE_400 = 400;
     /**
      * Constant UTF8.
      */
     public static final String UTF8 = "UTF-8";
     /**
      * Constant CONSTMILLISECONDS.
      */
     public static final int CONSTMILLISECONDS = 3600000;
     /**
      * Constant VERIFICATION_MAIL_TEMPLATE_FILE_NAME.
      */
     public static final String VERIFICATION_MAIL_TEMPLATE_FILE_NAME = "template_001.txt";
     /**
      * Constant ACTIVATE_ACCOUNT_MAIL_TEMPLATE_FILE_NAME.
      */
     public static final String ACTIVATE_ACCOUNT_MAIL_TEMPLATE_FILE_NAME = "template_002.txt";
     /**
      * Constant LOGIN_CREDENTIAL_MAIL_TEMPLATE_FILE_NAME.
      */
     public static final String LOGIN_CREDENTIAL_MAIL_TEMPLATE_FILE_NAME = "template_003.txt";
     /**
      * Constant ACCOUNT_LOCKED_MAIL_TEMPLATE_FILE_NAME.
      */
     public static final String ACCOUNT_LOCKED_MAIL_TEMPLATE_FILE_NAME = "template_004.txt";
     /**
      * Constant USERS_DELETED_LOCKED_MAIL_TEMPLATE_FILE_NAME.
      */
     public static final String USERS_DELETED_LOCKED_MAIL_TEMPLATE_FILE_NAME = "template_005.txt";
     /**
      * Constant USERS_DELETED_LOCKED_MAIL_TEMPLATE_FILE_NAME.
      */
     public static final String USERS_FORGET_PASSWORD_MAIL_TEMPLATE_FILE_NAME = "template_006.txt";
     /**
      * Constant CHANGE_PASSWORD_MAIL_TEMPLATE_FILE_NAME.
      */
     public static final String CHANGE_PASSWORD_MAIL_TEMPLATE_FILE_NAME = "template_007.txt";
     /**
      * Constant CHANGE_PASSWORD_BY_SUBSCRIBER_MAIL_TEMPLATE_FILE_NAME.
      */
     public static final String CHANGE_PASSWORD_BY_SUBSCRIBER_MAIL_TEMPLATE_FILE_NAME = "template_008.txt";
     /**
      * Constant EMAIL_NOT_VERIFIED_MSG.
      */
     public static final String EMAIL_NOT_VERIFIED_MSG = "Your email is not yet verified";
}
