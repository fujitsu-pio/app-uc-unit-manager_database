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
package com.fujitsu.dc.gui.portal.model;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.fujitsu.dc.gui.portal.service.AdministratorManagementServiceTest;
import com.fujitsu.dc.gui.portal.service.ChangePasswordServiceTest;
import com.fujitsu.dc.gui.portal.service.CreateEnvironmentServiceTest;
import com.fujitsu.dc.gui.portal.service.DeleteEnvironmentServiceTest;
import com.fujitsu.dc.gui.portal.service.EditUserInformationServiceTest;
import com.fujitsu.dc.gui.portal.service.LoginServiceTest;
import com.fujitsu.dc.gui.portal.service.RegistrationServiceTest;
import com.fujitsu.dc.gui.portal.service.UnitManagementServiceTest;
import com.fujitsu.dc.gui.portal.service.UserEmailServiceTest;
@RunWith(Suite.class)
@Suite.SuiteClasses({
   EnvironmentTest.class,
   OrganizationTest.class,
   OrgUnitTest.class,
   UnitTest.class,
   UserTest.class,
   MessageTest.class,
   UnitMessageTest.class,
   UserEnvironmentTest.class,
   CreateEnvironmentServiceTest.class,
   LoginServiceTest.class,
   RegistrationServiceTest.class,
   ChangePasswordServiceTest.class,
   EditUserInformationServiceTest.class,
   DeleteEnvironmentServiceTest.class,
   AdministratorManagementServiceTest.class,
   UnitManagementServiceTest.class,
   UserEmailServiceTest.class
})

/**
 * This is a test suite for all Model/Entity classes
*/
public class PCSTestSuite {

}
