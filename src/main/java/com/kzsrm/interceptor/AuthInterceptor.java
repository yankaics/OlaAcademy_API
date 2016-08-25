package com.kzsrm.interceptor;

public class AuthInterceptor extends BaseAuthInterceptor {
	static {
		useUriFilterForNoCheck();
		controllerUriFilter.add("");
		
		
	}

	

}
