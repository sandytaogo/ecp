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

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * power designer document resolved 解析
 * @author Sandy
 * @version 1.0.0
 * @since DTC 27 2026
 */
public class PdmParse {

	/**
	 * 
	 * @param args
	 * @throws DocumentException 
	 */
	public static void main(String [] args) throws DocumentException {
		SAXReader sax = new SAXReader();
		Document doc = sax.read("");
		Node node = doc.selectSingleNode("//c:Children/o:Model");
		Element ele = null;
		Integer.parseInt(null);
	}
	
}
