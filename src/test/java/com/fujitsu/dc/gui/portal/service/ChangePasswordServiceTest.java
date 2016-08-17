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
 /**
 * Test cases for Change Password functionality
 */
package com.fujitsu.dc.gui.portal.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fujitsu.dc.gui.portal.PCSConstants;
import com.fujitsu.dc.gui.portal.SHAHashing;
import com.fujitsu.dc.gui.portal.dao.ChangePasswordDAO;
import com.fujitsu.dc.gui.portal.dao.LoginDao;
import com.fujitsu.dc.gui.portal.model.User;

public class ChangePasswordServiceTest {

	private ChangePasswordService changePasswordService;
	private ChangePasswordDAO mockChangePasswordDAO;
	private SHAHashing mockSHAHashing;
	private LoginDao mockLoginDao;
	private LoginService loginService;
	
	
	/**
	 * set up pre-requisite to run private method
	 * */
	@Before
	public void setUp() throws Exception{
		changePasswordService = new ChangePasswordService();
		loginService = new LoginService();
		mockChangePasswordDAO = EasyMock
				.createStrictMock(ChangePasswordDAO.class);
		changePasswordService.setChangePasswordDAO(mockChangePasswordDAO);
		
		mockSHAHashing = EasyMock
				.createStrictMock(SHAHashing.class);
		changePasswordService.setShaHashing(mockSHAHashing);
		
		mockLoginDao = EasyMock
				.createStrictMock(LoginDao.class);
		loginService.setLoginDao(mockLoginDao);
	}

	@After
	public void tearDown() {
		changePasswordService =null;
	}
	
	private User getUser(){
		User user = new User();
		user.setSalt("d9ad9e1d-c12f-4a19-898b-eb180c3523c6");
		user.setEncryptedPassphrase("cf199a1c977ed20165ca21aa7ac222ea2b43fa1d");
		user.setUpdatedAt(new Date());
		user.setPasswordResetAt(new Date());
		return user;
	}
	
	@Test
	public void testValidateAndUpdatePasswordWhenSuccess() throws Exception{
		String userName = "john";
		String currentPassword = "test123";
		String newPassword = "test123";
		String result = null;
		String encryptedPassphrase = "cf199a1c977ed20165ca21aa7ac222ea2b43fa1d";
		String salt = "d9ad9e1d-c12f-4a19-898b-eb180c3523c6";
		String hashedPassword = "cf199a1c977ed20165ca21aa7ac222ea2b43fa1d";
		//String newSalt = "";
		String testSalt = UUID.randomUUID().toString();
		Date passwordResetAt = new Date();
		Date updatedAt = new Date();
		EasyMock.expect(
				mockChangePasswordDAO.getUserByUserName(userName))
				.andReturn(getUser());
		EasyMock.expect(
				mockSHAHashing.validateHashedPassword(salt, currentPassword))
				.andReturn(hashedPassword);
		EasyMock.expect(
				mockSHAHashing.createHashedPassword(newPassword, testSalt))
				.andReturn(encryptedPassphrase);
		//mockSHAHashing.salt = "";
		EasyMock.expect(
				mockChangePasswordDAO.changePassword(userName, encryptedPassphrase, testSalt, passwordResetAt, updatedAt, PCSConstants.DEFUALT_LOCK_STATUS))
				.andReturn(1);
		EasyMock.replay(mockSHAHashing);
		EasyMock.replay(mockChangePasswordDAO);
		result = changePasswordService.validateAndUpdatePassword(userName, currentPassword, newPassword, passwordResetAt, updatedAt, testSalt);
		assertEquals("success", result);
	}
	
	@Test
	public void testValidateAndUpdatePasswordWhenInvalidCurrentPassword() throws Exception{
		String userName = "john";
		String currentPassword = "test123";
		String newPassword = "test123";
		String result = null;
		String salt = "d9ad9e1d-c12f-4a19-898b-eb180c3523c6";
		String hashedPassword = "cf199a1c967ed20165ca21aa7ac222ea2b43fa1d";
		String testSalt = UUID.randomUUID().toString();
		Date passwordResetAt = new Date();
		Date updatedAt = new Date();
		EasyMock.expect(
				mockChangePasswordDAO.getUserByUserName(userName))
				.andReturn(getUser());
		EasyMock.expect(
				mockSHAHashing.validateHashedPassword(salt, currentPassword))
				.andReturn(hashedPassword);
		EasyMock.replay(mockSHAHashing);
		EasyMock.replay(mockChangePasswordDAO);
		result = changePasswordService.validateAndUpdatePassword(userName, currentPassword, newPassword, passwordResetAt, updatedAt, testSalt);
		assertEquals("invalidCurrentPassword", result);
	}
	
	@Test
	public void testValidateAndUpdatePasswordWhenPasswordNotUpdated() throws Exception{
		String userName = "john";
		String currentPassword = "test123";
		String newPassword = "test123";
		String result = null;
		String encryptedPassphrase = "cf199a1c977ed20165ca21aa7ac222ea2b43fa1d";
		String salt = "d9ad9e1d-c12f-4a19-898b-eb180c3523c6";
		String hashedPassword = "cf199a1c977ed20165ca21aa7ac222ea2b43fa1d";
		//String newSalt = "";
		String testSalt = UUID.randomUUID().toString();
		Date passwordResetAt = new Date();
		Date updatedAt = new Date();
		int lockValue = 3;
		EasyMock.expect(
				mockChangePasswordDAO.getUserByUserName(userName))
				.andReturn(getUser());
		EasyMock.expect(
				mockSHAHashing.validateHashedPassword(salt, currentPassword))
				.andReturn(hashedPassword);
		EasyMock.expect(
				mockSHAHashing.createHashedPassword(newPassword, testSalt))
				.andReturn(encryptedPassphrase);
		EasyMock.expect(mockLoginDao.getLockValue(userName)).andReturn(lockValue);
		//mockSHAHashing.salt = "";
		EasyMock.expect(
				mockChangePasswordDAO.changePassword(userName, encryptedPassphrase, testSalt, passwordResetAt, updatedAt, PCSConstants.DEFUALT_LOCK_STATUS))
				.andReturn(0);
		EasyMock.replay(mockSHAHashing);
		EasyMock.replay(mockChangePasswordDAO);
		EasyMock.replay(mockLoginDao);
		try{
			result = changePasswordService.validateAndUpdatePassword(userName, currentPassword, newPassword, passwordResetAt, updatedAt, testSalt);
		}catch(Exception exception){
			result = "error";
		}
		assertEquals("error", result);
	}
	
	@Test
	public void testValidateAndUpdatePasswordWhenExceptionInCurrentPassword() throws Exception{
		String userName = "john";
		String currentPassword = "test123";
		String newPassword = "test123";
		String result = null;
		String encryptedPassphrase = "cf199a1c977ed20165ca21aa7ac222ea2b43fa1d";
		String salt = "d9ad9e1d-c12f-4a19-898b-eb180c3523c6";
		String hashedPassword = "cf199a1c977ed20165ca21aa7ac222ea2b43fa1d";
		String testSalt = UUID.randomUUID().toString();
		Date passwordResetAt = new Date();
		Date updatedAt = new Date();
		EasyMock.expect(
				mockChangePasswordDAO.getUserByUserName(userName))
				.andReturn(getUser());
		EasyMock.expect(
				mockSHAHashing.validateHashedPassword(salt, currentPassword))
				.andReturn(hashedPassword);
		EasyMock.expect(
				mockSHAHashing.createHashedPassword(newPassword, testSalt))
				.andReturn(encryptedPassphrase);
		EasyMock.expect(
				mockChangePasswordDAO.changePassword(userName, encryptedPassphrase, testSalt, passwordResetAt, updatedAt, PCSConstants.DEFUALT_LOCK_STATUS))
				.andReturn(0);
		EasyMock.replay(mockSHAHashing);
		EasyMock.replay(mockChangePasswordDAO);
		try{
			result = changePasswordService.validateAndUpdatePassword(userName, currentPassword, newPassword, passwordResetAt, updatedAt, testSalt);
		}catch(Exception exception){
			result = "exception";
		}
		assertEquals("exception", result);
	}
	
	@Test
	public void testChangePasswordDAO() throws Exception {
		changePasswordService.setChangePasswordDAO(mockChangePasswordDAO);
        ChangePasswordDAO expectedValue= mockChangePasswordDAO;
        ChangePasswordDAO actualValue= changePasswordService.getChangePasswordDAO();
        assertTrue("Correct Value successfully retrieved", expectedValue==actualValue);
	}
	
	@Test
	public void testShaHashing() throws Exception {
		changePasswordService.setShaHashing(mockSHAHashing);
        SHAHashing expectedValue= mockSHAHashing;
        SHAHashing actualValue= changePasswordService.getShaHashing();
        assertTrue("Correct Value successfully retrieved", expectedValue==actualValue);
	}
}
