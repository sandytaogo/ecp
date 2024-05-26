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
package com.sandy.ecp.framework.model;

/**
 * Framework Model State.
 * @author Sandy
 * @param <PK>
 * @version 1.0.0
 * @since 1.0.0 04th 12 2018
 */
public class ModelState {
	
	public static final String NAME = "modelState";
	
	public static final int DEFAULT = 1;
	
	public static final int DELETE = 2;
	
	public static final int NEW = 4;
	
	public static final int MODIFY = 8;
	
	public static final int SELECTED = 16;
	
	public static final int CHILD_MODIFY = 64;
}
