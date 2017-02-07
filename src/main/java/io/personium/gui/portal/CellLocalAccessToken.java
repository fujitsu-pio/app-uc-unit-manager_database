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

import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * Cell Local Token 縺ｮ逕滓�繝ｻ繝代�繧ｹ繧定｡後≧繧ｯ繝ｩ繧ｹ.
 */
public class CellLocalAccessToken extends LocalToken implements IAccessToken {

     /**
      * 繝医�繧ｯ繝ｳ縺ｮ繝励Ξ繝輔ぅ繝�け繧ｹ.
      */
     public static final String PREFIX_ACCESS = "AL~";
     /**
      * 繝医�繧ｯ繝ｳ縺ｮ譛牙柑譎る俣.
      */
     public static final int ACCESS_TOKEN_EXPIRES_HOUR = 1;

     static final int IDX_COUNT = 7;

     static final int IDX_ISSUED_AT = 0;

     static final int IDX_LIFESPAN = 1;

     static final int IDX_ISSUER = 5;

     static final int IDX_SUBJECT = 2;
     static final int IDX_ROLE_LIST = 4;
     static final int IDX_SCHEMA = 3;
     static final int IDX_USERID = 6;
     /**
      * 繝医�繧ｯ繝ｳ譁�ｭ怜�繧段ssuer縺ｧ謖�ｮ壹＆繧後◆Cell縺ｨ縺励※繝代�繧ｹ縺吶ｋ.
      * @param token Token String
      * @param issuer Cell Root URL
      * @return 繝代�繧ｹ縺輔ｌ縺櫃ellLocalToken繧ｪ繝悶ず繧ｧ繧ｯ繝�
      * @throws AbstractOAuth2Token.TokenParseException 繝医�繧ｯ繝ｳ縺ｮ繝代�繧ｹ縺ｫ螟ｱ謨励＠縺溘→縺肴兜縺偵ｉ繧後ｋ萓句､�
      */
     public static CellLocalAccessToken parse(final String token,
               final String issuer) throws AbstractOAuth2Token.TokenParseException {
          if (!token.startsWith(PREFIX_ACCESS) || issuer == null) {
               throw AbstractOAuth2Token.PARSE_EXCEPTION;
          }
          String[] frag = LocalToken.doParse(
                    token.substring(PREFIX_ACCESS.length()), issuer, IDX_COUNT);

          try {
               CellLocalAccessToken ret = new CellLocalAccessToken(
                         Long.valueOf(StringUtils.reverse(frag[IDX_ISSUED_AT])),
                         Long.valueOf(frag[IDX_LIFESPAN]), frag[IDX_ISSUER],
                         frag[IDX_SUBJECT],
                         AbstractOAuth2Token.parseRolesString(frag[IDX_ROLE_LIST]),
                         frag[IDX_SCHEMA], frag[IDX_USERID]);

               return ret;
          } catch (MalformedURLException e) {
               throw new TokenParseException(e.getMessage(), e);
          } catch (IllegalStateException e) {
               throw new TokenParseException(e.getMessage(), e);
          }
     }
     /**
      * 譏守､ｺ逧�↑譛牙柑譛滄俣繧定ｨｭ螳壹＠縺ｦ繝医�繧ｯ繝ｳ繧堤函謌舌☆繧�.
      * @param issuedAt 逋ｺ陦梧凾蛻ｻ(epoch縺九ｉ縺ｮ繝溘Μ遘�
      * @param lifespan 繝医�繧ｯ繝ｳ縺ｮ譛牙柑譎る俣�医Α繝ｪ遘抵ｼ�
      * @param issuer 逋ｺ陦�Cell URL
      * @param subject 繧｢繧ｯ繧ｻ繧ｹ荳ｻ菴填RL
      * @param roleList 繝ｭ繝ｼ繝ｫ繝ｪ繧ｹ繝�
      * @param schema 繧ｯ繝ｩ繧､繧｢繝ｳ繝郁ｪ崎ｨｼ縺輔ｌ縺溘ョ繝ｼ繧ｿ繧ｹ繧ｭ繝ｼ繝�
      * @param userID userID
      */
     public CellLocalAccessToken(final long issuedAt, final long lifespan,
               final String issuer, final String subject,
               final List<Role> roleList, final String schema, final String userID) {
          super(issuedAt, lifespan, issuer, subject, schema, userID);
          this.roleList = roleList;
          this.userName = userID;
     }
     /**
      * 譌｢螳壼�縺ｮ譛牙柑譛滄俣繧定ｨｭ螳壹＠縺ｦ繝医�繧ｯ繝ｳ繧堤函謌舌☆繧�.
      * @param issuedAt 逋ｺ陦梧凾蛻ｻ(epoch縺九ｉ縺ｮ繝溘Μ遘�
      * @param issuer 逋ｺ陦�Cell URL
      * @param subject 繧｢繧ｯ繧ｻ繧ｹ荳ｻ菴填RL
      * @param roleList 繝ｭ繝ｼ繝ｫ繝ｪ繧ｹ繝�
      * @param schema 繧ｯ繝ｩ繧､繧｢繝ｳ繝郁ｪ崎ｨｼ縺輔ｌ縺溘ョ繝ｼ繧ｿ繧ｹ繧ｭ繝ｼ繝�
      * @param userID String
      */
     public CellLocalAccessToken(final long issuedAt, final String issuer,
               final String subject, final List<Role> roleList, final String schema, final String userID) {
          this(issuedAt, ACCESS_TOKEN_EXPIRES_HOUR * MILLISECS_IN_AN_HOUR,
                    issuer, subject, roleList, schema, userID);
     }
     /**
      * 譌｢螳壼�縺ｮ譛牙柑譛滄俣縺ｨ迴ｾ蝨ｨ繧堤匱陦梧律譎ゅ→險ｭ螳壹＠縺ｦ繝医�繧ｯ繝ｳ繧堤函謌舌☆繧�.
      * @param issuer 逋ｺ陦�Cell URL
      * @param subject 繧｢繧ｯ繧ｻ繧ｹ荳ｻ菴填RL
      * @param roleList 繝ｭ繝ｼ繝ｫ繝ｪ繧ｹ繝�
      * @param schema 繧ｯ繝ｩ繧､繧｢繝ｳ繝郁ｪ崎ｨｼ縺輔ｌ縺溘ョ繝ｼ繧ｿ繧ｹ繧ｭ繝ｼ繝�
      * @param userID String
      */
     public CellLocalAccessToken(final String issuer, final String subject,
               final List<Role> roleList, final String schema, final String userID) {
          this(new DateTime().getMillis(), issuer, subject, roleList, schema, userID);
     }

     @Override
     public String getId() {
          return this.subject + ":" + this.issuedAt;
     }

     @Override
     public String getTarget() {
          return null;
     }

     @Override
     public String toTokenString() {
          StringBuilder ret = new StringBuilder(PREFIX_ACCESS);
          ret.append(this.doCreateTokenString(new String[]{this.makeRolesString()}));
          return ret.toString();
     }

}
