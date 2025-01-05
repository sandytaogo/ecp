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

import org.springframework.context.MessageSource;

import com.sandy.ecp.framework.context.SpringContextHolder;
import com.sandy.ecp.framework.i18n.EcpResourceBundleMessageSource;
import com.sandy.ecp.framework.i18n.I18nResourceServiceImpl;

/**
 * 企业云平台国际化工具栏.
 * 
 * @author Sandy
 * @since 1.0.0 12th 12 2024
 */
public class EcpI18nUtil {

	private static Locale defaultGlobalLocale = Locale.getDefault();

	private static MessageSource messageSource;

	public static Locale getLoginLocale() {
		return defaultGlobalLocale;
	}

	public static String getMessage(final String key, final String defaultMessage) {
		if (StringUtil.isEmpty(key)) {
			return defaultMessage;
		}
		if (messageSource == null) {
			messageSource = SpringContextHolder.getBean(EcpResourceBundleMessageSource.class);
		}
		String message = messageSource.getMessage(key, null, defaultMessage, defaultGlobalLocale);
		if (StringUtil.isNotEmptyString(message)) {
			return message;
		}
		message = new I18nResourceServiceImpl(null, null).getExceptionResourceMessage(key);
		if (StringUtil.isEmpty(message)) {
			return defaultMessage;
		}
		return message;
	}

	public static String getMessage(String key, String defaultMessage, Object... objects) {
		if (StringUtil.isEmpty(key)) {
			return defaultMessage;
		}
		if (messageSource == null) {
			messageSource = SpringContextHolder.getBean(EcpResourceBundleMessageSource.class);
		}
		String message = messageSource.getMessage(key, objects, defaultMessage, defaultGlobalLocale);
		if (StringUtil.isEmpty(message)) {
			return defaultMessage;
		}
		return message;
	}

}
