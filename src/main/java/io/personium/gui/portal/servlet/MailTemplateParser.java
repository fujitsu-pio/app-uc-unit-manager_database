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
package io.personium.gui.portal.servlet;

import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.personium.gui.portal.PropertiesUtil;
/**
 *  Mail Template class for mail process.
 */
public class MailTemplateParser {
    private boolean blanknull  = false;

    /**
     * This substitutes dynamic values.
     * @param map substitution map
     * @param matcher matcher
     * @return substituted string
     */
    public String substitute(Map<String, ? extends Object> map, Matcher matcher) {
        matcher.reset();
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String k0 = matcher.group();
            String k = matcher.group(1);
            Object vobj = map.get(k);
            String v = null;
            if (vobj == null) {
                if (this.blanknull) {
                    v = "";
                } else {
                   v = k0;
                }
            } else {
                v = vobj.toString();
            }
            /*String v = (vobj == null) ? (this.blanknull ? "" : k0) : vobj
                    .toString();*/
            matcher.appendReplacement(sb, Matcher.quoteReplacement(v));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * This method sets blank as null.
     * @return this
     */
    public MailTemplateParser setBlankNull() {
        this.blanknull = true;
        return this;
    }

    /**
     * This method replaces constants with dynamic data.
     * @param mapText mapText
     * @param text text
     * @return formattedText
     */
    protected String updateMailTemplateWithDynamicData(Map<String, String> mapText, String text) {
        Properties refPropertiesUtil = PropertiesUtil.getProperties();
        String urlPattern = refPropertiesUtil.getProperty("emailTemplateKeyPattern");
        Pattern mailTemplatePattern = Pattern.compile(urlPattern);
        Matcher matcher = mailTemplatePattern.matcher(text);
        String formattedText = null;
        setBlankNull();
        formattedText = substitute(mapText, matcher);
        return formattedText;
    }
}
