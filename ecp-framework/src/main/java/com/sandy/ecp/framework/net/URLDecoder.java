/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.framework.net;

import java.io.UnsupportedEncodingException;

public class URLDecoder {
	
	// The platform default encoding
    static String dfltEncName = URLEncoder.dfltEncName;

	 /**
     * Decodes a {@code x-www-form-urlencoded} string.
     * The platform's default encoding is used to determine what characters
     * are represented by any consecutive sequences of the form
     * "<i>{@code %xy}</i>".
     * @param s the {@code String} to decode
     * @return the newly decoded {@code String}
     */
    public static String decode(String s) {
        String str = null;
        try {
            str = decode(s, dfltEncName);
        } catch (UnsupportedEncodingException e) {
            // The system should always have the platform default
        }
        return str;
    }
	
    /**
     * Decodes a {@code application/x-www-form-urlencoded} string using a specific
     * encoding scheme.
     * The supplied encoding is used to determine
     * what characters are represented by any consecutive sequences of the
     * form "<i>{@code %xy}</i>".
     * <p>
     * <em><strong>Note:</strong> The <a href=
     * "http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
     * World Wide Web Consortium Recommendation</a> states that
     * UTF-8 should be used. Not doing so may introduce
     * incompatibilities.</em>
     *
     * @param s the {@code String} to decode
     * @param enc   The name of a supported
     *    <a href="../lang/package-summary.html#charenc">character
     *    encoding</a>.
     * @return the newly decoded {@code String}
     * @exception  UnsupportedEncodingException
     *             If character encoding needs to be consulted, but
     *             named character encoding is not supported
     * @see URLEncoder#encode(java.lang.String, java.lang.String)
     * @since 1.4
     */
    public static String decode(String s, String enc) throws UnsupportedEncodingException { 
    	if (s == null) {
    		return null;
    	}
    	return java.net.URLDecoder.decode(s, enc);
    }
}
