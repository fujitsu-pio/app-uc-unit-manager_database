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
package io.personium.gui;

import java.util.ResourceBundle;

/**
 * This class is responsible for providing the resource bundle content based on
 * packaging profile - Development, Production, Staging. Only one instance of
 * class will serve the PCS Portal application i.e. Resource Bundle is loaded
 * one time only - at the time of creation of PCSEnvironment instance.
 */
  public class PersoniumEnvironment {
     private static PersoniumEnvironment environment = null;;
     private ResourceBundle resourceBundle = null;

     /**
      * @return resource bundle
      */
     public ResourceBundle getResourceBundle() {
          return resourceBundle;
     }

     /**
      * @return single instance of class
      */
     public static synchronized PersoniumEnvironment getInstance() {
          if (environment == null) {
               environment = new PersoniumEnvironment();
               environment.setResourceBundle();
          }
          return environment;
     }

     /**
      * Retrieve and set Resource Bundle.
      */
     public void setResourceBundle() {
          resourceBundle = ResourceBundle.getBundle("environment");
     }

     /**
      * Private constructor - no direct instance of class.
      */
     private PersoniumEnvironment() {
     }
}
