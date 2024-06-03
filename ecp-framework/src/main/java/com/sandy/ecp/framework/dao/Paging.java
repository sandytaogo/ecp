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
package com.sandy.ecp.framework.dao;

/**
 * 分页信息服务接口
 * @author Sandy
 * @date 2022-04-24 12:12:12
 * @since 1.0.0 
 */
public interface Paging
{
    int getPageNumber();
    
    int getPageSize();
    
    int getOffset();
    
    Sort getSort();
    
    Paging next();
    
    Paging previous();
    
    Paging first();
    
    boolean hasPrevious();
}
