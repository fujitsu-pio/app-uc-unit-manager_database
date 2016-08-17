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
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fujitsu.dc.gui.portal.SHAHashing;
import com.fujitsu.dc.gui.portal.dao.LoginDao;
import com.fujitsu.dc.gui.portal.dao.UserEmailDAO;
import com.fujitsu.dc.gui.portal.model.Organization;
import com.fujitsu.dc.gui.portal.model.User;

public class UserEmailServiceTest {
    private UserEmailService userEmailService = null;
    private UserEmailDAO mockUserEmailDAO = null;
    private SHAHashing mockShaHashing = null;
    private LoginDao mockLoginDao = null;
    private LoginService loginService = null;

    @Before
    public void setUp() throws Exception {
        loginService = new LoginService();
        userEmailService = new UserEmailService();
        mockShaHashing = EasyMock.createStrictMock(SHAHashing.class);
        loginService.setShaHashing(mockShaHashing);
        // loginDao = new LoginDao();
        mockLoginDao = EasyMock.createStrictMock(LoginDao.class);
        userEmailService.setLoginDAO(mockLoginDao);
        mockUserEmailDAO = EasyMock.createStrictMock(UserEmailDAO.class);
        userEmailService.setUserEmailDAO(mockUserEmailDAO);
    }

    @After
    public void tearDown() {
        userEmailService = null;

    }

    @Test
    public void testGetUserInformation() {
        String userName = "testuser";
        List<User> userList = new ArrayList<User>();
        User user = new User();
        user.setName("test");
        user.setFamilyName("familyName");
        user.setStatus("active");
        Organization organization = new Organization();
        organization.setOrgId("5f5299a6");
        user.setOrganization(organization);
        userList.add(user);
        EasyMock.expect(mockUserEmailDAO.getUserInformation(userName))
                .andReturn(userList);
        EasyMock.replay(mockUserEmailDAO);
        List<User> result = userEmailService.getUserInformation(userName);
        assertEquals(result, userList);
    }

    @Test
    public void testGetSubscriberEmail() {
        String userName = "testuser";
        String subscriberEmail = "testuser@exapmle.com";
        EasyMock.expect(mockUserEmailDAO.getSubscriberEmail(userName))
                .andReturn(subscriberEmail);
        EasyMock.replay(mockUserEmailDAO);
        String email = userEmailService.getSubscriberEmail(userName);
        assertEquals(email, subscriberEmail);
    }

    @Test
    public void testUpdateUserVerifiedAt() {
        int userid = 19;
        String userName = "testuser";
        Date currentDateTime = new Date(System.currentTimeMillis());
        EasyMock.expect(mockLoginDao.getUserIDByName(userName)).andReturn(
                userid);
        EasyMock.expect(
                mockUserEmailDAO.updateUserVerifiedAt(userid, currentDateTime))
                .andReturn(true);
        EasyMock.replay(mockLoginDao);
        EasyMock.replay(mockUserEmailDAO);
        boolean result = userEmailService.updateUserVerifiedAt(userName,
                currentDateTime);
        assertEquals(result, true);
    }

    @Test
    public void testUpdateMailVerificationSendAt() {
        int userid = 19;
        String userName = "testuser";
        Date currentDateTime = new Date(System.currentTimeMillis());
        EasyMock.expect(mockLoginDao.getUserIDByName(userName)).andReturn(
                userid);
        EasyMock.expect(
                mockUserEmailDAO.updateMailVerificationSendAt(userid,
                        currentDateTime)).andReturn(true);
        EasyMock.replay(mockLoginDao);
        EasyMock.replay(mockUserEmailDAO);
        boolean result = userEmailService.updateMailVerificationSendAt(
                userName, currentDateTime);
        assertEquals(result, true);
    }

    @Test
    public void testUpdateUserStatus() {
        int userid = 19;
        String userName = "testuser";
        String status = "active";
        Date currentDateTime = new Date(System.currentTimeMillis());
        EasyMock.expect(mockLoginDao.getUserIDByName(userName)).andReturn(
                userid);
        EasyMock.expect(
                mockUserEmailDAO.updateUserStatusToActive(userid,
                        currentDateTime, status)).andReturn(true);
        EasyMock.replay(mockLoginDao);
        EasyMock.replay(mockUserEmailDAO);
        boolean result = userEmailService.updateUserStatus(userName,
                currentDateTime, status);
        assertEquals(result, true);
    }

    @Test
    public void testGetUserOrganizationName() {
        String orgId = "10pbe8e4";
        String userOrgName = "name";
        EasyMock.expect(mockUserEmailDAO.getUserOrganizationName(orgId))
                .andReturn(userOrgName);
        EasyMock.replay(mockUserEmailDAO);
        String orgName = userEmailService.getUserOrganizationName(orgId);
        assertEquals(orgName, userOrgName);
    }

    @Test
    public void testValidatePassword() throws Exception {
        String name = "testuser";
        String password = "hclpcs";
        String salt = "152511d2-debd-4092-9193-6c3119081e8c";
        String encryptedPassword = "fcd5e7684b48230c155d11de79f848f5c26eab08";
        String dbUsername = "testuser";
        EasyMock.expect(mockLoginDao.getDBUsername(name)).andReturn(dbUsername);
        EasyMock.expect(mockLoginDao.getSaltByName(dbUsername)).andReturn(salt);
        EasyMock.expect(mockShaHashing.validateHashedPassword(salt, password))
                .andReturn(encryptedPassword);
        EasyMock.replay(mockShaHashing);
        EasyMock.expect(mockLoginDao.getEncryptedPasswordByName(dbUsername))
                .andReturn(encryptedPassword);
        EasyMock.replay(mockLoginDao);
        boolean isPasswordValid = userEmailService.validatePassword(dbUsername,
                password);
        assertEquals(isPasswordValid, true);
    }

}
