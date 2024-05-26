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
package com.sandy.ecp.framework.zip;

import java.io.FilterOutputStream;
import java.io.OutputStream;

/**
 * zip 压缩文件支持.
 * @author Sandy
 * @see https://www.cnblogs.com/cliveleo/articles/9759019.html
 * @since 1.0.0 28th 12 2018
 */
public class ZipOutputStream extends FilterOutputStream {

	public ZipOutputStream(OutputStream out) {
		super(out);
	}
}
