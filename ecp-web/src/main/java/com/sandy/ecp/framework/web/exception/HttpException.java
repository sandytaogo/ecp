/*
 * Copyright 2018-2030 the original author or authors.
 *
 * Licensed under the company, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.company.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.framework.web.exception;

/**
 * HTTP 自定义异常.
 * @author Sandy
 * @date 2023-05-05 12:12:12
 */
public class HttpException extends RuntimeException {

	private static final long serialVersionUID = 6998016710769738641L;

	public HttpException() {
		super();
	}
	public HttpException(Throwable cause) {
	        super(cause);	
	}
	public HttpException(String message) {
        super(message);
    }
	
	public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
