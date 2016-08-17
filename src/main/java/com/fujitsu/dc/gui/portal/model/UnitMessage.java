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
 * This is a Join table entity of Message and Unit.
 * Has Many-to-one relationship with Message and Unit.
 */
@Entity
@Table(name = "UNITMESSAGE")
@AssociationOverrides({
     @AssociationOverride(name = "unitMessageId.unit", joinColumns = @JoinColumn(name = "UNITID")),
     @AssociationOverride(name = "unitMessageId.message", joinColumns = @JoinColumn(name = "MESSAGEID")) })
public class UnitMessage {

    /**
     * Constructor.
     */
    public UnitMessage() { }
    @EmbeddedId
    private UnitMessageId unitMessageId;
    @Column(name = "ISHIDDEN")
    private byte isHidden;
    @Column(name = "CREATED_AT")
    private Date createdAt;
    @Column(name = "UPDATED_AT")
    private Date updateddAt;

    /**
     * @return the unitMessageId
     */
    public UnitMessageId getUnitMessageId() {
        return unitMessageId;
    }

    /**
     * @param unitMessageId the unitMessageId to set
     */
    public void setUnitMessageId(UnitMessageId unitMessageId) {
        this.unitMessageId = unitMessageId;
    }
    /**
     * @return message
     */
    @Transient
    public Message getMessage() {
         return getUnitMessageId().getMessage();
    }

    /**
     * @param message Message
     */
    public void setMessage(Message message) {
        getUnitMessageId().setMessage(message);
    }
    /**
     * @return Unit
     */
    @Transient
    public Unit getUnit() {
         return getUnitMessageId().getUnit();
    }

    /**
     * @param unit Unit
     */
    public void setUnit(Unit unit) {
        getUnitMessageId().setUnit(unit);
    }

    /**
     * @return the isHidden
     */
    public byte getIsHidden() {
        return isHidden;
    }

    /**
     * @param isHidden the isHidden to set
     */
    public void setIsHidden(byte isHidden) {
        this.isHidden = isHidden;
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
         UnitMessage that = (UnitMessage) o;
         boolean equalCheck;
         if (getUnitMessageId() != null) {
          equalCheck = !getUnitMessageId().equals(that.getUnitMessageId());
         } else {
          equalCheck = that.getUnitMessageId() != null;
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
       if (getUnitMessageId() != null) {
          hashCodeVal = getUnitMessageId().hashCode();
       }
     return hashCodeVal;
    }
}
