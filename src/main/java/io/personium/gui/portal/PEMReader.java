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
 /**
 **************************************************************************
 *
 * @author:     zhang
 * @version:    $Revision$
 * @created:    Apr 24, 2009
 *
 * Description: A class to decode PEM files
 **/

package io.personium.gui.portal;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.codec.binary.Base64;

/**
 * This class convert PEM into byte array. The begin marker is saved and it can be used to determine the type of the PEM
 * file.
 * @author zhang
 */
public class PEMReader {

    // Begin markers for all supported PEM files
   /**
    * Marker PRIVATE_PKCS1_MARKER.
    */
    public static final String PRIVATE_PKCS1_MARKER = "-----BEGIN RSA PRIVATE KEY-----";
    /**
     * Marker PRIVATE_PKCS8_MARKER.
     */
    public static final String PRIVATE_PKCS8_MARKER = "-----BEGIN PRIVATE KEY-----";
    /**
     * Marker CERTIFICATE_X509_MARKER.
     */
    public static final String CERTIFICATE_X509_MARKER = "-----BEGIN CERTIFICATE-----";
    /**
     * Marker PUBLIC_X509_MARKER.
     */
    public static final String PUBLIC_X509_MARKER = "-----BEGIN PUBLIC KEY-----";
    /**
     * Marker BEGIN_MARKER.
     */
    private static final String BEGIN_MARKER = "-----BEGIN ";

    private final InputStream stream;
    private byte[] derBytes;
    private String beginMarker;

    /**
     * PEMReader reads input stream.
     * @param inStream InputStream
     * @throws IOException exception
     */
    public PEMReader(final InputStream inStream) throws IOException {
        stream = inStream;
        readFile();
    }

    /**
     * PEMReader reads buffer.
     * @param buffer byte array
     * @throws IOException exception
     */
    public PEMReader(final byte[] buffer) throws IOException {
        this(new ByteArrayInputStream(buffer));
    }

    /**
     * PEMReader takes filename.
     * @param fileName String
     * @throws IOException exception
     */
    public PEMReader(final String fileName) throws IOException {
        this(new FileInputStream(fileName));
    }

    /**
     * Gets derBytes.
     * @return derBytes byte
     */
    public byte[] getDerBytes() {
        // For fixing FindBugs warning 'EI_EXPOSE_REP'
        if (derBytes == null) {
            return null;
        } else {
            return derBytes.clone();
        }
        //return derBytes;
    }

    /**
     * Gets beginMarker.
     * @return beginMarker String.
     */
    public String getBeginMarker() {
        return beginMarker;
    }

    /**
     * Read the PEM file and save the DER encoded octet stream and begin marker.
     * @throws IOException exception
     */
    protected void readFile() throws IOException {

        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            while ((line = reader.readLine()) != null) {
                if (line.indexOf(BEGIN_MARKER) != -1) {
                    beginMarker = line.trim();
                    String endMarker = beginMarker.replace("BEGIN", "END");
                    derBytes = readBytes(reader, endMarker);
                    return;
                }
            }
            throw new IOException("Invalid PEM file: no begin marker");
        } finally {
       reader.close();
        }
    }

    /**
     * Read the lines between BEGIN and END marker and convert the Base64 encoded content into binary byte array.
     * @return DER encoded octet stream
     * @throws IOException exception
     */
    private byte[] readBytes(final BufferedReader reader, final String endMarker) throws IOException {
        String line = null;
        StringBuffer buf = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            if (line.indexOf(endMarker) != -1) {

                return Base64.decodeBase64(buf.toString());
            }

            buf.append(line.trim());
        }

        throw new IOException("Invalid PEM file: No end marker");
    }

}
