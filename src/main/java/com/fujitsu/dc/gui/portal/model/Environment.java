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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This Entity is mapped to environment table Has Many-to-one relationship with,
 * organization Has Many-to-one relationship with unit Has One-to-many,
 * relationship with user environment table.
 */
@Entity
@Table(name = "ENVIRONMENT")
public class Environment {

    /**
     * Constructor.
     */
    public Environment() {
    }

    /**
     * Parameterized constructor.
     * @param envId environmentId
     * @param name name
     * @param organizationenv oranizationEnvironment
     * @param unit unit
     * @param userEnvironments userEnvironments
     */
    public Environment(String envId, String name, Organization organizationenv,
            Unit unit, Set<UserEnvironment> userEnvironments) {
        setEnvId(envId);
        setName(name);
        setOrganizationenv(organizationenv);
        setUnit(unit);
        setUserEnvironments(userEnvironments);
    }
    /**
     * Parameterized constructor.
     * @param envId environmentId
     * @param name name
     * @param organizationenv oranizationEnvironment
     * @param unit unit
     * @param userEnvironments userEnvironments
     * @param isDeleted isDeleted
     * @param createdAt createdAt
     * @param updateddAt updateddAt
     * @param deletedAt deletedAt
     */
    public Environment(String envId, String name, Unit unit,
            Organization organizationenv,
            Set<UserEnvironment> userEnvironments, byte isDeleted,
            Date createdAt, Date updateddAt, Date deletedAt) {
        setEnvId(envId);
        setName(name);
        setOrganizationenv(organizationenv);
        setUnit(unit);
        setUserEnvironments(userEnvironments);
        setIsDeleted(isDeleted);
        setCreatedAt(createdAt);
        setUpdateddAt(updateddAt);
        setDeletedAt(deletedAt);
    }

    /**
     * Parameterized constructor.
     * @param envId environmentId
     * @param name name
     * @param organizationenv oranizationEnvironment
     * @param unit unit
     */
    public Environment(String envId, String name, Organization organizationenv,
            Unit unit) {
        this.envId = envId;
        this.name = name;
        this.organizationenv = organizationenv;
        this.unit = unit;
    }

    @Id
    @Column(name = "ENVID", nullable = false)
    private String envId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNITID", nullable = false)
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGID", nullable = false)
    private Organization organizationenv;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEnvironmentId.environment", cascade = CascadeType.ALL)
    private Set<UserEnvironment> userEnvironments = new HashSet<UserEnvironment>(0);
    @Column(name = "ISDELETED")
    private byte isDeleted;
    @Column(name = "CREATED_AT")
    private Date createdAt;
    @Column(name = "UPDATED_AT")
    private Date updateddAt;
    @Column(name = "DELETED_AT")
    private Date deletedAt;

    /**
     * @return organizationenv
     */
    public Organization getOrganizationenv() {
        return organizationenv;
    }

    /**
     * @param organizationenv organizationEnvironment
     */
    public void setOrganizationenv(Organization organizationenv) {
        this.organizationenv = organizationenv;
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
     * @return envId
     */
    public String getEnvId() {
        return envId;
    }

    /**
     * @param envId environmentId
     */
    public void setEnvId(String envId) {
        this.envId = envId;
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
     * @return unit
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * @param unit unit
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * @return the isDeleted
     */
    public byte getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(byte isDeleted) {
        this.isDeleted = isDeleted;
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

    /**
     * @return the deletedAt
     */
    public Date getDeletedAt() {
         if (deletedAt == null) {
             return null;
         } else {
             return (Date) deletedAt.clone();
         }
    }

    /**
     * @param deletedAt the deletedAt to set
     */
    public void setDeletedAt(Date deletedAt) {
        if (deletedAt == null) {
            this.deletedAt = null;
        } else {
            this.deletedAt = (Date) deletedAt.clone();
        }
    }
}
