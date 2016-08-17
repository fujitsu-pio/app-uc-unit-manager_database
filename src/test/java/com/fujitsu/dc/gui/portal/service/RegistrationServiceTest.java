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
package com.fujitsu.dc.gui.portal.service;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fujitsu.dc.gui.portal.SHAHashing;
import com.fujitsu.dc.gui.portal.dao.RegistrationDao;
import com.fujitsu.dc.gui.portal.model.UserBean;

public class RegistrationServiceTest {
	private RegistrationService registrationService = null;
	private RegistrationDao mockRegistrationDao = null;
	private SHAHashing shahash = null;

	@Before
	public void setUp() throws Exception {
		registrationService = new RegistrationService();
		shahash = new SHAHashing();
		mockRegistrationDao = EasyMock.createStrictMock(RegistrationDao.class);
		registrationService.setRegistrationDao(mockRegistrationDao);
	}

	@After
	public void tearDown() {
		registrationService = null;

	}

	@Test
	public void testRegisterUser() throws Exception {
		boolean successUser = true;
		String id = "test1";
		String passd = "test123";
		String firstName = "test";
		String familyName = "test";
		String orgName = "name";
		String email = "abc@abc.com";
		Date createdAt = new Date(System.currentTimeMillis());
		Date updatedAt = null;
		Date passwordResetAt = null;
		Date lastLoginAt = null;
		Date verificationSendAt = null;
		Date verifiedAt = null;
		String status = "active";
		String randomId = UUID.randomUUID().toString();
		String salt = UUID.randomUUID().toString();
		String orgId = registrationService.generateRandomNumber();
		String encryptPassd = shahash.createHashedPassword(passd, salt).trim();
		//String salt = SHAHashing.salt;
		String privilege = "admin";
		int orgStatus = 0;
		EasyMock.expect(
				mockRegistrationDao.insertUserDetails((UserBean)EasyMock.anyObject())).andReturn(successUser);
		EasyMock.replay(mockRegistrationDao);
		boolean result = registrationService.registerUser(createUserBean(id, encryptPassd, salt,
				firstName, familyName, email, status, privilege, randomId, createdAt, updatedAt,
				verificationSendAt, verifiedAt, lastLoginAt, passwordResetAt, orgStatus, orgName, orgId));
		assertEquals(result, true);
	}

	@Test
	public void testCheckUserStatus() {
		String name = "john";
		boolean isExist = true;
		EasyMock.expect(mockRegistrationDao.checkUserName(name)).andReturn(
				isExist);
		EasyMock.replay(mockRegistrationDao);
		boolean result = registrationService.checkUserStatus(name);
		assertEquals(result, true);
	}
	
	public UserBean createUserBean(String name, String encryptedPassphrase, String salt, String firstName,
			String familyName, String email, String status, String privilege, String randomId,
			Date createdAt, Date updatedAt, Date verificationSendAt, Date verifiedAt, Date lastLoginAt,
			Date passwordResetAt, int orgStatus, String orgName, String orgId) {
		UserBean userBean = new UserBean();
		userBean.setUsername(name);
		userBean.setEncryptedPass(encryptedPassphrase);
		userBean.setSalt(salt);
		userBean.setFirstName(firstName);
		userBean.setFamilyName(familyName);
		userBean.setEmail(email);
		userBean.setStatus(status);
		userBean.setPrivilege(privilege);
		userBean.setRandomId(randomId);
		userBean.setCreatedAt(createdAt);
		userBean.setUpdatedAt(updatedAt);
		userBean.setVerificationSendAt(verificationSendAt);
		userBean.setVerifiedAt(verifiedAt);
		userBean.setLastLoginAt(lastLoginAt);
		userBean.setPasswordResetAt(passwordResetAt);
		userBean.setOrgStatus(orgStatus);
		userBean.setOrganizationName(orgName);
		userBean.setOrganizationID(orgId);
		return userBean;
	}

}
