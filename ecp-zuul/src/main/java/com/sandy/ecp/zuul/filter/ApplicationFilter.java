package com.sandy.ecp.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class ApplicationFilter extends ZuulFilter {

	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
	    HttpServletRequest request = context.getRequest();
	    String token = request.getParameter("token");
	    if (token == null){
	      context.setSendZuulResponse(false);
	      context.setResponseStatusCode(401);
	      context.setResponseBody("unAuthrized");
	    }
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return false;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return null;
	}

}
