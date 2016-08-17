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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fujitsu.dc.gui.portal.PCSCustomException;
import com.fujitsu.dc.gui.portal.dao.EditUserInformationDAO;
import com.fujitsu.dc.gui.portal.model.User;

public class EditUserInformationServiceTest {

	private EditUserInformationService editUserInformationService = null;
	private EditUserInformationDAO mockEditUserInformationDAO = null;

	@Before
	public void setUp() throws Exception {
		editUserInformationService = new EditUserInformationService();
		mockEditUserInformationDAO = EasyMock
				.createStrictMock(EditUserInformationDAO.class);
		editUserInformationService
				.setEditUserInformationDAO(mockEditUserInformationDAO);
	}

	@After
	public void tearDown() {
		editUserInformationService = null;

	}

	@Test
	public void testUpdateUserInfo() throws PCSCustomException {
		String userName = "testuser";
		String newFirstName = "name";
		String newFamilyName = "technologies";
		String newEmail = "test@exapmle.com";
		Date updatedAt = new Date();
		int result = 1;
		EasyMock.expect(
				mockEditUserInformationDAO.updateUserDetails(userName,
						newFirstName, newFamilyName, newEmail, updatedAt))
				.andReturn(result);
		EasyMock.replay(mockEditUserInformationDAO);
		String updateStatus = editUserInformationService.updateUserInfo(
				userName, newFirstName, newFamilyName, newEmail, updatedAt);
		assertEquals(updateStatus, "success");
	}

	@Test
	public void testGetUserInfo() throws PCSCustomException {
		String userName = "testuser";
		List<User> userInfo = new ArrayList<User>();
		User user = new User();
		user.setFirstName("name");
		user.setFamilyName("technologies");
		user.setEmail("test@exapmle.com");
		userInfo.add(user);
		EasyMock.expect(mockEditUserInformationDAO.getUserDetails(userName))
				.andReturn(userInfo);
		EasyMock.replay(mockEditUserInformationDAO);
		List<User> existingUserInfo = editUserInformationService
				.getUserInfo(userName);
		assertEquals(existingUserInfo.size(), 1);
	}

	@Test
	public void testEditUserInformationDAO() throws Exception {
		editUserInformationService
				.setEditUserInformationDAO(mockEditUserInformationDAO);
		EditUserInformationDAO expectedValue = mockEditUserInformationDAO;
		EditUserInformationDAO actualValue = editUserInformationService
				.getEditUserInformationDAO();
		assertTrue("Correct Value successfully retrieved",
				expectedValue == actualValue);
	}
}
