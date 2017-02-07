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
 * Test cases for Create Environment functionality
 */
package io.personium.gui.portal.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.personium.gui.portal.dao.CreateEnvironmentDAO;
import io.personium.gui.portal.model.Unit;

public class CreateEnvironmentServiceTest {
	
	private CreateEnvironmentService createEnvironmentService;
	private CreateEnvironmentDAO mockCreateEnvironmentDAO;
	
	/**
	 * set up pre-requisite to run private method
	 * */
	@Before
	public void setUp() throws Exception{
		createEnvironmentService = new CreateEnvironmentService();
		mockCreateEnvironmentDAO = EasyMock
				.createStrictMock(CreateEnvironmentDAO.class);
		createEnvironmentService.setCreateEnvironmentDAO(mockCreateEnvironmentDAO);
	}

	@After
	public void tearDown() {
		createEnvironmentService =null;

	}
	
	@Test
	public void testCreateEnvironmentWhenAdminAndTypeNotAllAndExceedMaxEnvt() throws Exception{
		String userName = "testUser";
		String privilege = "Administrator";
		int userID = 1;
		String envtID = UUID.randomUUID().toString();
		String envtName = "testenvtname";
		String orgID = "testorgID";
		int unitID = 1;
		int maxEnvtLimit = 3;
		int noOfCreatedEnvt = 3;
		String unitType = "Limited";
		EasyMock.expect(
				mockCreateEnvironmentDAO.checkUserPrivilege(userName))
				.andReturn(privilege);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchUserIDOnName(userName))
				.andReturn(userID);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchUnitType(unitID))
				.andReturn(unitType);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchMaxEnvironmentLimit(orgID, unitID))
				.andReturn(maxEnvtLimit);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchNumberOfCreatedEnvt(orgID, unitID))
				.andReturn(noOfCreatedEnvt);
		EasyMock.replay(mockCreateEnvironmentDAO);
		String result = createEnvironmentService.createEnvironment(envtName, orgID, unitID, userName, envtID);
		assertEquals(result, "exceedMaxEnvt");
	}
	
	@Test
	public void testCreateEnvironmentWhenAdminAndTypeNotAllAndNotExceedMaxEnvtAndSuccess() throws Exception{
		String userName = "testUser";
		String privilege = "Administrator";
		int userID = 1;
		String envtID = UUID.randomUUID().toString();
		String envtName = "testenvtname";
		String orgID = "testorgID";
		int unitID = 1;
		int maxEnvtLimit = 10;
		int noOfCreatedEnvt = 5;
		String unitType = "Limited";
		EasyMock.expect(
				mockCreateEnvironmentDAO.checkUserPrivilege(userName))
				.andReturn(privilege);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchUserIDOnName(userName))
				.andReturn(userID);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchUnitType(unitID))
				.andReturn(unitType);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchMaxEnvironmentLimit(orgID, unitID))
				.andReturn(maxEnvtLimit);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchNumberOfCreatedEnvt(orgID, unitID))
				.andReturn(noOfCreatedEnvt);
		EasyMock.expect(
				mockCreateEnvironmentDAO.insertEnvironmentRecord(envtName, orgID, unitID, envtID, userID))
				.andReturn(true);
		EasyMock.replay(mockCreateEnvironmentDAO);
		String result = createEnvironmentService.createEnvironment(envtName, orgID, unitID, userName, envtID);
		assertEquals(result, "success");
	}
	
	@Test
	public void testCreateEnvironmentWhenAdminAndTypeNotAllAndNotExceedMaxEnvtAndFailure() throws Exception{
		String userName = "testUser";
		String privilege = "Administrator";
		int userID = 1;
		String envtID = UUID.randomUUID().toString();
		String envtName = "testenvtname";
		String orgID = "testorgID";
		int unitID = 1;
		int maxEnvtLimit = 10;
		int noOfCreatedEnvt = 5;
		String unitType = "Limited";
		boolean result = false;
		EasyMock.expect(
				mockCreateEnvironmentDAO.checkUserPrivilege(userName))
				.andReturn(privilege);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchUserIDOnName(userName))
				.andReturn(userID);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchUnitType(unitID))
				.andReturn(unitType);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchMaxEnvironmentLimit(orgID, unitID))
				.andReturn(maxEnvtLimit);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchNumberOfCreatedEnvt(orgID, unitID))
				.andReturn(noOfCreatedEnvt);
		EasyMock.expect(
				mockCreateEnvironmentDAO.insertEnvironmentRecord(envtName, orgID, unitID, envtID, userID))
				.andReturn(false);
		EasyMock.replay(mockCreateEnvironmentDAO);
		try{
			createEnvironmentService.createEnvironment(envtName, orgID, unitID, userName, envtID);
		}catch(Exception exception){
			result = true;
		}
		assertEquals(result, true);
	}
	
	@Test
	public void testCreateEnvironmentWhenAdminAndTypeAllAndSuccess() throws Exception{
		String userName = "testUser";
		String privilege = "Administrator";
		int userID = 1;
		String envtID = UUID.randomUUID().toString();
		String envtName = "testenvtname";
		String orgID = "testorgID";
		int unitID = 1;
		String unitType = "All";
		EasyMock.expect(
				mockCreateEnvironmentDAO.checkUserPrivilege(userName))
				.andReturn(privilege);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchUserIDOnName(userName))
				.andReturn(userID);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchUnitType(unitID))
				.andReturn(unitType);
		EasyMock.expect(
				mockCreateEnvironmentDAO.insertEnvironmentRecord(envtName, orgID, unitID, envtID, userID))
				.andReturn(true);
		EasyMock.replay(mockCreateEnvironmentDAO);
		String result = createEnvironmentService.createEnvironment(envtName, orgID, unitID, userName, envtID);
		assertEquals(result, "success");
	}
	
	@Test
	public void testCreateEnvironmentWhenAdminAndTypeAllAndFailure() throws Exception{
		String userName = "testUser";
		String privilege = "Administrator";
		int userID = 1;
		String envtID = UUID.randomUUID().toString();
		String envtName = "testenvtname";
		String orgID = "testorgID";
		int unitID = 1;
		String unitType = "All";
		boolean result = false;
		EasyMock.expect(
				mockCreateEnvironmentDAO.checkUserPrivilege(userName))
				.andReturn(privilege);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchUserIDOnName(userName))
				.andReturn(userID);
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchUnitType(unitID))
				.andReturn(unitType);
		EasyMock.expect(
				mockCreateEnvironmentDAO.insertEnvironmentRecord(envtName, orgID, unitID, envtID, userID))
				.andReturn(false);
		EasyMock.replay(mockCreateEnvironmentDAO);
		try{
			createEnvironmentService.createEnvironment(envtName, orgID, unitID, userName, envtID);
		}
		catch(Exception exception){
			result = true;
		}
		assertEquals(result, true);
	}
	
	@Test
	public void testCreateEnvironmentWhenNotAdmin() throws Exception{
		String userName = "testUser";
		String privilege = "User";
		String envtID = UUID.randomUUID().toString();
		EasyMock.expect(
				mockCreateEnvironmentDAO.checkUserPrivilege(userName))
				.andReturn(privilege);
		EasyMock.replay(mockCreateEnvironmentDAO);
		String result = createEnvironmentService.createEnvironment("testEnvtName", "testorgID", 1,  userName, envtID);
		assertEquals(result, "notAdmin");
	}
	
	@Test
	public void testGetUnitURLListWhenEmpty() throws Exception{
		List<Unit> unitList = null;
		Map<Integer, List<String>> unitUrlList = new HashMap<Integer, List<String>>();
		String orgID = "testOrg";
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchUnitURLList(orgID))
				.andReturn(unitList);
		EasyMock.replay(mockCreateEnvironmentDAO);
		unitUrlList = createEnvironmentService.getUnitURLList(orgID);
		assertEquals(unitUrlList, null);
	}
	
	@Test
	public void testGetUnitURLListWithData() throws Exception{
		List<Unit> unitList = new ArrayList<Unit>();
		Unit unit = new Unit();
		unit.setUnitId(1);
		unit.setUrl("http://test.personium.io");
		unitList.add(unit);
		Map<Integer, List<String>> unitUrlList = new HashMap<Integer, List<String>>();
		String orgID = "testOrg";
		EasyMock.expect(
				mockCreateEnvironmentDAO.fetchUnitURLList(orgID))
				.andReturn(unitList);
		EasyMock.replay(mockCreateEnvironmentDAO);
		unitUrlList = createEnvironmentService.getUnitURLList(orgID);
		assertEquals(unitUrlList.size(), 1);
	}
	
	@Test
	public void testCreateEnvironmentDAO() throws Exception {
		createEnvironmentService.setCreateEnvironmentDAO(mockCreateEnvironmentDAO);
        CreateEnvironmentDAO expectedValue= mockCreateEnvironmentDAO;
        CreateEnvironmentDAO actualValue= createEnvironmentService.getCreateEnvironmentDAO();
        assertTrue("Correct Value successfully retrieved", expectedValue==actualValue);
 }
}
