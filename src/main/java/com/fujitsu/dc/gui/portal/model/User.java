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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.Date;

/**
 * This class is mapped to User Table Has One-to-many relationship with user,
 * environment Has Many-to-one relationship with Organization.
 */
@Entity
@Table(name = "p_user")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "USERID")
    private int userid;

    /**
     * Constructor.
     */
    public User() {

    }

    /**
     * Parameterized constructor.
     * @param userid userid
     * @param name name
     * @param encryptedPassphrase encryptedPassphrase
     * @param salt salt
     * @param firstName firstName
     * @param familyName familyName
     * @param email email
     * @param status status
     * @param privilege privilege
     * @param randomId randomId
     * @param createdAt createdAt
     * @param verificationSendAt verificationSendAt
     * @param verifiedAt verifiedAt
     * @param updatedAt updatedAt
     * @param lastLoginAt lastLoginAt
     * @param passwordResetAt passwordResetAt
     * @param userEnvironments userEnvironments
     * @param organization organization
     */
    /*public User(int userid, String name, String encryptedPassphrase,
            String salt, String firstName, String familyName, String email,
            String status, String privilege, String randomId, Date createdAt,
            Date verificationSendAt, Date verifiedAt, Date updatedAt,
            Date lastLoginAt, Date passwordResetAt,
            Set<UserEnvironment> userEnvironments, Organization organization) {
        super();
        setUserid(userid);
        setName(name);
        setEncryptedPassphrase(encryptedPassphrase);
        setSalt(salt);
        setFirstName(firstName);
        setFamilyName(familyName);
        setEmail(email);
        setStatus(status);
        setPrivilege(privilege);
        setRandomId(randomId);
        setCreatedAt(createdAt);
        setVerificationSendAt(verificationSendAt);
        setVerifiedAt(verifiedAt);
        setUpdatedAt(updatedAt);
        setLastLoginAt(lastLoginAt);
        setPasswordResetAt(passwordResetAt);
        setUserEnvironments(userEnvironments);
        setOrganization(organization);
    }*/

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "ENCRYPTED_PASSPHRASE")
    private String encryptedPassphrase;

    @Column(name = "SALT")
    private String salt;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "FAMILYNAME")
    private String familyName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "PRIVILEGE", nullable = false)
    private String privilege;

    @Column(name = "RANDOMID", nullable = false, unique = true)
    private String randomId;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "VERIFICATIONSEND_AT")
    private Date verificationSendAt;

    @Column(name = "VERIFIED_AT")
    private Date verifiedAt;

    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @Column(name = "LASTLOGIN_AT")
    private Date lastLoginAt;

    @Column(name = "PASSWORDRESET_AT")
    private Date passwordResetAt;

    @Column(name = "LOCK_STATUS")
    private int lockStatus;

    @Column(name = "TNCACCEPTED_AT")
    private Date termsAndConditionAcceptedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEnvironmentId.user", cascade = CascadeType.ALL)
    private Set<UserEnvironment> userEnvironments = new HashSet<UserEnvironment>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGID", nullable = false)
    private Organization organization;

    /**
     * @return organization
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * @param organization organization
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    /**
     * @return userEnvironments
     */
    public Set<UserEnvironment> getUserEnvironments() {
        return userEnvironments;
    }

    /**
     * @param userEnvironments userEnvironments
     */
    public void setUserEnvironments(Set<UserEnvironment> userEnvironments) {
        this.userEnvironments = userEnvironments;
    }

    /**
     * @return userid
     */
    public int getUserid() {
        return userid;
    }

    /**
     * @param userid userid
     */
    public void setUserid(int userid) {
        this.userid = userid;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return encryptedPassphrase
     */
    public String getEncryptedPassphrase() {
        return encryptedPassphrase;
    }

    /**
     * @param encryptedPassphrase encryptedPassphrase
     */
    public void setEncryptedPassphrase(String encryptedPassphrase) {
        this.encryptedPassphrase = encryptedPassphrase;
    }

    /**
     * @return salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return familyName
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * @param familyName familyName
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return privilege
     */
    public String getPrivilege() {
        return privilege;
    }

    /**
     * @param privilege privilege
     */
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    /*     *//**
     * @return orgId
     */
    /*
     * public String getOrgId() { return orgId; }
     *//**
     * @param orgId
     */
    /*
     * public void setOrgId(String orgId) { this.orgId = orgId; }
     */

    /**
     * @return randomId
     */
    public String getRandomId() {
        return randomId;
    }

    /**
     * @param randomId randomId
     */
    public void setRandomId(String randomId) {
        this.randomId = randomId;
    }

    /**
     * @return createdAt
     */
    public Date getCreatedAt() {
        if (createdAt == null) {
            return null;
        } else {
            return (Date) createdAt.clone();
        }
    }

    /**
     * @param createdAt createdAt
     */
    public void setCreatedAt(Date createdAt) {
        if (createdAt == null) {
            this.createdAt = null;
        } else {
            this.createdAt = (Date) createdAt.clone();
        }
    }

    /**
     * @return verificationSendAt
     */
    public Date getVerificationSendAt() {
        if (verificationSendAt == null) {
            return null;
        } else {
            return (Date) verificationSendAt.clone();
        }
    }

    /**
     * @param verificationSendAt verificationSendAt
     */
    public void setVerificationSendAt(Date verificationSendAt) {
        if (verificationSendAt == null) {
            this.verificationSendAt = null;
        } else {
            this.verificationSendAt = (Date) verificationSendAt.clone();
        }
    }

    /**
     * @return verifiedAt
     */
    public Date getVerifiedAt() {
        if (verifiedAt == null) {
            return null;
        } else {
            return (Date) verifiedAt.clone();
        }
    }

    /**
     * @param verifiedAt verifiedAt
     */
    public void setVerifiedAt(Date verifiedAt) {
        if (verifiedAt == null) {
            this.verifiedAt = null;
        } else {
            this.verifiedAt = (Date) verifiedAt.clone();
        }
    }

    /**
     * @return updatedAt
     */
    public Date getUpdatedAt() {
        if (updatedAt == null) {
            return null;
        } else {
            return (Date) updatedAt.clone();
        }
    }

    /**
     * @param updatedAt updatedAt
     */
    public void setUpdatedAt(Date updatedAt) {
        if (updatedAt == null) {
            this.updatedAt = null;
        } else {
            this.updatedAt = (Date) updatedAt.clone();
        }
    }

    /**
     * @return lastLoginAt
     */
    public Date getLastLoginAt() {
        if (lastLoginAt == null) {
            return null;
        } else {
            return (Date) lastLoginAt.clone();
        }
    }

    /**
     * @param lastLoginAt lastLoginAt
     */
    public void setLastLoginAt(Date lastLoginAt) {
        if (lastLoginAt == null) {
            this.lastLoginAt = null;
        } else {
            this.lastLoginAt = (Date) lastLoginAt.clone();
        }
    }

    /**
     * @return passwordResetAt
     */
    public Date getPasswordResetAt() {
        if (passwordResetAt == null) {
            return null;
        } else {
            return (Date) passwordResetAt.clone();
        }
    }

    /**
     * @param passwordResetAt passwordResetAt
     */
    public void setPasswordResetAt(Date passwordResetAt) {
        if (passwordResetAt == null) {
            this.passwordResetAt = null;
        } else {
            this.passwordResetAt = (Date) passwordResetAt.clone();
        }
    }

    /**
     * @return termsAndConditionAcceptedAt
     */
    public Date getTermsAndConditionAcceptedAt() {
        if (termsAndConditionAcceptedAt == null) {
            return null;
        } else {
            return (Date) termsAndConditionAcceptedAt.clone();
        }
    }

    /**
     * @param termsAndConditionAcceptedAt termsAndConditionAcceptedAt
     */
    public void setTermsAndConditionAcceptedAt(Date termsAndConditionAcceptedAt) {
        if (termsAndConditionAcceptedAt == null) {
            this.termsAndConditionAcceptedAt = null;
        } else {
            this.termsAndConditionAcceptedAt = (Date) termsAndConditionAcceptedAt.clone();
        }
    }

    /**
     * @return lockStatus
     */
    public int getLockStatus() {
        return lockStatus;
    }

    /**
     * @param lockStatus lockStatus
     */
    public void setLockStatus(int lockStatus) {
        this.lockStatus = lockStatus;
    }
}
