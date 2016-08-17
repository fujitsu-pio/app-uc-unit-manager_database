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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This Entity is mapped to Message table Has One-to-many
 * relationship with UnitMessage table.
 */
@Entity
@Table(name = "MESSAGE")
public class Message {

    /**
     * Constructor.
     */
    public Message() {
    }
    /**
     * Parameterized constructor.
     * @param messageId messageId
     * @param content content
     * @param isGlobal isGlobal
     * @param expiredAt expiredAt
     * @param createdAt createdAt
     * @param updateddAt updateddAt
     * @param deletedAt deletedAt
     * @param unitMessage unitMessage
     */
    public Message(int messageId, String content, byte isGlobal,
             Date expiredAt, Date createdAt, Date updateddAt, Date deletedAt,
            Set<UnitMessage> unitMessage) {
        setMessageId(messageId);
        setContent(content);
        setIsGlobal(isGlobal);
        setExpiredAt(expiredAt);
        setCreatedAt(createdAt);
        setUpdateddAt(updateddAt);
        setDeletedAt(deletedAt);
        setUnitMessage(unitMessage);
    }
    @Id
    @GeneratedValue
    @Column(name = "MESSAGEID")
    private int messageId;
    @Column(name = "CONTENT")
    private String content;
    @Column(name = "ISGLOBAL")
    private byte isGlobal;
    @Column(name = "EXPIRED_AT", nullable = false)
    private Date expiredAt;
    @Column(name = "CREATED_AT")
    private Date createdAt;
    @Column(name = "UPDATED_AT")
    private Date updateddAt;
    @Column(name = "DELETED_AT")
    private Date deletedAt;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unitMessageId.message", cascade = CascadeType.ALL)
    private Set<UnitMessage> unitMessage = new HashSet<UnitMessage>(0);

    /**
     * @return the messageId
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the isGlobal
     */
    public byte getIsGlobal() {
        return isGlobal;
    }

    /**
     * @param isGlobal the isGlobal to set
     */
    public void setIsGlobal(byte isGlobal) {
        this.isGlobal = isGlobal;
    }

    /**
     * @return the expiredAt
     */
    public Date getExpiredAt() {
        if (expiredAt == null) {
            return null;
        } else {
            return (Date) expiredAt.clone();
        }
    }
    /**
     * @param expiredAt the expiredAt to set
     */
    public void setExpiredAt(Date expiredAt) {
        if (expiredAt == null) {
            this.expiredAt = null;
        } else {
            this.expiredAt = (Date) expiredAt.clone();
        }
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
