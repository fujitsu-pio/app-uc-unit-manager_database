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

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * This class is mapped to UserEnvironment Table.
 * Has Many-to-one relationship with User
 * Has Many-to-one relationship with Environment
 */
@Entity
@Table(name = "USERENVIRONMENT")
@AssociationOverrides({
          @AssociationOverride(name = "userEnvironmentId.user", joinColumns = @JoinColumn(name = "USERID")),
          @AssociationOverride(name = "userEnvironmentId.environment", joinColumns = @JoinColumn(name = "ENVID")) })
public class UserEnvironment implements java.io.Serializable {

     private static final long serialVersionUID = 1L;

     @EmbeddedId
     private UserEnvironmentId userEnvironmentId = new UserEnvironmentId();

     /**
      * @return environment
      */
     public UserEnvironmentId getUserEnvironmentId() {
          return userEnvironmentId;
     }

     /**
      * @param userEnvironmentId UserEnvironmentId
      */
     public void setUserEnvironmentId(UserEnvironmentId userEnvironmentId) {
          this.userEnvironmentId = userEnvironmentId;
     }

     /**
      * @return user
      */
     @Transient
     public User getUser() {
          return getUserEnvironmentId().getUser();
     }

     /**
      * @param user User
      */
     public void setUser(User user) {
          getUserEnvironmentId().setUser(user);
     }

     /**
      * @return Environment
      */
     @Transient
     public Environment getEnvironment() {
          return getUserEnvironmentId().getEnvironment();
     }

     /**
      * @param environment Environment
      */
     public void setEnvironment(Environment environment) {
          getUserEnvironmentId().setEnvironment(environment);
     }

     /**
      * Overridden equals method.
      * @param o Object
      * @return true/false
      */
     @Override
     public boolean equals(Object o) {
          if (this == o) {
               return true;
          }
          if (o == null || getClass() != o.getClass()) {
               return false;
          }
          UserEnvironment that = (UserEnvironment) o;
          boolean userEnvtIdEqualCheck;
          if (getUserEnvironmentId() != null) {
           userEnvtIdEqualCheck = !getUserEnvironmentId().equals(that.getUserEnvironmentId());
          } else {
           userEnvtIdEqualCheck = that.getUserEnvironmentId() != null;
          }
          if (userEnvtIdEqualCheck) {
               return false;
          }
          return true;
     }
     /**
      * Overridden hashCode method.
      * @return hashCodeVal
      */
     @Override
     public int hashCode() {
      int hashCodeVal = 0;
      if (getUserEnvironmentId() != null) {
       hashCodeVal = getUserEnvironmentId().hashCode();
      }
       return hashCodeVal;
     }

}
