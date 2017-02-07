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

/**
 * Custom Exception Class for PCS_Portal.
 */
public class PersoniumCustomException extends Exception {
    private static final long serialVersionUID = 1L;
    /** Status Code. */
    private int code = 0;
    /**
     * Constructor.
     * @param msg Message
     * @param throwable Throwable
     */
    public PersoniumCustomException(final String msg, final Throwable throwable) {
        super(msg, throwable);
    }

    /**
     * Constructor.
     * @param msg Message
     */
    public PersoniumCustomException(final String msg) {
        super(msg);
    }

    /**
     * Constructor.
     * @param msg Message
     * @param statusCode Status Code
     */
    public PersoniumCustomException(final String msg, final int statusCode) {
        super(msg);
        this.code = statusCode;
    }

    /**
     * Generate PCSCustomException.
     * @param msg Message
     * @param statusCode Status Code
     * @return PCSCustomException object
     */
    public static PersoniumCustomException create(final String msg, final int statusCode) {
        return new PersoniumCustomException(String.format("%s,%s", Integer.toString(statusCode), msg), statusCode);
    }

    /**
     * The purpose of this method is to get the status code when an exception occurs.
     * @return status code
     */
    public final String getCode() {
        return Integer.toString(code);
    }
}
