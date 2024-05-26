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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * paging array list
 * @author Sandy
 * @Since 1.0.0 23th 06 2017
 * @param <E>
 */
public class PagingArrayList<E> implements PagingList<E>, Serializable {

	private static final long serialVersionUID = -1470272076414702572L;
	
	public static final int DEFAULT_PAGE_SIZE = 10;
	public static final int MAX_PAGE_SIZE = 100;
	
	private List<E> data = new ArrayList<E>();
	private int page;
	private int pageSize;
	private long total;
	
	public Iterator<E> iterator() {
		return data.iterator();
	}
	
	public boolean add(E e) {
		return data.add(e);
	}
	
	public boolean addAll(Collection<? extends E> c) {
		return data.addAll(c);
	}

	public List<E> getData() {
		return data;
	}
	
	public void setData(List<E> data) {
		this.data = data;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPages() {
		//Computational paging for Total page
		//System.arraycopy(src, srcPos, dest, destPos, length);
		
		return (int) (total / pageSize) + (0 ==(total % pageSize) ? 0 : 1);
	}
	public long getTotal() {
		return total;
	}
	
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public void setTotal(long total) {
		this.total = total;
	}
	
	 //  String conversion

    /**
     * Returns a string representation of this collection.  The string
     * representation consists of a list of the collection's elements in the
     * order they are returned by its iterator, enclosed in square brackets
     * (<tt>"[]"</tt>).  Adjacent elements are separated by the characters
     * <tt>", "</tt> (comma and space).  Elements are converted to strings as
     * by {@link String#valueOf(Object)}.
     *
     * @return a string representation of this collection
     */
	@Override
    public String toString() {
        Iterator<E> it = iterator();
        if (! it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }
}
