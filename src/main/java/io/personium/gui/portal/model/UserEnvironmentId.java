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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 * This class serves the purpose of embedding userId and envId
 * as the composite primary key of UserEnvironment Table.
 */
@Embeddable
public class UserEnvironmentId implements Externalizable {
     @ManyToOne(targetEntity = User.class)
     private User user;
     @ManyToOne(targetEntity = Environment.class)
     private Environment environment;
     private static final long serialVersionUID = 1L;
     /**
      * Constructor.
      */
     public UserEnvironmentId() { }

     /**
      * @return user
      */
     public User getUser() {
          return user;
     }

     /**
      * @param user User
      */
     public void setUser(User user) {
          this.user = user;
     }

     /**
      * @return environment
      */
     public Environment getEnvironment() {
          return environment;
     }

     /**
      * @param environment Environment
      */
     public void setEnvironment(Environment environment) {
          this.environment = environment;
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
          UserEnvironmentId that = (UserEnvironmentId) o;
          boolean userCheck;
          if (user != null) {
             userCheck = !user.equals(that.user);
          } else {
             userCheck = that.user != null;
          }
          if (userCheck) {
               return false;
          }
          boolean environmentCheck;
          if (environment != null) {
             environmentCheck = !environment.equals(that.environment);
          } else {
             environmentCheck = that.environment != null;
          }
          if (environmentCheck) {
               return false;
          }
          return true;
     }
     /**
      * Overridden hashCode method.
      * @return result
      */
     @Override
     public int hashCode() {
          int result = 0;
          if (user != null) {
              result = user.hashCode();
          }
          final int multiplier = 31;
          int hashCodeVal = 0;
          if (environment != null) {
              hashCodeVal = environment.hashCode();
          }
          result = multiplier * result + hashCodeVal;
          return result;
     }
     /**
      * The purpose of this function is to perform Serialization.
      * @param out ObjectOutput
      * @throws IOException exception
      */
     @Override
     public void writeExternal(ObjectOutput out) throws IOException {
     }
     /**
      * The purpose of this function is to perform Serialization.
      * @param in ObjectInput
      * @throws IOException exception
      * @throws ClassNotFoundException exception
      */
     @Override
     public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
     }

}
