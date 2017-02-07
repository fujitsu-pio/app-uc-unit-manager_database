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
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;


/**
 * TransCellTokenで認証したときに発行するリフレッシュトークンを扱うクラス. 生成・パースが基本機能. このトークン自体はRefreshTokenなのでCellをまたぐ納涼は与えていない。
 * あくまで、このトークンを発行したCellがもう一度トークンを発行するのみの用途で用いる。 発行スコープ情報をここでは持っておきたい。
 */
public final class TransCellRefreshToken extends LocalToken implements IRefreshToken, IExtRoleContainingToken {

    /**
     * この種類のトークンのプレフィックス.
     */
    public static final String PREFIX_TC_REFRESH = "RT~";
    /**
     * トークンの有効時間.
     */
    public static final int REFRESH_TOKEN_EXPIRES_HOUR = 24;

    String id;
    String originalIssuer;

    /**
     * 明示的な有効期間を設定してトークンを生成する.
     * @param id トークンの一意識別子
     * @param issuedAt 発行時刻(epochからのミリ秒)
     * @param lifespan 有効期間(ミリ秒)
     * @param issuer 発行者URL
     * @param subject アクセス主体URL
     * @param origIssuer このRefreshToken発行の際に使われた、元のTransCell アクセストークンの発行者
     * @param origRoleList このRefreshToken発行の際に使われた、元のTransCell アクセストークンに書かれたロールリスト
     * @param schema クライアント認証されたデータスキーマ
     * @param userName userName
     */
    public TransCellRefreshToken(
            final String id,
            final long issuedAt,
            final long lifespan,
            final String issuer,
            final String subject,
            final String origIssuer,
            final List<Role> origRoleList,
            final String schema, final String userName) {
        super(issuedAt, lifespan, issuer, subject, schema, userName);
        this.id = id;
        this.originalIssuer = origIssuer;
        this.roleList = origRoleList;
        this.userName = userName;
    }

    /**
     * 既定値の有効期間を設定してトークンを生成する.
     * @param id トークンの一意識別子
     * @param issuedAt 発行時刻(epochからのミリ秒)
     * @param issuer 発行者URL
     * @param subject アクセス主体URL
     * @param origIssuer このRefreshToken発行の際に使われた、元のTransCell アクセストークンの発行者
     * @param origRoleList このRefreshToken発行の際に使われた、元のTransCell アクセストークンに書かれたロールリスト
     * @param schema クライアント認証されたデータスキーマ
     * @param userName userName
     */
public TransCellRefreshToken(
          final String id,
     final long issuedAt,
     final String issuer,
     final String subject,
     final String origIssuer,
     final List<Role> origRoleList,
     final String schema, final String userName) {
     this(id, issuedAt,
          REFRESH_TOKEN_EXPIRES_HOUR * MILLISECS_IN_AN_HOUR,
     issuer,
     subject, origIssuer, origRoleList, schema, userName);
     }

    /**
     * 既定値の有効期間を設定してトークンを生成する.
     * @param id トークンの一意識別子
     * @param issuer 発行者URL
     * @param subject アクセス主体URL
     * @param origIssuer このRefreshToken発行の際に使われた、元のTransCell アクセストークンの発行者
     * @param origRoleList このRefreshToken発行の際に使われた、元のTransCell アクセストークンに書かれたロールリスト
     * @param schema クライアント認証されたデータスキーマ
     * @param userName userName
     */
    public TransCellRefreshToken(
            final String id,
            final String issuer,
            final String subject,
            final String origIssuer,
            final List<Role> origRoleList,
            final String schema, final String userName) {
        this(id, new DateTime().getMillis(), issuer, subject, origIssuer, origRoleList, schema, userName);
    }

    @Override
     public String toTokenString() {
     StringBuilder ret = new StringBuilder(PREFIX_TC_REFRESH);
     String[] items = new String[] {this.id, this.originalIssuer, this.makeRolesString(), this.userName};
     ret.append(this.doCreateTokenString(items));
     return ret.toString();
     }

    static final int IDX_COUNT = 9;
    static final int IDX_ID = 4;
    static final int IDX_ISSUED_AT = 0;
    static final int IDX_LIFESPAN = 1;
    static final int IDX_ISSUER = 8;
    static final int IDX_SUBJECT = 2;
    static final int IDX_ORIG_ISSUER = 5;
    static final int IDX_ORIG_ROLE_LIST = 6;
    static final int IDX_SCHEMA = 3;
    static final int IDX_USERNAME = 7;

    /**
     * トークン文字列をissuerで指定されたCellとしてパースする.
     * @param token Token String
     * @param issuer Cell Root URL
     * @return パースされたCellLocalTokenオブジェクト
     * @throws AbstractOAuth2Token.TokenParseException トークンのパースに失敗したとき投げられる例外
     */
    public static TransCellRefreshToken parse(final String token, final String issuer)
            throws AbstractOAuth2Token.TokenParseException {
        if (!token.startsWith(PREFIX_TC_REFRESH) || issuer == null) {
            throw AbstractOAuth2Token.PARSE_EXCEPTION;
        }
        String[] frag = LocalToken.doParse(token.substring(PREFIX_TC_REFRESH.length()), issuer, IDX_COUNT);
        try {
            TransCellRefreshToken ret = new TransCellRefreshToken(
                    frag[IDX_ID],
                    Long.valueOf(StringUtils.reverse(frag[IDX_ISSUED_AT])),
                    Long.valueOf(frag[IDX_LIFESPAN]),
                    frag[IDX_ISSUER],
                    frag[IDX_SUBJECT],
                    frag[IDX_ORIG_ISSUER],
                    AbstractOAuth2Token.parseRolesString(frag[IDX_ORIG_ROLE_LIST]),
                    frag[IDX_SCHEMA], frag[IDX_USERNAME]);
            return ret;
        } catch (MalformedURLException e) {
            throw AbstractOAuth2Token.PARSE_EXCEPTION;
        } catch (IllegalStateException e) {
            throw AbstractOAuth2Token.PARSE_EXCEPTION;
        }
    }
    /**
     * This methods gets ID.
     * @return id id.
     */
    @Override
    public String getId() {
        return this.id;
    }
    /**
     * This method gets refreshAccessToken.
     * @param issuedAt long
     * @param target String
     * @param url String
     * @param role List
     * @return IAccessToken IAccessToken.
     */
    @Override
    public IAccessToken refreshAccessToken(final long issuedAt, final String target, String url, List<Role> role) {
        if (target == null) {
            return new CellLocalAccessToken(issuedAt, url, this.getSubject(), role, this.getSchema(), this.userName);
        } else {
            return new TransCellAccessToken(issuedAt, url, this.getSubject(), target, role, null);
        }
    }

    /**
     * This method gets refreshRefreshToken for issuedAt.
     * @param issuedAt long
     * @return IRefreshToken IRefreshToken
     */
    @Override
    public IRefreshToken refreshRefreshToken(final long issuedAt) {
        // TODO 本当は ROLEは再度読み直すべき。
        return new TransCellRefreshToken(UUID.randomUUID().toString(), issuedAt, this.issuer, this.subject,
                this.originalIssuer, this.getRoles(), this.schema, this.userName);
    }
    /**
     * Gets target.
     * @return null
     */
    @Override
    public String getTarget() {
        return null;
    }
    /**
     * Gets ExtCellUrl.
     * @return originalIssuer
     */
    @Override
    public String getExtCellUrl() {
        return this.originalIssuer;
    }
    /**
     * Gets roleList.
     * @return List<Role>
     */
    @Override
    public List<Role> getRoleList() {
        return this.getRoles();
    }
}
