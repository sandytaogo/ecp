/*
 * Copyright 2024-2040 the original author or authors.
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
package com.sandy.ecp.recognition;

import java.text.MessageFormat;

import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenCvCore {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(CmdImageConvertString.class);

	static {
	    try {
	        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    } catch (UnsatisfiedLinkError e) {
	    	if (LOGGER.isErrorEnabled()) {
	    		LOGGER.error(MessageFormat.format("本地库加载失败，请检查路径={0}", e.getMessage()), e);
	    	}
	    }
	}
	
}
