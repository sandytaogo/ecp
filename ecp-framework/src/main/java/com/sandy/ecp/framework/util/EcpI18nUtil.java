/*
 * Copyright 2024-2030 the original author or authors.
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
package com.sandy.ecp.framework.util;

import java.util.Locale;

import com.sandy.ecp.framework.i18n.I18nResourceServiceImpl;

public class EcpI18nUtil {
	
	private static Locale defaultGlobalLocale = Locale.getDefault();
	
	public static Locale getLoginLocale() {
		return defaultGlobalLocale;
	}

	private static String getMessage(final String key, final String content) {
        if (StringUtil.isEmpty(key)) {
            return content;
        }
        final String res = new I18nResourceServiceImpl(null, null).getExceptionResourceMessage(key);
        if (StringUtil.isEmpty(res)) {
            return content;
        }
        return res;
    }
	
}
