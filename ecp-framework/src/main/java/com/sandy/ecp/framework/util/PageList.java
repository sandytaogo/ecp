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
package com.sandy.ecp.framework.util;

import java.util.List;

/**
 * paging list
 * @author Sandy
 * @param <E>
 * @since 1.0.0 23th 06 2017
 */
//<img class="figure_pic" onerror="picerr(this,'h');" _stat="videolist:click" src="//puui.qpic.cn/vpic/0/q0027jnj72b_160_90_3.jpg/0" alt="复仇者联盟3：无限战争(普通话版)">
public interface PageList<E> extends Iterable<E> {
	/**
	 * all data
	 * 
	 * @return list 
	 */
	public List<E> getData();
	/**
	 * get page
	 * @return current page
	 */
	public int getPage();
	/**
	 * get page size
	 * @return pageSize
	 */
	public int getPageSize();
	/**
	 * page total
	 * 
	 * @return total
	 */
	public int getPages();
	/**
	 * elements total
	 * 
	 * @return total
	 */
	public long getTotal();
	
}
