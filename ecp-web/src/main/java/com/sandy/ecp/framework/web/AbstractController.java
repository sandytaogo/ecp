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
package com.sandy.ecp.framework.web;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sandy.ecp.framework.dao.PageRequest;
import com.sandy.ecp.framework.dao.Paging;
import com.sandy.ecp.framework.dao.Sort;
import com.sandy.ecp.framework.dao.Sort.Direction;
import com.sandy.ecp.framework.dao.Sort.Order;
import com.sandy.ecp.framework.net.URLDecoder;
import com.sandy.ecp.framework.session.SessionVO;
import com.sandy.ecp.framework.util.ConvertUtil;
import com.sandy.ecp.framework.util.StringUtil;
import com.sandy.ecp.framework.web.context.EcpWebSecurityContextHolder;

/**
 * return null != session ? "forward:/index.jsp" :"redirect:/login.jsp";
 * @author Sandy
 * @date 2020-04-05 14:12:12
 * @since 1.0.0
 */
public abstract class AbstractController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected static final ThreadLocal<Object> THREAD_LOCAL = new InheritableThreadLocal<Object>();
	
	public HttpServletRequest getRequest() {
        final HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
	
	public SessionVO getSessionVO() {
		return EcpWebSecurityContextHolder.getSessionVO();
	}
	
	public SessionVO getSessionVO(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			Object details = session.getAttribute(SessionVO.KEY);
			if (details instanceof SessionVO) {
				return (SessionVO) details;
			}
		}
		return null;
	}
	
	public Paging getPaging(HttpServletRequest request) {
		String pageNum = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		Integer pageNo = 1, pageSizeNo = 10;
		if (StringUtil.isNumber(pageNum)) {
			pageNo = ConvertUtil.toInteger(pageNum);
		}
		if (StringUtil.isNumber(pageSize)) {
			pageSizeNo = ConvertUtil.toInteger(pageSize);
		}
		Sort sort = new Sort(new Order(Direction.ASC, "SORT"), new Order(Direction.DESC, "CREATED_TIME"));
		Paging paging = new PageRequest(pageNo, pageSizeNo, sort);
		return paging;
	}

	protected ModelMap paramToMap(final HttpServletRequest request) {
		final ModelMap model = new ModelMap();
		if (null == request) {
			return model;
		}
		final Enumeration<String> parameterNames = (Enumeration<String>) request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			final String parameterName = parameterNames.nextElement();
			model.put(parameterName, URLDecoder.decode(request.getParameter(parameterName)));
		}
		return model;
	}
	
}
