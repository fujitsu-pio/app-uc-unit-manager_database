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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This class is mapped to Organization table Has One-to-many relationship with,
 * User Has One-to-many relationship with Environment Has One-to-many,
 * relationship with OrgUnit.
 */
@Entity
@Table(name = "ORGANIZATION")
public class Organization {

    /**
     * Constructor.
     */
    public Organization() {
    }

    /**
     * Parameterized constructor.
     * @param orgId organizationId
     * @param name name
     * @param isActive status
     * @param users users
     * @param environments environments
     * @param orgUnits organizationUnits
     * @param createdAt createdAt
     * @param updateddAt updateddAt
     */
    public Organization(String orgId, String name, int isActive,
            Set<User> users, Set<Environment> environments,
            Set<OrgUnit> orgUnits, Date createdAt, Date updateddAt) {
        setOrgId(orgId);
        setName(name);
        setIsActive(isActive);
        setUsers(users);
        setEnvironments(environments);
        setOrgUnits(orgUnits);
        setCreatedAt(createdAt);
        setUpdateddAt(updateddAt);
    }

    @Id
    @Column(name = "ORGID", nullable = false)
    private String orgId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ISACTIVE", nullable = false)
    private int isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization", cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<User>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organizationenv", cascade = CascadeType.ALL)
    private Set<Environment> environments = new HashSet<Environment>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orgUnitId.organization", cascade = CascadeType.ALL)
    private Set<OrgUnit> orgUnits = new HashSet<OrgUnit>(0);
    @Column(name = "CREATED_AT")
    private Date createdAt;
    @Column(name = "UPDATED_AT")
    private Date updateddAt;

    /**
     * @return environments
     */
    public Set<Environment> getEnvironments() {
        return environments;
    }

    /**
     * @param environments environments
     */
    public void setEnvironments(Set<Environment> environments) {
        this.environments = environments;
    }

    /**
     * @return users
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * @param users users
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /**
     * @return orgId
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * @param orgId organizationId
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
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
     * @return isActive
     */
    public int getIsActive() {
        return isActive;
    }

    /**
     * @param isActive status
     */
    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    /**
     * @return orgUnits
     */
    public Set<OrgUnit> getOrgUnits() {
        return orgUnits;
    }

    /**
     * @param orgUnits organizationUnits
     */
    public void setOrgUnits(Set<OrgUnit> orgUnits) {
        this.orgUnits = orgUnits;
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
     * @return the updateddAt
     */
    public Date getUpdateddAt() {
        if (updateddAt == null) {
            return null;
        } else {
            return (Date) updateddAt.clone();
        }
    }

    /**
     * @param updateddAt the updateddAt to set
     */
    public void setUpdateddAt(Date updateddAt) {
        if (updateddAt == null) {
            this.updateddAt = null;
        } else {
            this.updateddAt = (Date) updateddAt.clone();
        }
    }
}
