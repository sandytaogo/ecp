/*
 * Copyright 2023-2030 the original author or authors.
 *
 * Licensed under the sandy License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.sandy.org/licenses/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.framework.domain;

import java.io.Serializable;

public class PageRequest implements Paging, Serializable {
    private static final long serialVersionUID = 6585040613400357967L;
    private final int page;
    private final int size;
    private final Sort sort;
    
    protected PageRequest() {
        this(0, 10, null);
    }
    
    public PageRequest(final int page, final int size) {
        this(page, size, null);
    }
    
    public PageRequest(final int page, final int size, final Sort.Direction direction, final String... properties) {
        this(page, size, new Sort(direction, properties));
    }
    
    public PageRequest(final int page, final int size, final Sort sort) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one!");
        }
        this.page = page;
        this.size = size;
        this.sort = sort;
    }
    
    @Override
    public int getPageNumber() {
        return this.page;
    }
    
    @Override
    public int getPageSize() {
        return this.size;
    }
    
    @Override
    public int getOffset() {
        return (this.page - 1) * this.size;
    }
    
    @Override
    public Sort getSort() {
        return this.sort;
    }
    
    @Override
    public Paging next() {
        return new PageRequest(this.getPageNumber() + 1, this.getPageSize(), this.getSort());
    }
    
    @Override
    public Paging previous() {
        return (this.getPageNumber() == 0) ? this : new PageRequest(this.getPageNumber() - 1, this.getPageSize(), this.getSort());
    }
    
    @Override
    public Paging first() {
        return new PageRequest(0, this.getPageSize(), this.getSort());
    }
    
    @Override
    public boolean hasPrevious() {
        return this.page > 0;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PageRequest)) {
            return false;
        }
        final PageRequest that = (PageRequest)obj;
        final boolean sortEqual = (this.sort == null) ? (that.sort == null) : this.sort.equals(that.sort);
        return this.page == that.page && this.size == that.size && sortEqual;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.page;
        result = prime * result + this.size;
        return prime * result + ((null == this.sort) ? 0 : this.sort.hashCode());
    }
    
    @Override
    public String toString() {
        return String.format("Page request [number: %d, size %d, sort: %s]", this.getPageNumber(), this.getPageSize(), (this.sort == null) ? null : this.sort.toString());
    }
}
