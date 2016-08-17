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
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fujitsu.dc.gui.portal.PCSCustomException;
import com.fujitsu.dc.gui.portal.PCSConstants;
import com.fujitsu.dc.gui.portal.SHAHashing;
import com.fujitsu.dc.gui.portal.dao.AdministratorManagementDAO;
import com.fujitsu.dc.gui.portal.model.Organization;
import com.fujitsu.dc.gui.portal.model.User;
import com.fujitsu.dc.gui.portal.model.UserBean;

public class AdministratorManagementServiceTest {

	private AdministratorManagementService administratorManagementService = null;
	private AdministratorManagementDAO mockAdministratorManagementDAO = null;
	private SHAHashing shaHashing = null;

	@Before
	public void setUp() throws Exception {
		administratorManagementService = new AdministratorManagementService();
		shaHashing = new SHAHashing();
		mockAdministratorManagementDAO = EasyMock
				.createStrictMock(AdministratorManagementDAO.class);
		administratorManagementService
				.setAdministratorManagementDAO(mockAdministratorManagementDAO);
	}

	@After
	public void tearDown() {
		administratorManagementService = null;

	}

	@Test
	public void testCreateNewUserWithPrivilegeUser() throws PCSCustomException {
		boolean successUser = true;
		String name = "testUser";
		String passd = "test123";
		String firstName = "test";
		String familyName = "test";
		String email = "testUser@abc.com";
		Date createdAt = new Date(System.currentTimeMillis());
		Date updatedAt = null;
		Date passwordResetAt = null;
		Date lastLoginAt = null;
		Date verificationSendAt = null;
		Date verifiedAt = null;
		String status = "active";
		String randomId = UUID.randomUUID().toString();
		String salt = UUID.randomUUID().toString();
		String orgId = "2abdfe34";
		String encryptedPassphrase = shaHashing.createHashedPassword(passd, salt);
		//String salt = SHAHashing.salt;
		String privilege = "user";
		EasyMock.expect(
				mockAdministratorManagementDAO.insertNewUserDetails((UserBean)EasyMock.anyObject())).andReturn(successUser);
		EasyMock.replay(mockAdministratorManagementDAO);
		boolean result = administratorManagementService.createNewUser(createUserBean(name, encryptedPassphrase, salt,
				firstName, familyName, email, status, privilege, randomId, createdAt, updatedAt,
				verificationSendAt, verifiedAt, lastLoginAt, passwordResetAt, orgId));
		assertEquals(result, true);

	}

	@Test
	public void testCreateNewUserWithPrivilegeAdministrator()
			throws PCSCustomException {
		boolean successUser = true;
		String name = "testAdmin";
		String passd = "test123";
		String firstName = "test";
		String familyName = "test";
		String email = "testAdmin@abc.com";
		Date createdAt = new Date(System.currentTimeMillis());
		Date updatedAt = null;
		Date passwordResetAt = null;
		Date lastLoginAt = null;
		Date verificationSendAt = null;
		Date verifiedAt = null;
		String status = PCSConstants.DEFAULT_USER_STATUS;
		String randomId = UUID.randomUUID().toString();
		String orgId = "2abdfe34";
		String salt = UUID.randomUUID().toString();
		String encryptedPassphrase = shaHashing.createHashedPassword(passd, salt);
		//String salt = SHAHashing.salt;
		String privilege = "administrator";
		EasyMock.expect(
				mockAdministratorManagementDAO.insertNewUserDetails((UserBean)EasyMock.anyObject())).andReturn(successUser);
		EasyMock.replay(mockAdministratorManagementDAO);
		boolean result = administratorManagementService.createNewUser(createUserBean(name, encryptedPassphrase,
				salt, firstName, familyName, email, status, privilege, randomId, createdAt, updatedAt,
				verificationSendAt, verifiedAt, lastLoginAt, passwordResetAt, orgId));
		assertEquals(result, true);

	}
	
	
	
	@Test
	public void testGetActiveUsersList() {
		String status = "active";
		String orgId = "5f5299a6";
		List<User> activeUsersList = new ArrayList<User>();
		User user = new User();
		user.setName("test");
		user.setFamilyName("familyName");
		user.setStatus(status);
		Organization organization = new Organization();
		organization.setOrgId(orgId);
		user.setOrganization(organization);
		activeUsersList.add(user);
		EasyMock.expect(mockAdministratorManagementDAO.getActiveUsersList(status, orgId)).andReturn(activeUsersList);
		EasyMock.replay(mockAdministratorManagementDAO);
		List<User> result = administratorManagementService.getActiveUsersList(status, orgId);
		assertEquals(result, activeUsersList);
	}

	@Test
	public void testUpdateAdminMgmntUserInfo() {
		String userName = "testuser";
		String newFirstName = "name";
		String newFamilyName = "technologies";
		String newEmail = "test@exapmle.com";
		Date updatedAt = new Date(System.currentTimeMillis());
		String newPrivilege = "administrator";
		int success = 1;
		EasyMock.expect(mockAdministratorManagementDAO.updateAdminMgmntUserDetails(userName, newFirstName, newFamilyName, newEmail, updatedAt, newPrivilege)).andReturn(success);
		EasyMock.replay(mockAdministratorManagementDAO);
		String result = administratorManagementService.updateAdminMgmntUserInfo(userName, newFirstName, newFamilyName, newEmail, updatedAt, newPrivilege);
		assertEquals(result, "success");
	}

	@Test
	public void testUpdatePassword() {
		String userName ="testuser";
		String encryptedPass = "35360bf0c0dda488da420f911ab739e66a782512";
		String salt = "be4432b2-db10-448a-bb9e-166639a7b42c";
		Date updatedAt = new Date(System.currentTimeMillis());
		Date passwordResetAt = new Date(System.currentTimeMillis());
		int lockStatus = PCSConstants.DEFUALT_LOCK_STATUS;
		int success = 1;
		EasyMock.expect(mockAdministratorManagementDAO.updatePassword(userName, encryptedPass, salt, updatedAt, passwordResetAt, lockStatus)).andReturn(success);
		EasyMock.replay(mockAdministratorManagementDAO);
		boolean result = administratorManagementService.updatePassword(userName, encryptedPass, salt, updatedAt, passwordResetAt);
		assertEquals(result, true);
	}

	@Test
	public void testDeleteUser() {
		String userName = "testuser"; 
		Date updatedAt = new Date(System.currentTimeMillis());
		String status = PCSConstants.INACTIVE_USER_STATUS;
		int success = 1;
		EasyMock.expect(mockAdministratorManagementDAO.setStatusInactive(userName, status, updatedAt)).andReturn(success);
		EasyMock.replay(mockAdministratorManagementDAO);
		boolean result = administratorManagementService.deleteUser(userName, updatedAt, status);
		assertEquals(result, true);
	}

	

	@Test
	public void testAdministratorManagementDAO() throws Exception {
		administratorManagementService
				.setAdministratorManagementDAO(mockAdministratorManagementDAO);
		AdministratorManagementDAO expectedValue = mockAdministratorManagementDAO;
		AdministratorManagementDAO actualValue = administratorManagementService
				.getAdministratorManagementDAO();
		assertTrue("Correct Value successfully retrieved",
				expectedValue == actualValue);
	}
	
	public UserBean createUserBean(String name, String encryptedPassphrase, String salt, String firstName,
			String familyName, String email, String status, String privilege, String randomId,
			Date createdAt, Date updatedAt, Date verificationSendAt, Date verifiedAt, Date lastLoginAt,
			Date passwordResetAt, String orgId) {
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
		userBean.setOrganizationID(orgId);
		return userBean;
	}

}
