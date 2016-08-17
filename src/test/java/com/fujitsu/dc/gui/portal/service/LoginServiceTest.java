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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fujitsu.dc.gui.portal.PCSConstants;
import com.fujitsu.dc.gui.portal.SHAHashing;
import com.fujitsu.dc.gui.portal.dao.CreateEnvironmentDAO;
import com.fujitsu.dc.gui.portal.dao.LoginDao;
import com.fujitsu.dc.gui.portal.model.Environment;
import com.fujitsu.dc.gui.portal.model.Unit;

public class LoginServiceTest {

	private LoginService loginService = null;
	private LoginDao mockLoginDao = null;
	private SHAHashing mockShaHashing = null;
	private CreateEnvironmentDAO mockCreateEnvironmentDAO = null;

	@Before
	public void setUp() throws Exception {
		loginService = new LoginService();
		mockLoginDao = EasyMock.createStrictMock(LoginDao.class);
		loginService.setLoginDao(mockLoginDao);

		mockCreateEnvironmentDAO = EasyMock
				.createStrictMock(CreateEnvironmentDAO.class);
		loginService
				.setCreateEnvironmentDAO(mockCreateEnvironmentDAO);

		mockShaHashing = EasyMock.createStrictMock(SHAHashing.class);
		loginService.setShaHashing(mockShaHashing);
	}

	@After
	public void tearDown() {
		loginService = null;

	}

	@Test
	public void testValidateUsernamePasswordPass() throws Exception {
		String name = "john";
		String password = "test";
		String salt = "61e2a862-9220-4b82-bebb-816fcc6939dc";
		String encryptedPassword = "d696e62232b37569b8253c43778bca87eaeb87fb";
		String dbUsername = "john";
		int lockValue = 2;
		String status = PCSConstants.DEFAULT_USER_STATUS;
		int defaultLockValue = PCSConstants.DEFUALT_LOCK_STATUS;
		Date updatedAt = new Date(System.currentTimeMillis());
		
		
		EasyMock.expect(mockLoginDao.getDBUsername(name)).andReturn(dbUsername);
		EasyMock.expect(mockLoginDao.getLockValue(dbUsername)).andReturn(lockValue);
		EasyMock.expect(mockLoginDao.getStatusByName(dbUsername)).andReturn(status);
		EasyMock.expect(mockLoginDao.getSaltByName(dbUsername)).andReturn(salt);
		EasyMock.expect(
				mockShaHashing.validateHashedPassword(salt, password))
				.andReturn(encryptedPassword);
		EasyMock.replay(mockShaHashing);

		EasyMock.expect(mockLoginDao.getEncryptedPasswordByName(dbUsername))
				.andReturn(encryptedPassword);
		EasyMock.expect(mockLoginDao.updateLockStatus(dbUsername, defaultLockValue, updatedAt)).andReturn(1);
		EasyMock.replay(mockLoginDao);
		Boolean result = loginService.validateUsernamePassword(dbUsername, password, updatedAt);
		assertEquals(result, true);
	}

	@Test
	public void testValidateUsernamePasswordFail() throws Exception {
		String name = "john";
		String password = "test1234";
		String salt = "478e9dfc-2879-4f7a-97f7-c49d6874765c";
		String encryptedPassword = "a8fd6920a739dd0c8b7ab6d6491de1872784741fb";
		
		String dbUsername = "john";
		int lockValue = 2;
		String status = PCSConstants.DEFAULT_USER_STATUS;
		int updatedLockValue = 3;
		Date updatedAt = new Date(System.currentTimeMillis());
		

		EasyMock.expect(mockLoginDao.getDBUsername(name)).andReturn(dbUsername);
		EasyMock.expect(mockLoginDao.getLockValue(dbUsername)).andReturn(lockValue);
		EasyMock.expect(mockLoginDao.getStatusByName(dbUsername)).andReturn(status);
		EasyMock.expect(mockLoginDao.getSaltByName(dbUsername)).andReturn(salt);
		EasyMock.expect(
				mockShaHashing.validateHashedPassword(salt, password))
				.andReturn(encryptedPassword);
		EasyMock.replay(mockShaHashing);

		EasyMock.expect(mockLoginDao.getEncryptedPasswordByName(dbUsername))
				.andReturn(encryptedPassword);
		EasyMock.expect(mockLoginDao.updateLockStatus(dbUsername, updatedLockValue, updatedAt)).andReturn(1);
		EasyMock.replay(mockLoginDao);
		Boolean result = loginService.validateUsernamePassword(dbUsername, password, updatedAt);
		assertFalse(result);
	}

	@Test
	public void testCheckOrganizationStatusPass() {
		String name = "john";
		int status = 1;
		EasyMock.expect(mockLoginDao.getOrganizationStatus(name)).andReturn(
				status);
		EasyMock.replay(mockLoginDao);
		int result = loginService.checkOrganizationStatus(name);
		assertEquals(result, 1);
	}

	@Test
	public void testGetOrganizationIDPass() {
		String name = "john";
		String organizationID = "ABCD1234";// loginDao.getOrganizationID(name);
		EasyMock.expect(mockLoginDao.getOrganizationID(name)).andReturn(
				organizationID);
		EasyMock.replay(mockLoginDao);
		String result = loginService.getOrganizationID(name);
		assertEquals(result, "ABCD1234");
	}

	@Test
	public void testupdateLastLoggedInPass() {
		String name = "john";
		int userID = 1;
		boolean isUpdated = true;
		EasyMock.expect(mockLoginDao.getUserIDByName(name)).andReturn(userID);
		Date currentDateTime = new Date(System.currentTimeMillis());

		EasyMock.expect(
				mockLoginDao.updateLastLoginAt(userID, name, currentDateTime))
				.andReturn(isUpdated);
		EasyMock.replay(mockLoginDao);

		boolean result = loginService.updateLastLoggedIn(name, currentDateTime);
		assertTrue(result);
	}

	@Test
	public void testGetEnvironmentListPass() throws Exception {
		List<Unit> unitList = new ArrayList<Unit>();
		Environment env = new Environment();
		env.setEnvId("46f5d869-7ac5-4f43-929a-515475bbeb1b");
		env.setName("john");
		List<Environment> listEnvironment = new ArrayList<Environment>();
		Map<String, List<List<String>>> environmentList = new HashMap<String,List<List<String>>>();
		listEnvironment.add(0,env);
		Unit unit = new Unit();
		unit.setUnitId(1);
		unit.setUrl("https://fj.baas.jp.fujitsu.com/");
		unitList.add(unit);
		String orgID = "ABCD1234";
		int unitID = 1;
		EasyMock.expect(mockCreateEnvironmentDAO.fetchUnitURLList(orgID))
				.andReturn(unitList);
		EasyMock.replay(mockCreateEnvironmentDAO);

		EasyMock.expect(mockLoginDao.fetchEnvironmentList(orgID, unitID))
				.andReturn(listEnvironment);
		EasyMock.replay(mockLoginDao);

		environmentList = loginService.getEnvironmentList(orgID);
		assertEquals(environmentList.size(), 1);
	}
	
	@Test
	public void testGetUserPrivilege() {
		String userName = "john";
		String privilege = "admin";
		EasyMock.expect(mockCreateEnvironmentDAO.checkUserPrivilege(userName))
		.andReturn(privilege);
		EasyMock.replay(mockCreateEnvironmentDAO);
		String result = loginService.getUserPrivilege(userName);
		assertEquals(result, "admin");
	}
	
	@Test
	public void testgetNameFromDB() throws Exception {
		String name = "testuser";
		String dbUsername = "testuser";
		EasyMock.expect(mockLoginDao.getDBUsername(name)).andReturn(dbUsername);
		EasyMock.replay(mockLoginDao);
		String result = loginService.getNameFromDB(name);
		assertEquals(result, "testuser");
	}
	
	@Test
	public void testNotificationStatusListWithGlobal() throws Exception {
		List<Date> dateList = new ArrayList<Date>();
		dateList.add(new Date());
		Map<Integer, List<String>> unitURLList = new HashMap<Integer, List<String>>();
		List<String> unit1 = new ArrayList<String>();
		unit1.add("1");
		unit1.add("https://test");
		unitURLList.put(1, unit1);
		Map<Integer, Boolean> notificationStatusList = new HashMap<Integer, Boolean>();
		EasyMock.expect(mockLoginDao.getNotificationStatusListGlobal()).andReturn(dateList);
		EasyMock.replay(mockLoginDao);
		notificationStatusList = loginService.getNotificationStatusList(unitURLList);
		assertNotNull(notificationStatusList);
	}
	
	@Test
	public void testNotificationStatusListForWithData() throws Exception {
		List<Date> dateListForGlobal = null;
		List<Date> dateList = new ArrayList<Date>();
		dateList.add(new Date());
		int unitID = 1;
		Map<Integer, List<String>> unitURLList = new HashMap<Integer, List<String>>();
		List<String> unit1 = new ArrayList<String>();
		unit1.add("1");
		unit1.add("https://test.personium.io/");
		unitURLList.put(1, unit1);
		Map<Integer, Boolean> notificationStatusList = new HashMap<Integer, Boolean>();
		EasyMock.expect(mockLoginDao.getNotificationStatusListGlobal()).andReturn(dateListForGlobal);
		EasyMock.expect(mockLoginDao.getNotificationStatusListForUnit(unitID)).andReturn(dateList);
		EasyMock.replay(mockLoginDao);
		notificationStatusList = loginService.getNotificationStatusList(unitURLList);
		assertNotNull(notificationStatusList);
	}
	
	@Test
	public void testNotificationStatusListForWithOutData() throws Exception {
		List<Date> dateListForGlobal = null;
		List<Date> dateList = new ArrayList<Date>();
		dateList.add(new Date(99,8,7));
		int unitID = 1;
		Map<Integer, List<String>> unitURLList = new HashMap<Integer, List<String>>();
		List<String> unit1 = new ArrayList<String>();
		unit1.add("1");
		unit1.add("https://test.personium.io/");
		unitURLList.put(1, unit1);
		Map<Integer, Boolean> notificationStatusList = new HashMap<Integer, Boolean>();
		EasyMock.expect(mockLoginDao.getNotificationStatusListGlobal()).andReturn(dateListForGlobal);
		EasyMock.expect(mockLoginDao.getNotificationStatusListForUnit(unitID)).andReturn(dateList);
		EasyMock.replay(mockLoginDao);
		notificationStatusList = loginService.getNotificationStatusList(unitURLList);
		assertNotNull(notificationStatusList);
	}
}
