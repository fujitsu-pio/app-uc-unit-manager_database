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
package io.personium.gui.portal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The purpose of this class is to create a hashed password.
 */
public class SHAHashing {
     /**
      * The purpose of this method is to create hashed password when user
      * registers with the portal.
      * @param password String
      * @param salt String
      * @return hashedPassword String
      * @throws PersoniumCustomException exception
      */
     public String createHashedPassword(String password, String salt)
               throws PersoniumCustomException {
          // SALT is a random number
          String saltedPassword = salt + password;
          String hashedPassword = generateHash(saltedPassword);
          return hashedPassword;
     }

     /**
      * The purpose of this method is to create hashed password at the time of
      * login.
      * @param salt String
      * @param password String
      * @return hashedPassword String
      * @throws PersoniumCustomException exception
      */
     public String validateHashedPassword(String salt, String password) throws PersoniumCustomException {
          String saltedPassword = salt + password;
          String hashedPassword = generateHash(saltedPassword);
          return hashedPassword;

     }

     /**
      * The purpose of this method is to generate Hash.
      * @param input String
      * @return hash String
      * @throws PersoniumCustomException exception
      */
     private String generateHash(String input) throws PersoniumCustomException {
          StringBuilder hash = new StringBuilder();
          try {
               MessageDigest sha = MessageDigest.getInstance("SHA-1");
               byte[] hashedBytes = sha.digest(input.getBytes());
               char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                         'a', 'b', 'c', 'd', 'e', 'f'};
               final int digitCount = 4;
               final int hexCodeOne = 0xf0;
               final int hexCodeTwo = 0x0f;
               for (int idx = 0; idx < hashedBytes.length; ++idx) {
                    byte b = hashedBytes[idx];
                    hash.append(digits[(b & hexCodeOne) >> digitCount]);
                    hash.append(digits[b & hexCodeTwo]);
               }
          } catch (NoSuchAlgorithmException exe) {
               throw new PersoniumCustomException("NoSuchAlgorithmException" + exe);
          }
          return hash.toString();
     }
}
