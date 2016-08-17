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

import java.util.Date;

/**
 * This class carries the user information.
 */
public class UserBean {
     private String username;
     private String encryptedPass;
     private String salt;
     private String firstName;
     private String familyName;
     private String email;
     private String status;
     private String privilege;
     private String randomId;
     private Date createdAt;
     private Date updatedAt;
     private Date verificationSendAt;
     private Date verifiedAt;
     private Date lastLoginAt;
     private Date passwordResetAt;
     private String organizationID;
     private int orgStatus;
     private String organizationName;
     private Date termsAndConditionAcceptedAt;
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * @return the encryptedPass
     */
    public String getEncryptedPass() {
       return encryptedPass;
    }
    /**
     * @param encryptedPass the encryptedPass to set
     */
    public void setEncryptedPass(String encryptedPass) {
       this.encryptedPass = encryptedPass;
    }
    /**
     * @return the salt
     */
    public String getSalt() {
       return salt;
    }
    /**
     * @param salt the salt to set
     */
    public void setSalt(String salt) {
       this.salt = salt;
    }
    /**
     * @return the firstName
     */
    public String getFirstName() {
       return firstName;
    }
    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * @return the familyName
     */
    public String getFamilyName() {
        return familyName;
    }
    /**
     * @param familyName the familyName to set
     */
    public void setFamilyName(String familyName) {
       this.familyName = familyName;
    }
    /**
     * @return the email
     */
    public String getEmail() {
       return email;
    }
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return the status
     */
    public String getStatus() {
       return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
       this.status = status;
    }
    /**
     * @return the privilege
     */
    public String getPrivilege() {
       return privilege;
    }
    /**
     * @param privilege the privilege to set
     */
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }
    /**
     * @return the randomId
     */
    public String getRandomId() {
       return randomId;
    }
    /**
     * @param randomId the randomId to set
     */
    public void setRandomId(String randomId) {
       this.randomId = randomId;
    }
    /**
     * @return the createdAt
     */
    public Date getCreatedAt() {
        if (createdAt == null) {
            return null;
        } else {
            return (Date) createdAt.clone();
        }
    }
    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        if (createdAt == null) {
            this.createdAt = null;
        } else {
            this.createdAt = (Date) createdAt.clone();
        }
    }
    /**
     * @return the updatedAt
     */
    public Date getUpdatedAt() {
        if (updatedAt == null) {
            return null;
        } else {
            return (Date) updatedAt.clone();
        }
    }
    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(Date updatedAt) {
        if (updatedAt == null) {
            this.updatedAt = null;
        } else {
            this.updatedAt = (Date) updatedAt.clone();
        }
    }
    /**
     * @return the verificationSendAt
     */
    public Date getVerificationSendAt() {
        if (verificationSendAt == null) {
            return null;
        } else {
            return (Date) verificationSendAt.clone();
        }
    }
    /**
     * @param verificationSendAt the verificationSendAt to set
     */
    public void setVerificationSendAt(Date verificationSendAt) {
        if (verificationSendAt == null) {
            this.verificationSendAt = null;
        } else {
            this.verificationSendAt = (Date) verificationSendAt.clone();
        }
    }
    /**
     * @return the verifiedAt
     */
    public Date getVerifiedAt() {
        if (verifiedAt == null) {
            return null;
        } else {
            return (Date) verifiedAt.clone();
        }
    }
    /**
     * @param verifiedAt the verifiedAt to set
     */
    public void setVerifiedAt(Date verifiedAt) {
        if (verifiedAt == null) {
            this.verifiedAt = null;
        } else {
            this.verifiedAt = (Date) verifiedAt.clone();
        }
    }
    /**
     * @return the lastLoginAt
     */
    public Date getLastLoginAt() {
        if (lastLoginAt == null) {
            return null;
        } else {
            return (Date) lastLoginAt.clone();
        }
    }
    /**
     * @param lastLoginAt the lastLoginAt to set
     */
    public void setLastLoginAt(Date lastLoginAt) {
        if (lastLoginAt == null) {
            this.lastLoginAt = null;
        } else {
            this.lastLoginAt = (Date) lastLoginAt.clone();
        }
    }
    /**
     * @return the passwordResetAt
     */
    public Date getPasswordResetAt() {
        if (passwordResetAt == null) {
            return null;
        } else {
            return (Date) passwordResetAt.clone();
        }
    }
    /**
     * @param passwordResetAt the passwordResetAt to set
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
     * @return the organizationID
     */
    public String getOrganizationID() {
       return organizationID;
    }
    /**
     * @param organizationID the organizationID to set
     */
    public void setOrganizationID(String organizationID) {
       this.organizationID = organizationID;
    }
    /**
     * @return the orgStatus
     */
    public int getOrgStatus() {
       return orgStatus;
    }
    /**
     * @param orgStatus the orgStatus to set
     */
    public void setOrgStatus(int orgStatus) {
       this.orgStatus = orgStatus;
    }
    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
       return organizationName;
    }
    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
       this.organizationName = organizationName;
    }

}
