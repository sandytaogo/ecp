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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sandy.ecp.framework.domain.PageRequest;
import com.sandy.ecp.framework.domain.Paging;
import com.sandy.ecp.framework.domain.Sort;
import com.sandy.ecp.framework.domain.Sort.Direction;
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
	
	@Autowired(required = false)
	protected Environment environment;

	/**
	 * 线程本地变量.
	 */
	protected static final ThreadLocal<Object> THREAD_LOCAL = new InheritableThreadLocal<Object>();
	
	public HttpServletRequest getRequest() {
        final HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
	
	/**
	 * 获取临时会话信息.
	 * @param request
	 * @return
	 */
	public SessionVO getTemporarySession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		SessionVO sessionVO = (SessionVO) session.getAttribute(SessionVO.TEMPORARY_KEY);
		if (sessionVO == null) {
			sessionVO = (SessionVO) session.getAttribute(SessionVO.KEY);
		}
		return sessionVO;
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
	
	/**
	 * 获取排序对象.
	 * @param request HttpServletRequest.
	 * @param args [sortName, order:asc or DESC].
	 * @return Sort
	 */
	public Sort getSort(HttpServletRequest request, String ...args) {
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		Sort sortOrder = null;
		if (StringUtil.isNotEmptyString(sort) && StringUtil.isNotEmptyString(order)) {
			sortOrder = new Sort(Direction.fromString(order), sort);
		}
		return sortOrder;
	}
	
	public Paging getPaging(HttpServletRequest request, Sort sort) {
		String pageNum = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		Integer pageNo = 1, pageSizeNo = 20;
		if (StringUtil.isNumber(pageNum)) {
			pageNo = ConvertUtil.toInteger(pageNum);
		}
		if (StringUtil.isNumber(pageSize)) {
			pageSizeNo = ConvertUtil.toInteger(pageSize);
		}
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
	
	/**
	 * 读入HTTP body data 注意只能读取一次IO
	 * @param request
	 * @return String
	 * @throws IOException
	 */
	protected String readerString(final HttpServletRequest request) throws IOException {
		BufferedReader br = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line;
		while((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
	
	/**
	 * 系统之间通信许可.
	 * @param request HttpServletRequest.
	 * @return boolean
	 */
	public boolean accessSystemAllow(final HttpServletRequest request) {
		String systemId = request.getHeader("systemid");
		if (systemId != null ) {
			return systemId.equals(environment.getProperty("system.id"));
		}
		return false;
	}
}
