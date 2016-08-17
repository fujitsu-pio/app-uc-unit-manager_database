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

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * This is a Join table entity of Organization and Unit.
 * Has Many-to-one relationship with Organization and Unit.
 */
@Entity
@Table(name = "ORGANIZATIONUNIT")
@AssociationOverrides({
     @AssociationOverride(name = "orgUnitId.organization", joinColumns = @JoinColumn(name = "ORGID")),
     @AssociationOverride(name = "orgUnitId.unit", joinColumns = @JoinColumn(name = "UNITID")) })
public class OrgUnit {
     /**
      * Constructor.
      */
     public OrgUnit() { }
     @EmbeddedId
     private OrgUnitId orgUnitId;
     @Column(name = "MAXENV", nullable = false)
     private int maxEnv;
     @Column(name = "CREATED_AT")
     private Date createdAt;
     @Column(name = "UPDATED_AT")
     private Date updateddAt;

     /**
      * @return maxEnv
      */
     public int getMaxEnv() {
          return maxEnv;
     }
     /**
      * @param maxEnv int
      */
     public void setMaxEnv(int maxEnv) {
          this.maxEnv = maxEnv;
     }
     /**
      * Get orgunitid.
      * @return orgUnitId
      */
     public OrgUnitId getOrgUnitId() {
          return orgUnitId;
     }
     /**
      * Set orgunitid.
      * @param orgUnitId OrgUnitId
      */
     public void setOrgUnitId(OrgUnitId orgUnitId) {
          this.orgUnitId = orgUnitId;
     }
     /**
      * @return organization
      */
     @Transient
     public Organization getOrganization() {
          return getOrgUnitId().getOrganization();
     }

     /**
      * @param organization Organization
      */
     public void setOrganization(Organization organization) {
          getOrgUnitId().setOrganization(organization);
     }
     /**
      * @return Unit
      */
     @Transient
     public Unit getUnit() {
          return getOrgUnitId().getUnit();
     }

     /**
      * @param unit Unit
      */
     public void setUnit(Unit unit) {
          getOrgUnitId().setUnit(unit);
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
      * Overridden equals method.
      * @param o Object
      * @return true/false
      */
     public boolean equals(Object o) {
          if (this == o) {
               return true;
          }
          if (o == null || getClass() != o.getClass()) {
               return false;
          }
          OrgUnit that = (OrgUnit) o;
          boolean equalCheck;
          if (getOrgUnitId() != null) {
           equalCheck = !getOrgUnitId().equals(that.getOrgUnitId());
          } else {
           equalCheck = that.getOrgUnitId() != null;
          }
          if (equalCheck) {
               return false;
          }
          return true;
     }
     /**
      * Overridden hashCode method.
      * @return int.
      */
     public int hashCode() {
        int hashCodeVal = 0;
        if (getOrgUnitId() != null) {
           hashCodeVal = getOrgUnitId().hashCode();
        }
      return hashCodeVal;
     }
}
