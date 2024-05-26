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
import java.nio.charset.StandardCharsets;

public class URLEncoder {

	static String dfltEncName = StandardCharsets.UTF_8.name();

	/**
	 * You can't call the constructor.
	 */
	private URLEncoder() {
		super();
	}

	/**
     * Translates a string into {@code x-www-form-urlencoded}
     * format. This method uses the platform's default encoding
     * as the encoding scheme to obtain the bytes for unsafe characters.
     * @param   s   {@code String} to be translated.
     * @return  the translated {@code String}.
     */
    public static String encode(String s) {
        String str = null;
        try {
            str = encode(s, dfltEncName);
        } catch (UnsupportedEncodingException e) {
            // The system should always have the platform default
        }
        return str;
    }
    
    public static String encode(String s, String enc) throws UnsupportedEncodingException {
    	if (s == null) {
    		return null;
    	}
    	return java.net.URLEncoder.encode(s, enc);
    }
}
