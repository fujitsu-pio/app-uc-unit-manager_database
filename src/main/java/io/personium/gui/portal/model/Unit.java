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
package io.personium.gui.portal.model;

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
 * This class is mapped to Unit Table Has One-to-many relationship with,
 * environment and orgunit.
 */
@Entity
@Table(name = "UNIT")
public class Unit {

    /**
     * Constructor.
     */
    public Unit() {
    };
    /**
     * Parameterized constructor.
     * @param unitId unitId
     * @param url url
     * @param type type
     * @param environments environments
     * @param orgUnits orgUnits
     * @param isAvailable isAvailable
     */
    public Unit(int unitId, String url, String type,
            Set<Environment> environments, Set<OrgUnit> orgUnits, int isAvailable) {
        setUnitId(unitId);
        setUrl(url);
        setType(type);
        setEnvironments(environments);
        setOrgUnits(orgUnits);
        setIsAvailable((byte) isAvailable);
    }

    /**
     * Parameterized constructor.
     * @param unitId unitId
     * @param url url
     * @param type type
     * @param environments environments
     * @param orgUnits orgUnits
     * @param name name
     * @param vsysid vsysid
     * @param plan plan
     * @param odataDisk odataDisk
     * @param webdavDisk webdavDisk
     * @param status status
     * @param isDeleted isDeleted
     * @param mail mail
     * @param mailPort mailPort
     * @param globalIP globalIP
     * @param createdAt createdAt
     * @param updateddAt updateddAt
     * @param deletedAt deletedAt
     */
    /*public Unit(int unitId, String url, String type,
            Set<Environment> environments, Set<OrgUnit> orgUnits, String name,
            String vsysid, String plan, long odataDisk, long webdavDisk,
            String status, byte isDeleted, String mail, String mailPort,
            String globalIP, Date createdAt, Date updateddAt, Date deletedAt) {
        setUnitId(unitId);
        setUrl(url);
        setType(type);
        setEnvironments(environments);
        setOrgUnits(orgUnits);
        setName(name);
        setVsysid(vsysid);
        setPlan(plan);
        setOdataDisk(odataDisk);
        setWebdavDisk(webdavDisk);
        setStatus(status);
        setIsDeleted(isDeleted);
        setMail(mail);
        setMailPort(mailPort);
        setGlobalIP(globalIP);
        setCreatedAt(createdAt);
        setUpdateddAt(updateddAt);
        setDeletedAt(deletedAt);
    }*/

    @Id
    @Column(name = "UNITID")
    private int unitId;

    @Column(name = "URL", unique = true, nullable = false)
    private String url;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unit", cascade = CascadeType.ALL)
    private Set<Environment> environments = new HashSet<Environment>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orgUnitId.unit", cascade = CascadeType.ALL)
    private Set<OrgUnit> orgUnits = new HashSet<OrgUnit>(0);
    @Column(name = "NAME")
    private String name;
    @Column(name = "VSYSID")
    private String vsysid;
    @Column(name = "PLAN")
    private String plan;
    /*@Column(name = "STATUS")
    private String status;*/
    @Column(name = "ISAVAILABLE")
    private byte isAvailable;
    @Column(name = "ISDELETED")
    private byte isDeleted;
    @Column(name = "MAIL")
    private String mail;
    @Column(name = "MAIL_PORT")
    private String mailPort;
    @Column(name = "GLOBAL_IP")
    private String globalIP;
    @Column(name = "CREATED_AT")
    private Date createdAt;
    @Column(name = "UPDATED_AT")
    private Date updateddAt;
    @Column(name = "DELETED_AT")
    private Date deletedAt;
    @Column(name = "DISPLAYID", nullable = false)
    private String displayId;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unitMessageId.unit", cascade = CascadeType.ALL)
    private Set<UnitMessage> unitMessage = new HashSet<UnitMessage>(0);

    /**
     * @return displayId
     */
    public String getDisplayId() {
        return displayId;
    }
    /**
     * @param displayId displayId
     */
    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }
    /**
     * @return organizationUnits
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
     * @return unitId
     */
    public int getUnitId() {
        return unitId;
    }

    /**
     * @param unitId unitId
     */
    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the vsysid
     */
    public String getVsysid() {
        return vsysid;
    }

    /**
     * @param vsysid the vsysid to set
     */
    public void setVsysid(String vsysid) {
       this.vsysid = vsysid;
    }

    /**
     * @return the plan
     */
    public String getPlan() {
       return plan;
    }

    /**
     * @param plan the plan to set
     */
    public void setPlan(String plan) {
       this.plan = plan;
     }

    /**
     * @return the status
     */
   /* public String getStatus() {
        return status;
    }*/

    /**
     * @param status the status to set
     */
    /*public void setStatus(String status) {
        this.status = status;
    }*/

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
     * @return the isAvailable
     */
    public byte getIsAvailable() {
        return isAvailable;
    }

    /**
     * @param isAvailable the isAvailable to set
     */
    public void setIsAvailable(byte isAvailable) {
        this.isAvailable = isAvailable;
    }
    /**
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * @return the mailPort
     */
    public String getMailPort() {
        return mailPort;
    }

    /**
     * @param mailPort the mailPort to set
     */
    public void setMailPort(String mailPort) {
        this.mailPort = mailPort;
    }

    /**
     * @return the globalIP
     */
    public String getGlobalIP() {
        return globalIP;
    }

    /**
     * @param globalIP the globalIP to set
     */
    public void setGlobalIP(String globalIP) {
        this.globalIP = globalIP;
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
    /**
     * @return the unitMessage
     */
    public Set<UnitMessage> getUnitMessage() {
        return unitMessage;
    }
    /**
     * @param unitMessage the unitMessage to set
     */
    public void setUnitMessage(Set<UnitMessage> unitMessage) {
        this.unitMessage = unitMessage;
    }

}
