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

package com.fujitsu.dc.gui.portal;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;

/**
 * Cell Local Token 縺ｮ逕滓�繝ｻ繝代�繧ｹ繧定｡後≧繧ｯ繝ｩ繧ｹ.
 */
public abstract class LocalToken extends AbstractOAuth2Token {

     /**
      * AES/CBC/PKCS5Padding.
      */
     public static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
     private static final String SEPARATOR = "\t";
     static final int IV_BYTE_LENGTH = 16;

     private static byte[] keyBytes;
     private static SecretKey aesKey;

     /**
      * 蠕ｩ蜿ｷ縺吶ｋ.
      * @param in 證怜捷蛹匁枚蟄怜�
      * @param ivBytes 繧､繝九す繝｣繝ｫ繝吶け繝医Ν
      * @return 蠕ｩ蜿ｷ縺輔ｌ縺滓枚蟄怜�
      * @throws AbstractOAuth2Token.TokenParseException 萓句､�
      */
     public static String decode(final String in, final byte[] ivBytes)
               throws AbstractOAuth2Token.TokenParseException {
          byte[] inBytes = DcCoreUtils.decodeBase64Url(in);
          Cipher cipher;
          aesKey = new SecretKeySpec(ivBytes, "AES");
          try {
               cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
          } catch (NoSuchAlgorithmException e) {
               throw AbstractOAuth2Token.PARSE_EXCEPTION;
          } catch (NoSuchPaddingException e) {
               throw AbstractOAuth2Token.PARSE_EXCEPTION;
          }
          try {
               cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(
                         ivBytes));
          } catch (InvalidKeyException e) {
               throw AbstractOAuth2Token.PARSE_EXCEPTION;
          } catch (InvalidAlgorithmParameterException e) {
               throw AbstractOAuth2Token.PARSE_EXCEPTION;
          }
          byte[] plainBytes;
          try {
               plainBytes = cipher.doFinal(inBytes);
          } catch (IllegalBlockSizeException e) {
               throw AbstractOAuth2Token.PARSE_EXCEPTION;
          } catch (BadPaddingException e) {
               throw AbstractOAuth2Token.PARSE_EXCEPTION;
          }
          try {
               return new String(plainBytes, CharEncoding.UTF_8);
          } catch (UnsupportedEncodingException e) {
               throw AbstractOAuth2Token.PARSE_EXCEPTION;
          }
     }

     /**
      * 繝代�繧ｹ蜃ｦ逅�.
      * @param token 繝医�繧ｯ繝ｳ
      * @param issuer 逋ｺ陦瑚�
      * @param numFields 繝輔ぅ繝ｼ繝ｫ繝画焚
      * @return 繝代�繧ｹ縺輔ｌ縺溘ヨ繝ｼ繧ｯ繝ｳ
      * @throws AbstractOAuth2Token.TokenParseException 繝医�繧ｯ繝ｳ隗｣驥医↓螟ｱ謨励＠縺溘→縺�
      */
     static String[] doParse(final String token, final String issuer,
               final int numFields) throws AbstractOAuth2Token.TokenParseException {
          String tokenDecoded = decode(token, getIvBytes(issuer));

          String[] frag = tokenDecoded.split(SEPARATOR);

          // 豁｣蟶ｸ縺ｪ蠖｢蠑上〒縺ｪ縺代ｌ縺ｰ繝代�繧ｹ螟ｱ謨励→縺吶ｋ
          if (frag.length != numFields || !issuer.equals(frag[numFields - 1])) {
               throw AbstractOAuth2Token.PARSE_EXCEPTION;
          }
          return frag;
     }

     /**
      * 譁�ｭ怜�繧呈囓蜿ｷ蛹悶☆繧�.
      * @param in 蜈･蜉帶枚蟄怜�
      * @param ivBytes 繧､繝九す繝｣繝ｫ繝吶け繝医Ν
      * @return 證怜捷蛹悶＆繧後◆譁�ｭ怜�
      */
     public static String encode(final String in, final byte[] ivBytes) {
          // IV縺ｫ縲∫匱陦靴ELL縺ｮURL騾��繧貞�繧後ｋ縺薙→縺ｧ縲√ｈ繧顔洒縺�ヨ繝ｼ繧ｯ繝ｳ縺ｫ縲�
          Cipher cipher;
          try {
               cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
               aesKey = new SecretKeySpec(ivBytes, "AES");
               cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(
                         ivBytes));
               byte[] cipherBytes = cipher
                         .doFinal(in.getBytes(CharEncoding.UTF_8));
               return DcCoreUtils.encodeBase64Url(cipherBytes);
          } catch (Exception e) {
               throw new RuntimeException(e);
          }
     }

     /**
      * 謖�ｮ壹�Issuer蜷代￠縺ｮIV(Initial Vector)繧堤函謌舌＠縺ｦ霑斐＠縺ｾ縺�
      * IV縺ｨ縺励※issuer縺ｮ譛�ｾ後�譛�ｾ後��托ｼ匁枚蟄励ｒ騾�ｻ｢縺輔○縺滓枚蟄怜�繧堤畑縺�∪縺吶�
      * 縺薙ｌ縺ｫ繧医ｊ縲�＆縺�ssuer繧呈Φ螳壹＠縺ｦ繝代�繧ｹ縺吶ｋ縺ｨ縲√ヱ繝ｼ繧ｹ縺ｫ螟ｱ謨励☆繧九�.
      * @param issuer Issuer URL
      * @return Initial Vector縺ｮ Byte驟榊�
      */
     protected static byte[] getIvBytes(final String issuer) {
          try {
               return StringUtils.reverse("123456789abcdefg" + issuer)
                         .substring(0, IV_BYTE_LENGTH).getBytes(CharEncoding.UTF_8);
          } catch (UnsupportedEncodingException e) {
               throw new RuntimeException(e);
          }
     }

     /**
      * Key譁�ｭ怜�繧定ｨｭ螳壹＠縺ｾ縺吶�.
      * @param keyString 繧ｭ繝ｼ譁�ｭ怜�.
      */
     public static void setKeyString(String keyString) {
          keyBytes = keyString.getBytes(); // 16/24/32繝舌う繝医�骰ｵ繝舌う繝亥�
          aesKey = new SecretKeySpec(keyBytes, "AES");
     }

     /**
      * 譏守､ｺ逧�↑譛牙柑譛滄俣繧定ｨｭ螳壹＠縺ｦ繝医�繧ｯ繝ｳ繧堤函謌舌☆繧�.
      * @param issuedAt 逋ｺ陦梧凾蛻ｻ(epoch縺九ｉ縺ｮ繝溘Μ遘�
      * @param lifespan 譛牙柑譎る俣(繝溘Μ遘�
      * @param issuer 逋ｺ陦瑚�
      * @param subject 荳ｻ菴�
      * @param schema 繧ｹ繧ｭ繝ｼ繝�
      * @param userName String
      */
     public LocalToken(final long issuedAt, final long lifespan,
               final String issuer, final String subject, final String schema, final String userName) {
          this.issuedAt = issuedAt;
          this.lifespan = lifespan;
          this.issuer = issuer;
          this.subject = subject;
          this.schema = schema;
          this.userName = userName;
     }

     /**
      * It creates token string.
      * @param contents contents
      * @return baseURL baseURL
      */
     final String doCreateTokenString(final String[] contents) {
          StringBuilder raw = new StringBuilder();
          // 逋ｺ陦梧凾蛻ｻ縺ｮEpoch縺九ｉ縺ｮ繝溘Μ遘偵ｒ騾��縺ｫ縺励◆譁�ｭ怜�縺悟�鬆ｭ縺九ｉ蜈･繧九◆繧√�謗ｨ貂ｬ縺励▼繧峨＞縲�
          String iaS = Long.toString(this.issuedAt);
          String iaSr = StringUtils.reverse(iaS);
          raw.append(iaSr);

          raw.append(SEPARATOR);
          raw.append(Long.toString(this.lifespan));
          raw.append(SEPARATOR);
          raw.append(this.subject);
          raw.append(SEPARATOR);
          if (this.schema != null) {
               raw.append(this.schema);
          }

          if (contents != null) {
               for (String cont : contents) {
                    raw.append(SEPARATOR);
                    if (cont != null) {
                         raw.append(cont);
                    }
               }
          }

          raw.append(SEPARATOR);
          raw.append(this.issuer);
          return encode(raw.toString(), getIvBytes(issuer));
     }

}
